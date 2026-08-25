package de.fuballer.mcendgame.main.component.entity.custom.entities.training_dummy

import de.fuballer.mcendgame.main.component.killer.KillerScreenHandler
import de.fuballer.mcendgame.main.component.killer.KillerScreenHandlerFactory
import de.fuballer.mcendgame.main.component.killer.db.KillerEntity
import de.fuballer.mcendgame.main.component.killer.networking.KillerEntityPayload
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import kotlin.math.max

class TrainingDummyEntity(
    type: EntityType<out TrainingDummyEntity>,
    world: Level,
) : Mob(type, world) {
    companion object {
        private const val DAMAGE_TIME_OUT = 100

        fun createAttributes(): AttributeSupplier.Builder {
            return createLivingAttributes()
                .add(Attributes.FOLLOW_RANGE, 10.0)
                .add(Attributes.MOVEMENT_SPEED, 0.0)
                .add(Attributes.ARMOR, 0.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0)
        }

        val LAST_DAMAGE: EntityDataAccessor<Float> = SynchedEntityData.defineId(TrainingDummyEntity::class.java, EntityDataSerializers.FLOAT)
        val HIGHEST_DAMAGE: EntityDataAccessor<Float> = SynchedEntityData.defineId(TrainingDummyEntity::class.java, EntityDataSerializers.FLOAT)
        val DAMAGE_SUM: EntityDataAccessor<Float> = SynchedEntityData.defineId(TrainingDummyEntity::class.java, EntityDataSerializers.FLOAT)
        val DAMAGE_PER_SECOND: EntityDataAccessor<Float> = SynchedEntityData.defineId(TrainingDummyEntity::class.java, EntityDataSerializers.FLOAT)
        val DAMAGE_DURATION: EntityDataAccessor<Int> = SynchedEntityData.defineId(TrainingDummyEntity::class.java, EntityDataSerializers.INT)
        val DAMAGE_ACTIVE: EntityDataAccessor<Boolean> = SynchedEntityData.defineId(TrainingDummyEntity::class.java, EntityDataSerializers.BOOLEAN)
    }

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        super.defineSynchedData(builder)

        builder.define(LAST_DAMAGE, 0F)
        builder.define(HIGHEST_DAMAGE, 0F)
        builder.define(DAMAGE_SUM, 0F)
        builder.define(DAMAGE_PER_SECOND, 0F)
        builder.define(DAMAGE_DURATION, 0)
        builder.define(DAMAGE_ACTIVE, false)
    }

    private var damageStartAge = -1
    private var lastDamageAge = -1

    override fun tick() {
        super.tick()
        if (level() is ServerLevel) tickDps()
    }

    private fun tickDps() {
        if (!entityData.get(DAMAGE_ACTIVE)) return
        if (tickCount - lastDamageAge > DAMAGE_TIME_OUT) entityData.set(DAMAGE_ACTIVE, false)

        var duration = max(tickCount - damageStartAge, 1)
        if (!entityData.get(DAMAGE_ACTIVE)) duration -= DAMAGE_TIME_OUT
        entityData.set(DAMAGE_DURATION, duration)

        entityData.set(DAMAGE_PER_SECOND, entityData.get(DAMAGE_SUM) / (duration / 20f))
    }

    override fun setHealth(health: Float) {
        val damage = this.health - health
        if (damage > 1000000) { // high number to keep /kill working
            super.setHealth(health)
            return
        }
        if (damage <= 0) return

        if (!entityData.get(DAMAGE_ACTIVE)) {
            entityData.set(DAMAGE_ACTIVE, true)
            damageStartAge = tickCount
            entityData.set(HIGHEST_DAMAGE, 0f)
            entityData.set(DAMAGE_SUM, 0f)
            entityData.set(DAMAGE_DURATION, 0)
            entityData.set(DAMAGE_PER_SECOND, 0f)
        }
        lastDamageAge = tickCount

        updateDamage(damage)
    }

    private fun updateDamage(damage: Float) {
        entityData.set(LAST_DAMAGE, damage)
        if (damage > entityData.get(HIGHEST_DAMAGE)) entityData.set(HIGHEST_DAMAGE, damage)

        entityData.set(DAMAGE_SUM, entityData.get(DAMAGE_SUM) + damage)
    }

    override fun isPushable() = false

    override fun doPush(entity: Entity) {}

    override fun knockback(power: Double, xd: Double, zd: Double, source: DamageSource, damage: Float) {}

    override fun knockback(power: Double, xd: Double, zd: Double, source: DamageSource, damage: Float, comesFromEffect: Boolean) {}

    override fun setDeltaMovement(velocity: Vec3) {}

    override fun isPushedByFluid() = false

    override fun interact(
        player: Player,
        hand: InteractionHand,
        location: Vec3
    ): InteractionResult {
        if (hand != InteractionHand.MAIN_HAND) return super.interact(player, hand, location)
        if (level() !is ServerLevel) return super.interact(player, hand, location)

        val stack = player.getItemInHand(hand)
        if (!stack.isEmpty) return super.interact(player, hand, location)

        val killerEntity = KillerEntity.of(player, this)
        val killerEntityPayload = KillerEntityPayload(killerEntity)

        val screenHandlerFactory = KillerScreenHandlerFactory(killerEntityPayload, displayName)
        { syncId, _, _ -> KillerScreenHandler(syncId, killerEntityPayload) }

        player.openMenu(screenHandlerFactory)

        return InteractionResult.SUCCESS
    }
}