package de.fuballer.mcendgame.component.dungeon.generation

import org.bukkit.util.Vector
import kotlin.math.floor

object VectorUtil {
    //use only for values near an int
    fun getRoundedVector(vector: Vector) = Vector(floor(vector.x + 0.1), floor(vector.y + 0.1), floor(vector.z + 0.1))
}