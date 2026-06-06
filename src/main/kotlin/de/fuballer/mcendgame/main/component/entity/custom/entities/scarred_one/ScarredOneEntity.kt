package de.fuballer.mcendgame.main.component.entity.custom.entities.scarred_one

import com.geckolib.animatable.GeoEntity
import com.geckolib.animatable.instance.AnimatableInstanceCache
import com.geckolib.animatable.manager.AnimatableManager
import com.geckolib.util.GeckoLibUtil
import de.fuballer.mcendgame.main.component.dungeon.generation.encounter.encounters.scarred_one.data.RolledScarredOneEffect
import de.fuballer.mcendgame.main.component.dungeon.generation.encounter.encounters.scarred_one.messaging.ScarredOneInteractEvent
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.addCustomAttribute
import de.maucon.mauconframework.event.EventGateway
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.minecraft.world.phys.Vec3
import kotlin.jvm.optionals.getOrDefault
import kotlin.random.Random

private const val POSITIVE_EFFECTS_KEY = "positive_effects"
private const val NEGATIVE_EFFECTS_KEY = "negative_effects"
private const val GOT_RESPONSE_KEY = "got_response"

class ScarredOneEntity(
    type: EntityType<out ScarredOneEntity>,
    world: Level,
) : Mob(type, world), GeoEntity {
    companion object {
        fun createAttributes(): AttributeSupplier.Builder {
            return createLivingAttributes()
                .add(Attributes.FOLLOW_RANGE, 15.0)
                .add(Attributes.MOVEMENT_SPEED, 0.0)
        }
    }

    var positiveEffects = listOf<RolledScarredOneEffect>()
    var negativeEffects = listOf<RolledScarredOneEffect>()

    var gotResponse = false

    fun respond(player: ServerPlayer, accept: Boolean, world: ServerLevel) {
        if (gotResponse) return
        gotResponse = true

        if (accept) {
            positiveEffects.forEach { world.addCustomAttribute(it.attribute, it.targets.predicate) }
            negativeEffects.forEach { world.addCustomAttribute(it.attribute, it.targets.predicate) }
            playAcceptedSound(world)
        }

        EventGateway.publish(ScarredOneDespawnEvent(player, this, accept))
    }

    private fun playAcceptedSound(world: ServerLevel) {
        world.playSound(this, blockPosition(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.NEUTRAL, 1F, 0.95F + 0.1F * Random.nextFloat())
    }

    fun hasRolledEffects() = positiveEffects.isNotEmpty() || negativeEffects.isNotEmpty()

    override fun registerGoals() {
        goalSelector.addGoal(0, LookAtPlayerGoal(this, Player::class.java, 8F, 1F))
    }

    private val cache: AnimatableInstanceCache = GeckoLibUtil.createInstanceCache(this)
    override fun getAnimatableInstanceCache() = cache

    override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {
    }

    override fun interact(
        player: Player,
        hand: InteractionHand,
        location: Vec3
    ): InteractionResult {
        val world = level() as? ServerLevel ?: return InteractionResult.PASS
        val serverPlayer = player as? ServerPlayer ?: return InteractionResult.PASS

        val event = ScarredOneInteractEvent(this, serverPlayer, world)
        EventGateway.publish(event)

        return InteractionResult.PASS
    }

    override fun isPushable() = false

    override fun addAdditionalSaveData(view: ValueOutput) {
        super.addAdditionalSaveData(view)

        view.store(POSITIVE_EFFECTS_KEY, RolledScarredOneEffect.LIST_CODEC, positiveEffects)
        view.store(NEGATIVE_EFFECTS_KEY, RolledScarredOneEffect.LIST_CODEC, negativeEffects)

        view.putBoolean(GOT_RESPONSE_KEY, gotResponse)
    }

    override fun readAdditionalSaveData(view: ValueInput) {
        super.readAdditionalSaveData(view)

        positiveEffects = view.read(POSITIVE_EFFECTS_KEY, RolledScarredOneEffect.LIST_CODEC).getOrDefault(listOf())
        negativeEffects = view.read(NEGATIVE_EFFECTS_KEY, RolledScarredOneEffect.LIST_CODEC).getOrDefault(listOf())

        gotResponse = view.getBooleanOr(GOT_RESPONSE_KEY, false)
    }
}