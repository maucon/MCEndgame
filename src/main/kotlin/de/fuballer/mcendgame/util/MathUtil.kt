package de.fuballer.mcendgame.util

import org.bukkit.Location
import org.bukkit.entity.Entity
import kotlin.math.atan2

object MathUtil {
    fun calculateYawToFacingLocation(location: Location, facing: Location): Float {
        val dx = facing.x - location.x
        val dz = facing.z - location.z

        var yaw = atan2(dz, dx) * (180 / Math.PI)
        yaw = (yaw + 270) % 360
        return yaw.toFloat()
    }

    fun getFacingAngle(entity: Entity, target: Entity): Double {
        val facing = entity.location.direction
        val directionToTarget = target.location.toVector().subtract(entity.location.toVector())

        val angleRad = facing.angle(directionToTarget).toDouble()

        return Math.toDegrees(angleRad)
    }
}