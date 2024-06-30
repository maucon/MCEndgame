package de.fuballer.mcendgame.util

import com.sk89q.worldedit.math.BlockVector3
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.util.Vector
import kotlin.math.floor

object VectorUtil {
    /** use only for values near an int */
    fun getRoundedVector(vector: Vector) = Vector(floor(vector.x + 0.1), floor(vector.y + 0.1), floor(vector.z + 0.1))

    fun toLocation(world: World, vector: Vector) =
        Location(world, vector.x, vector.y, vector.z)

    fun toLocation(world: World, vector: Vector, rotation: Double) =
        Location(world, vector.x, vector.y, vector.z, rotation.toFloat(), 0F)

    fun fromBlockVector3(blockVector: BlockVector3) =
        Vector(blockVector.x(), blockVector.y(), blockVector.z())

    fun toBlockVector3(vector: Vector): BlockVector3 =
        BlockVector3.at(vector.x, vector.y, vector.z)
}