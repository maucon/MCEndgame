package de.fuballer.mcendgame.main.component.entity.custom.interfaces

import net.minecraft.world.entity.Mob

interface BlockAbleMovementMob<T> where T : Mob, T : DisableAbleGoalsMob {
    var blockAbleMovementEntity: T
    var blockedMovementTicks: Int
    var blockedMovementAirborne: Boolean

    fun tickBlockedMovement() {
        updateAirborneBlocked()

        if (isMovementBlocked()) blockAbleMovementEntity.moveControl.strafe(0F, 0F)

        if (blockedMovementTicks <= 0) return
        if (--blockedMovementTicks > 0) return
        blockAbleMovementEntity.updateGoals()
    }

    fun updateAirborneBlocked() {
        if (!blockedMovementAirborne) return
        if (!blockAbleMovementEntity.onGround() && !blockAbleMovementEntity.isInLiquid) return
        blockedMovementAirborne = false
        blockAbleMovementEntity.updateGoals()
    }

    fun blockMovement(ticks: Int) {
        if (ticks <= 0) return
        val oldTicks = blockedMovementTicks
        blockedMovementTicks = ticks
        if (oldTicks > 0) return

        blockAbleMovementEntity.updateGoals()
        blockAbleMovementEntity.navigation.stop()
    }

    fun isMovementBlocked() = blockedMovementTicks > 0 || blockedMovementAirborne

    fun setAirborneBlocked() {
        blockedMovementAirborne = true
        blockAbleMovementEntity.updateGoals()
        blockAbleMovementEntity.navigation.stop()
    }
}