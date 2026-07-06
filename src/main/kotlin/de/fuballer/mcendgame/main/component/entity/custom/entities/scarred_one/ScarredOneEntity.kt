package de.fuballer.mcendgame.main.component.entity.custom.entities.scarred_one

import de.fuballer.mcendgame.main.component.dungeon.generation.encounter.encounters.scarred_one.data.RolledScarredOneEffect
import de.fuballer.mcendgame.main.component.dungeon.generation.encounter.encounters.scarred_one.messaging.ScarredOneInteractEvent
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.addCustomAttribute
import de.maucon.mauconframework.event.EventGateway
import net.minecraft.entity.EntityType
import net.minecraft.entity.ai.goal.LookAtEntityGoal
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.storage.ReadView
import net.minecraft.storage.WriteView
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.world.World
import software.bernie.geckolib.animatable.GeoEntity
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
import software.bernie.geckolib.animatable.manager.AnimatableManager
import software.bernie.geckolib.util.GeckoLibUtil
import kotlin.jvm.optionals.getOrDefault
import kotlin.random.Random

private const val POSITIVE_EFFECTS_KEY = "positive_effects"
private const val NEGATIVE_EFFECTS_KEY = "negative_effects"
private const val GOT_RESPONSE_KEY = "got_response"

class ScarredOneEntity(
    type: EntityType<out ScarredOneEntity>,
    world: World,
) : MobEntity(type, world), GeoEntity {
    companion object {
        fun createAttributes(): DefaultAttributeContainer.Builder {
            return createLivingAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 15.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.0)
        }
    }

    var positiveEffects = listOf<RolledScarredOneEffect>()
    var negativeEffects = listOf<RolledScarredOneEffect>()

    var gotResponse = false

    fun respond(player: ServerPlayerEntity, accept: Boolean, world: ServerWorld) {
        if (gotResponse) return
        gotResponse = true

        if (accept) {
            positiveEffects.forEach { world.addCustomAttribute(it.attribute, it.targets.predicate) }
            negativeEffects.forEach { world.addCustomAttribute(it.attribute, it.targets.predicate) }
            playAcceptedSound(world)
        }

        EventGateway.publish(ScarredOneDespawnEvent(player, this, accept))
    }

    private fun playAcceptedSound(world: ServerWorld) {
        world.playSound(this, blockPos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.NEUTRAL, 1F, 0.95F + 0.1F * Random.nextFloat())
    }

    fun hasRolledEffects() = positiveEffects.isNotEmpty() || negativeEffects.isNotEmpty()

    override fun initGoals() {
        goalSelector.add(0, LookAtEntityGoal(this, PlayerEntity::class.java, 8F, 1F))
    }

    private val cache: AnimatableInstanceCache = GeckoLibUtil.createInstanceCache(this)
    override fun getAnimatableInstanceCache() = cache

    override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {
    }

    override fun interact(
        player: PlayerEntity,
        hand: Hand
    ): ActionResult {
        val world = entityWorld as? ServerWorld ?: return ActionResult.PASS
        val serverPlayer = player as? ServerPlayerEntity ?: return ActionResult.PASS

        val event = ScarredOneInteractEvent(this, serverPlayer, world)
        EventGateway.publish(event)

        return ActionResult.PASS
    }

    override fun isPushable() = false

    override fun writeCustomData(view: WriteView) {
        super.writeCustomData(view)

        view.put(POSITIVE_EFFECTS_KEY, RolledScarredOneEffect.LIST_CODEC, positiveEffects)
        view.put(NEGATIVE_EFFECTS_KEY, RolledScarredOneEffect.LIST_CODEC, negativeEffects)

        view.putBoolean(GOT_RESPONSE_KEY, gotResponse)
    }

    override fun readCustomData(view: ReadView) {
        super.readCustomData(view)

        positiveEffects = view.read(POSITIVE_EFFECTS_KEY, RolledScarredOneEffect.LIST_CODEC).getOrDefault(listOf())
        negativeEffects = view.read(NEGATIVE_EFFECTS_KEY, RolledScarredOneEffect.LIST_CODEC).getOrDefault(listOf())

        gotResponse = view.getBoolean(GOT_RESPONSE_KEY, false)
    }
}