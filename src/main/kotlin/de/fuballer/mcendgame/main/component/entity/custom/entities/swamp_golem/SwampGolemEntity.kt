package de.fuballer.mcendgame.main.component.entity.custom.entities.swamp_golem

import de.fuballer.mcendgame.main.component.entity.custom.goals.SlamAttackGoal
import de.fuballer.mcendgame.main.component.entity.custom.interfaces.CustomPosesEntity
import de.fuballer.mcendgame.main.component.entity.custom.interfaces.SlamAttacker
import net.minecraft.entity.AnimationState
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.entity.passive.ChickenEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.world.World

class SwampGolemEntity(
    type: EntityType<out SwampGolemEntity>,
    world: World,
) : HostileEntity(type, world), SlamAttacker {
    val slamAnimationState = AnimationState()
    val idleAnimationState = AnimationState()
    val walkAnimationState = AnimationState()

    companion object {
        val CUSTOM_POSE = DataTracker.registerData(SwampGolemEntity::class.java, CustomPosesEntity.CUSTOM_POSE_TDH)

        fun createAttributes(): DefaultAttributeContainer.Builder {
            return createHostileAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 35.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.20)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0)
                .add(EntityAttributes.GENERIC_ARMOR, 5.0)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.5)
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

    override fun initGoals() {
        goalSelector.add(0, SwimGoal(this))
        goalSelector.add(2, SlamAttackGoal(this, 1.0, 25, 17, 50))
        goalSelector.add(7, WanderAroundFarGoal(this, 1.0))
        goalSelector.add(8, LookAtEntityGoal(this, PlayerEntity::class.java, 8.0f))
        goalSelector.add(8, LookAroundGoal(this))

        targetSelector.add(1, ActiveTargetGoal(this, PlayerEntity::class.java, true))
        targetSelector.add(1, ActiveTargetGoal(this, ChickenEntity::class.java, true))
    }

    override fun tick() {
        super.tick()
        tickWalking()
    }

    private fun tickWalking() {
        if (entityWorld.isClient) return
        if (navigation.isFollowingPath) {
            if (dataTracker.get(CUSTOM_POSE) != CustomPosesEntity.CustomPose.IDLING) return
            dataTracker.set(CUSTOM_POSE, CustomPosesEntity.CustomPose.WALKING)
        } else {
            if (dataTracker.get(CUSTOM_POSE) != CustomPosesEntity.CustomPose.WALKING) return
            dataTracker.set(CUSTOM_POSE, CustomPosesEntity.CustomPose.IDLING)
        }
    }

    override fun initDataTracker(builder: DataTracker.Builder) {
        super.initDataTracker(builder)
        builder.add(CUSTOM_POSE, CustomPosesEntity.CustomPose.IDLING)
    }

    override fun onTrackedDataSet(data: TrackedData<*>) {
        if (data == CUSTOM_POSE) {
            when (dataTracker.get(CUSTOM_POSE)) {
                CustomPosesEntity.CustomPose.SLAMMING -> {
                    walkAnimationState.stop()
                    idleAnimationState.stop()
                    slamAnimationState.start(age)
                }

                CustomPosesEntity.CustomPose.IDLING -> {
                    walkAnimationState.stop()
                    idleAnimationState.start(age)
                }

                CustomPosesEntity.CustomPose.WALKING -> {
                    idleAnimationState.stop()
                    walkAnimationState.start(age)
                }

                else -> {}
            }
        }
        super.onTrackedDataSet(data)
    }

    override fun setRotation(yaw: Float, pitch: Float) {
        if (isRotationLocked()) return
        super.setRotation(yaw, pitch)
    }

    override fun setYaw(yaw: Float) {
        if (isRotationLocked()) return
        super.setYaw(yaw)
    }

    override fun setPose(pose: CustomPosesEntity.CustomPose) {
        dataTracker.set(CUSTOM_POSE, pose)
    }

    private fun isRotationLocked() = dataTracker.get(CUSTOM_POSE) == CustomPosesEntity.CustomPose.SLAMMING
}