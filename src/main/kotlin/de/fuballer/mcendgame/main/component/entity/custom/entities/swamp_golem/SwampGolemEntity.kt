package de.fuballer.mcendgame.main.component.entity.custom.entities.swamp_golem

import de.fuballer.mcendgame.main.component.entity.custom.goals.SlamAttackGoal
import de.fuballer.mcendgame.main.component.entity.custom.interfaces.CustomPosesEntity
import de.fuballer.mcendgame.main.component.entity.custom.interfaces.SlamAttacker
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.entity.AnimationState
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.FloatGoal
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.animal.chicken.Chicken
import net.minecraft.world.entity.monster.Monster
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level

class SwampGolemEntity(
    type: EntityType<out SwampGolemEntity>,
    world: Level,
) : Monster(type, world), SlamAttacker {
    val slamAnimationState = AnimationState()
    val idleAnimationState = AnimationState()
    val walkAnimationState = AnimationState()

    companion object {
        val CUSTOM_POSE = SynchedEntityData.defineId(SwampGolemEntity::class.java, CustomPosesEntity.CUSTOM_POSE_TDH)

        fun createAttributes(): AttributeSupplier.Builder {
            return createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 35.0)
                .add(Attributes.MOVEMENT_SPEED, 0.20)
                .add(Attributes.ATTACK_DAMAGE, 4.0)
                .add(Attributes.ARMOR, 5.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5)
        }
    }

    //Slam Attacker properties
    override val slamAttacker = this
    override val slamRadius = 3.0
    override val minSlamStrength = 0.3
    override val slamCenterFacingOffset = 1.1
    override val applyScale = true
    override val knockbackStrength = 1.0
    override fun shouldDamage(target: LivingEntity) = target.isAlive

    override fun registerGoals() {
        goalSelector.addGoal(0, FloatGoal(this))
        goalSelector.addGoal(2, SlamAttackGoal(this, 1.0, 25, 17, 50))
        goalSelector.addGoal(7, WaterAvoidingRandomStrollGoal(this, 1.0))
        goalSelector.addGoal(8, LookAtPlayerGoal(this, Player::class.java, 8.0f))
        goalSelector.addGoal(8, RandomLookAroundGoal(this))

        targetSelector.addGoal(1, NearestAttackableTargetGoal(this, Player::class.java, true))
        targetSelector.addGoal(1, NearestAttackableTargetGoal(this, Chicken::class.java, true))
    }

    override fun tick() {
        super.tick()
        tickWalking()
    }

    private fun tickWalking() {
        if (level().isClientSide) return
        if (navigation.isInProgress) {
            if (entityData.get(CUSTOM_POSE) != CustomPosesEntity.CustomPose.IDLING) return
            entityData.set(CUSTOM_POSE, CustomPosesEntity.CustomPose.WALKING)
        } else {
            if (entityData.get(CUSTOM_POSE) != CustomPosesEntity.CustomPose.WALKING) return
            entityData.set(CUSTOM_POSE, CustomPosesEntity.CustomPose.IDLING)
        }
    }

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        super.defineSynchedData(builder)
        builder.define(CUSTOM_POSE, CustomPosesEntity.CustomPose.IDLING)
    }

    override fun onSyncedDataUpdated(data: EntityDataAccessor<*>) {
        if (data == CUSTOM_POSE) {
            when (entityData.get(CUSTOM_POSE)) {
                CustomPosesEntity.CustomPose.SLAMMING -> {
                    walkAnimationState.stop()
                    idleAnimationState.stop()
                    slamAnimationState.start(tickCount)
                }

                CustomPosesEntity.CustomPose.IDLING -> {
                    walkAnimationState.stop()
                    idleAnimationState.start(tickCount)
                }

                CustomPosesEntity.CustomPose.WALKING -> {
                    idleAnimationState.stop()
                    walkAnimationState.start(tickCount)
                }

                else -> {}
            }
        }
        super.onSyncedDataUpdated(data)
    }

    override fun forceSetRotation(yaw: Float, relativeYaw: Boolean, pitch: Float, relativePitch: Boolean) {
        if (isRotationLocked()) return
        super.forceSetRotation(yaw, relativeYaw, pitch, relativePitch)
    }

    override fun setYRot(yaw: Float) {
        if (isRotationLocked()) return
        super.setYRot(yaw)
    }

    override fun setPose(pose: CustomPosesEntity.CustomPose) {
        entityData.set(CUSTOM_POSE, pose)
    }

    private fun isRotationLocked() = entityData.get(CUSTOM_POSE) == CustomPosesEntity.CustomPose.SLAMMING
}