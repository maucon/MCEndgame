package de.fuballer.mcendgame.main.component.portal

import com.mojang.logging.LogUtils
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import de.fuballer.mcendgame.main.component.portal.teleport.TeleportLocation
import de.fuballer.mcendgame.main.component.portal.type.DefaultPortalType
import de.fuballer.mcendgame.main.component.portal.type.PortalType
import de.fuballer.mcendgame.main.messaging.portal.PortalUsedEvent
import de.maucon.mauconframework.event.EventGateway
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.*
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.minecraft.world.phys.Vec3
import java.util.*
import kotlin.jvm.optionals.getOrNull

private const val DATA_KEY = "portal_entity_data"

class PortalEntity(
    entityType: EntityType<out LivingEntity>,
    world: Level,
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
        this.isNoGravity = true
        isInvulnerable = true
        noPhysics = true
    }

    companion object {
        val TYPE: EntityDataAccessor<String> = SynchedEntityData.defineId(PortalEntity::class.java, EntityDataSerializers.STRING)
    }

    override fun tick() {
        if (removed) {
            discard()
            return
        }

        super.tick()
        type.tickAnimation(this)
    }

    override fun interact(player: Player, hand: InteractionHand, hitPos: Vec3): InteractionResult {
        if (level().isClientSide) return InteractionResult.PASS
        if (hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS

        val event = PortalUsedEvent(player, teleportLocation)
        EventGateway.publish(event)

        if (singleUse) {
            remove(RemovalReason.KILLED)
        }
        return InteractionResult.SUCCESS
    }

    override fun shouldShowName(): Boolean = false

    override fun isOnFire() = false
    override fun canFreeze() = false

    override fun move(type: MoverType, movement: Vec3) {}

    override fun getItemBySlot(slot: EquipmentSlot): ItemStack = ItemStack.EMPTY
    override fun setItemSlot(slot: EquipmentSlot, stack: ItemStack) {}
    override fun getMainArm(): HumanoidArm = HumanoidArm.RIGHT

    override fun hurtServer(world: ServerLevel, source: DamageSource, amount: Float) = false

    override fun kill(level: ServerLevel) {
        remove(RemovalReason.KILLED)
    }

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        super.defineSynchedData(builder)
        builder.define(TYPE, "default")
    }

    override fun readAdditionalSaveData(view: ValueInput) {
        super.readAdditionalSaveData(view)

        if (!view.contains(DATA_KEY)) {
            log.info("Marking outdated portal to be removed: $uuid")
            removed = true
            return
        }

        val result = view.read(DATA_KEY, PortalEntityData.CODEC)
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
        entityData.set(TYPE, data.typeId)

        if (level().isClientSide) return

        singleUse = data.singleUse
        teleportLocation = data.teleportLocation
    }

    override fun addAdditionalSaveData(view: ValueOutput) {
        super.addAdditionalSaveData(view)

        view.store(DATA_KEY, PortalEntityData.CODEC, PortalEntityData(type.getId(), singleUse, teleportLocation))
    }
}