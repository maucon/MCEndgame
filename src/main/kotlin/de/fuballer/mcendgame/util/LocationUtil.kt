package de.fuballer.mcendgame.util

import org.bukkit.Location
import org.bukkit.util.BoundingBox
import kotlin.math.PI

object LocationUtil {
    fun getLocationRotatedAroundLocationsYAxis(location: Location, centerLocation: Location, degree: Double): Location {
        val radians = degree / 180.0 * PI

        val offsetFromCenter = location.toVector().subtract(centerLocation.toVector())
        val rotatedOffsetFromCenter = offsetFromCenter.clone().rotateAroundY(radians)

        return Location(
            centerLocation.world,
            centerLocation.x + rotatedOffsetFromCenter.x,
            centerLocation.y + rotatedOffsetFromCenter.y,
            centerLocation.z + rotatedOffsetFromCenter.z
        )
    }

    fun isInsideUnpassableBlock(location: Location, boundingBox: BoundingBox): Boolean {
        val casterHeight = boundingBox.height
        val casterHalfWidthX = boundingBox.widthX / 2
        val casterHalfWidthZ = boundingBox.widthZ / 2

        for (y in 0..1) {
            for (xz in 0..3) {
                val xOff = if (xz <= 1) casterHalfWidthX else -casterHalfWidthX
                val yOff = if (y == 0) 0.0 else casterHeight
                val zOff = if (xz % 2 == 0) casterHalfWidthZ else -casterHalfWidthZ

                val loc = Location(location.world, location.x + xOff, location.y + yOff, location.z + zOff)
                if (isInsideUnpassableBlock(loc)) return true
            }
        }

        return false
    }

    private fun isInsideUnpassableBlock(location: Location): Boolean {
        val world = location.world ?: return true
        val block = world.getBlockAt(location)
        return !block.isPassable
    }
}