package de.fuballer.mcendgame.main.component.portal

import com.mojang.logging.LogUtils
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import de.fuballer.mcendgame.main.component.portal.teleport.TeleportLocation
import de.fuballer.mcendgame.main.component.portal.type.DefaultPortalType
import de.fuballer.mcendgame.main.component.portal.type.PortalType
import de.fuballer.mcendgame.main.messaging.portal.PortalUsedEvent
import de.maucon.mauconframework.event.EventGateway
import net.minecraft.entity.EntityType
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.MovementType
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtOps
import net.minecraft.util.ActionResult
import net.minecraft.util.Arm
import net.minecraft.util.Hand
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import java.util.*
import kotlin.jvm.optionals.getOrNull

private const val DATA_KEY = "portal_entity_data"

class PortalEntity(
    entityType: EntityType<out LivingEntity>,
    world: World,
) : LivingEntity(entityType, world) {
    private val log = LogUtils.getLogger()

    private var removed: Boolean = false
    var type: PortalType = DefaultPortalType()
    var teleportLocation: TeleportLocation? = null
    var singleUse = false

    private data class PortalEntityData(
        val typeId: String,
        val singleUse: Boolean,
        val teleportLocation: TeleportLocation?,
    ) {
        companion object {
            val CODEC: Codec<PortalEntityData> = RecordCodecBuilder.create { instance ->
                instance.group(
                    Codec.STRING.fieldOf("type_id").forGetter(PortalEntityData::typeId),
                    Codec.BOOL.fieldOf("single_use").forGetter(PortalEntityData::singleUse),
                    TeleportLocation.CODEC.lenientOptionalFieldOf("teleport_location").forGetter { Optional.ofNullable(it.teleportLocation) },
                ).apply(instance) { typeId, singleUse, location ->
                    PortalEntityData(typeId, singleUse, location.getOrNull())
                }
            }
        }
    }

    init {
        this.setNoGravity(true)
        isInvulnerable = true
        noClip = true
    }

    companion object {
        val TYPE: TrackedData<String> = DataTracker.registerData(PortalEntity::class.java, TrackedDataHandlerRegistry.STRING)
    }

    override fun tick() {
        if (removed) {
            discard()
            return
        }

        super.tick()
        type.tickAnimation(this)
    }

    override fun interactAt(player: PlayerEntity, hitPos: Vec3d, hand: Hand): ActionResult {
        if (entityWorld.isClient) return ActionResult.PASS
        if (hand != Hand.MAIN_HAND) return ActionResult.PASS

        val event = PortalUsedEvent(player, teleportLocation)
        EventGateway.publish(event)

        if (singleUse) {
            remove(RemovalReason.KILLED)
        }
        return ActionResult.SUCCESS
    }

    override fun shouldRenderName(): Boolean = false

    override fun isOnFire() = false
    override fun canFreeze() = false

    override fun move(type: MovementType, movement: Vec3d) {}

    override fun getEquippedStack(slot: EquipmentSlot): ItemStack = ItemStack.EMPTY
    override fun equipStack(slot: EquipmentSlot?, stack: ItemStack) {}
    override fun getMainArm(): Arm = Arm.RIGHT

    override fun damage(source: DamageSource, amount: Float) = false

    override fun getArmorItems(): Iterable<ItemStack> = listOf()

    override fun kill() {
        remove(RemovalReason.KILLED)
    }

    override fun initDataTracker(builder: DataTracker.Builder) {
        super.initDataTracker(builder)
        builder.add(TYPE, "default")
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)

        if (!nbt.contains(DATA_KEY)) {
            log.info("Marking outdated portal to be removed: $uuid")
            removed = true
            return
        }

        val result = PortalEntityData.CODEC
            .parse(NbtOps.INSTANCE, nbt.get(DATA_KEY))
            .result()
        if (result.isEmpty) {
            log.warn("Cannot load data of portal: $uuid")
            removed = true
            return
        }

        val data = result.get()
        if (data.teleportLocation == null) {
            log.warn("Teleport Location is missing, removing portal: $uuid")
            removed = true
            return
        }

        type = PortalType.getById(data.typeId)
        dataTracker.set(TYPE, data.typeId)

        if (world.isClient) return

        singleUse = data.singleUse
        teleportLocation = data.teleportLocation
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)

        nbt.put(
            DATA_KEY,
            PortalEntityData.CODEC.encodeStart(
                NbtOps.INSTANCE,
                PortalEntityData(type.getId(), singleUse, teleportLocation),
            ).getOrThrow(),
        )
    }
}