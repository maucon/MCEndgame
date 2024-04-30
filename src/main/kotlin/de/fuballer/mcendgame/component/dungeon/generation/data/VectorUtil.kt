package de.fuballer.mcendgame.component.dungeon.generation.data

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.util.Vector
import kotlin.math.floor

object VectorUtil {
    //use only for values near an int
    fun getRoundedVector(vector: Vector) = Vector(floor(vector.x + 0.1), floor(vector.y + 0.1), floor(vector.z + 0.1))

    fun toLocation(world: World, vector: Vector) =
        Location(world, vector.x, vector.y, vector.z)
}