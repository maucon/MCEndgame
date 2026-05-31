package de.fuballer.mcendgame.main.component.entity.custom.entities.training_dummy

import de.fuballer.mcendgame.main.component.killer.KillerScreenHandler
import de.fuballer.mcendgame.main.component.killer.KillerScreenHandlerFactory
import de.fuballer.mcendgame.main.component.killer.db.KillerEntity
import de.fuballer.mcendgame.main.component.killer.networking.KillerEntityPayload
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import kotlin.math.max

class TrainingDummyEntity(
    type: EntityType<out TrainingDummyEntity>,
    world: World,
) : MobEntity(type, world) {
    companion object {
        private const val DAMAGE_TIME_OUT = 100

        fun createAttributes(): DefaultAttributeContainer.Builder {
            return createLivingAttributes()
                .add(EntityAttributes.FOLLOW_RANGE, 10.0)
                .add(EntityAttributes.MOVEMENT_SPEED, 0.0)
                .add(EntityAttributes.ARMOR, 0.0)
                .add(EntityAttributes.KNOCKBACK_RESISTANCE, 1.0)
        }

        val LAST_DAMAGE: TrackedData<Float> = DataTracker.registerData(TrainingDummyEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
        val HIGHEST_DAMAGE: TrackedData<Float> = DataTracker.registerData(TrainingDummyEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
        val DAMAGE_SUM: TrackedData<Float> = DataTracker.registerData(TrainingDummyEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
        val DAMAGE_PER_SECOND: TrackedData<Float> = DataTracker.registerData(TrainingDummyEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
        val DAMAGE_DURATION: TrackedData<Int> = DataTracker.registerData(TrainingDummyEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
        val DAMAGE_ACTIVE: TrackedData<Boolean> = DataTracker.registerData(TrainingDummyEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)
    }

    override fun initDataTracker(builder: DataTracker.Builder) {
        super.initDataTracker(builder)

        builder.add(LAST_DAMAGE, 0F)
        builder.add(HIGHEST_DAMAGE, 0F)
        builder.add(DAMAGE_SUM, 0F)
        builder.add(DAMAGE_PER_SECOND, 0F)
        builder.add(DAMAGE_DURATION, 0)
        builder.add(DAMAGE_ACTIVE, false)
    }

    private var damageStartAge = -1
    private var lastDamageAge = -1

    override fun tick() {
        super.tick()
        if (entityWorld is ServerWorld) tickDps()
    }

    private fun tickDps() {
        if (!dataTracker.get(DAMAGE_ACTIVE)) return
        if (age - lastDamageAge > DAMAGE_TIME_OUT) dataTracker.set(DAMAGE_ACTIVE, false)

        var duration = max(age - damageStartAge, 1)
        if (!dataTracker.get(DAMAGE_ACTIVE)) duration -= DAMAGE_TIME_OUT
        dataTracker.set(DAMAGE_DURATION, duration)

        dataTracker.set(DAMAGE_PER_SECOND, dataTracker.get(DAMAGE_SUM) / (duration / 20f))
    }

    override fun setHealth(health: Float) {
        val damage = this.health - health
        if (damage > 1000000) { // high number to keep /kill working
            super.setHealth(health)
            return
        }
        if (damage <= 0) return

        if (!dataTracker.get(DAMAGE_ACTIVE)) {
            dataTracker.set(DAMAGE_ACTIVE, true)
            damageStartAge = age
            dataTracker.set(HIGHEST_DAMAGE, 0f)
            dataTracker.set(DAMAGE_SUM, 0f)
            dataTracker.set(DAMAGE_DURATION, 0)
            dataTracker.set(DAMAGE_PER_SECOND, 0f)
        }
        lastDamageAge = age

        updateDamage(damage)
    }

    private fun updateDamage(damage: Float) {
        dataTracker.set(LAST_DAMAGE, damage)
        if (damage > dataTracker.get(HIGHEST_DAMAGE)) dataTracker.set(HIGHEST_DAMAGE, damage)

        dataTracker.set(DAMAGE_SUM, dataTracker.get(DAMAGE_SUM) + damage)
    }

    override fun isPushable() = false

    override fun pushAway(entity: Entity) {}

    override fun takeKnockback(strength: Double, x: Double, z: Double) {}

    override fun setVelocity(velocity: Vec3d) {}

    override fun isPushedByFluids() = false

    override fun interact(player: PlayerEntity, hand: Hand): ActionResult {
        if (hand != Hand.MAIN_HAND) return super.interact(player, hand)
        if (entityWorld !is ServerWorld) return super.interact(player, hand)

        val stack = player.getStackInHand(hand)
        if (!stack.isEmpty) return super.interact(player, hand)

        val killerEntity = KillerEntity.of(player, this)
        val killerEntityPayload = KillerEntityPayload(killerEntity)

        val screenHandlerFactory = KillerScreenHandlerFactory(killerEntityPayload, displayName ?: type.name)
        { syncId, playerInventory, _ -> KillerScreenHandler(syncId, playerInventory, killerEntityPayload) }

        player.openHandledScreen(screenHandlerFactory)

        return ActionResult.SUCCESS
    }
}