package de.fuballer.mcendgame.main.component.entity.custom.entities.mount

import de.fuballer.mcendgame.main.component.entity.custom.entities.arachne.ArachneEntity
import de.fuballer.mcendgame.main.component.entity.custom.interfaces.CustomPosesEntity
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.entity.AnimationState
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import kotlin.math.PI
import kotlin.math.abs

abstract class DirectionalMovementEntity(
    type: EntityType<out ArachneEntity>,
    world: Level,
) : PathfinderMob(type, world) {
    val idleAnimationState = AnimationState()
    val walkAnimationState = AnimationState()
    val walkBWAnimationState = AnimationState()
    val walkLeftAnimationState = AnimationState()
    val walkRightAnimationState = AnimationState()

    companion object {
        val MOVEMENT_POSE: EntityDataAccessor<CustomPosesEntity.CustomPose> =
            SynchedEntityData.defineId(DirectionalMovementEntity::class.java, CustomPosesEntity.CUSTOM_POSE_TDH)
        val ANIMATION_MOVEMENT_SPEED: EntityDataAccessor<Float> =
            SynchedEntityData.defineId(DirectionalMovementEntity::class.java, EntityDataSerializers.FLOAT)
    }

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        super.defineSynchedData(builder)
        builder.define(MOVEMENT_POSE, CustomPosesEntity.CustomPose.IDLING)
        builder.define(ANIMATION_MOVEMENT_SPEED, 0F)
    }

    override fun onSyncedDataUpdated(data: EntityDataAccessor<*>) {
        if (data == MOVEMENT_POSE && level().isClientSide) {
            when (entityData.get(MOVEMENT_POSE)) {
                CustomPosesEntity.CustomPose.IDLING -> startMovementAnimation(idleAnimationState)
                CustomPosesEntity.CustomPose.WALKING -> startMovementAnimation(walkAnimationState)
                CustomPosesEntity.CustomPose.WALKING_BW -> startMovementAnimation(walkBWAnimationState)
                CustomPosesEntity.CustomPose.WALKING_LEFT -> startMovementAnimation(walkLeftAnimationState)
                CustomPosesEntity.CustomPose.WALKING_RIGHT -> startMovementAnimation(walkRightAnimationState)

                else -> {}
            }
        }
        super.onSyncedDataUpdated(data)
    }

    private fun stopMovementAnimations() {
        idleAnimationState.stop()
        walkAnimationState.stop()
        walkBWAnimationState.stop()
        walkLeftAnimationState.stop()
        walkRightAnimationState.stop()
    }

    open fun startMovementAnimation(animationState: AnimationState) {
        if (animationState.isStarted) return
        stopMovementAnimations()
        animationState.start(tickCount)
    }

    override fun tick() {
        super.tick()
        updateMovementState()
    }

    open fun updateMovementState() {
        if (level().isClientSide) return

        updateMovementPose()
        updateAnimationMovementSpeed()
    }

    open fun updateMovementPose() {
        val currentPose = entityData.get(MOVEMENT_POSE)
        val newPose = when (getRelativeMovementDirection()) {
            MovementDirection.NONE -> CustomPosesEntity.CustomPose.IDLING
            MovementDirection.FORWARD -> CustomPosesEntity.CustomPose.WALKING
            MovementDirection.BACKWARD -> CustomPosesEntity.CustomPose.WALKING_BW
            MovementDirection.LEFT -> CustomPosesEntity.CustomPose.WALKING_LEFT
            MovementDirection.RIGHT -> CustomPosesEntity.CustomPose.WALKING_RIGHT
        }
        if (currentPose == newPose) return

        entityData.set(MOVEMENT_POSE, newPose)
    }

    private fun updateAnimationMovementSpeed() {
        val currentMovementSpeed = entityData.get(ANIMATION_MOVEMENT_SPEED)
        val newMovementSpeed = speed
        if (abs(currentMovementSpeed - newMovementSpeed) < 0.01) return
        entityData.set(ANIMATION_MOVEMENT_SPEED, newMovementSpeed)
    }

    private fun getRelativeMovement(): Vec3 = knownMovement.yRot(yBodyRot / 180F * PI.toFloat())

    private fun getRelativeMovementDirection(): MovementDirection {
        val relativeMovement = getRelativeMovement()
        if (abs(relativeMovement.x) + abs(relativeMovement.z) < 0.05) return MovementDirection.NONE

        if (relativeMovement.z < -0.05) return MovementDirection.BACKWARD
        if (relativeMovement.z > 0.05) return MovementDirection.FORWARD
        if (relativeMovement.x > 0.05) return MovementDirection.LEFT
        if (relativeMovement.x < -0.05) return MovementDirection.RIGHT

        return MovementDirection.FORWARD
    }
}