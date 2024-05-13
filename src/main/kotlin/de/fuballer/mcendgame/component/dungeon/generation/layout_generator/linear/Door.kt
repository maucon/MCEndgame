package de.fuballer.mcendgame.component.dungeon.generation.layout_generator.linear

import de.fuballer.mcendgame.util.VectorUtil
import org.bukkit.util.Vector

data class Door(
    var position: Vector,
    var direction: Vector
) {
    fun getAdjacentPosition() = position.clone().add(direction)

    fun getDirectionInDegree(): Double {
        if (direction.x == 1.0) return 270.0
        if (direction.x == -1.0) return 90.0
        if (direction.z == 1.0) return 0.0
        return 180.0
    }

    fun getRotated(rad: Double): Door {
        val newPosition = VectorUtil.getRoundedVector(position.clone().rotateAroundY(rad))
        val newRotation = VectorUtil.getRoundedVector(direction.clone().rotateAroundY(rad))
        return Door(newPosition, newRotation)
    }
}