package de.fuballer.mcendgame.util

import org.bukkit.Location
import org.bukkit.util.Vector
import kotlin.math.atan2

object MathUtil {
    fun calculateYawToFacingLocation(location: Location, facing: Vector): Float {
        val dx = facing.x - location.x
        val dz = facing.z - location.z

        var yaw = atan2(dz, dx) * (180 / Math.PI)
        yaw = (yaw + 270) % 360
        return yaw.toFloat()
    }
}