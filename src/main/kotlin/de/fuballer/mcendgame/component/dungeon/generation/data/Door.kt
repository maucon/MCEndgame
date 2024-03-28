package de.fuballer.mcendgame.component.dungeon.generation.data

import de.fuballer.mcendgame.component.dungeon.generation.VectorUtil
import org.bukkit.util.Vector

data class Door(
    var position: Vector,
    var direction: Vector
) {
    fun getAdjacentPosition() = position.clone().add(direction)

    fun getRotated(rad: Double): Door {
        val newPosition = VectorUtil.getRoundedVector(position.clone().rotateAroundY(rad))
        val newRotation = VectorUtil.getRoundedVector(direction.clone().rotateAroundY(rad))
        return Door(newPosition, newRotation)
    }
}