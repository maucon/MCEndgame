package de.fuballer.mcendgame.main.util

import net.minecraft.world.level.block.Rotation

object RotationUtil {
    fun getAsRotation(degree: Double) = when (degree) {
        90.0 -> Rotation.CLOCKWISE_90
        180.0 -> Rotation.CLOCKWISE_180
        270.0 -> Rotation.COUNTERCLOCKWISE_90
        else -> Rotation.NONE
    }
}