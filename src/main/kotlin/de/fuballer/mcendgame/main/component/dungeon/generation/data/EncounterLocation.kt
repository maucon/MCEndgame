package de.fuballer.mcendgame.main.component.dungeon.generation.data

import net.minecraft.core.Vec3i
import kotlin.math.atan2
import kotlin.math.roundToInt

data class EncounterLocation(
    val location: Vec3i,
    val facingToLocation: Vec3i,
) {
    fun getRotation16(): Int {
        val dx = (facingToLocation.x - location.x).toDouble()
        val dz = (facingToLocation.z - location.z).toDouble()
        val angle = Math.toDegrees(atan2(dz, dx))
        val rotation = (angle / 22.5).roundToInt()
        return (rotation + 20) % 16
    }
}