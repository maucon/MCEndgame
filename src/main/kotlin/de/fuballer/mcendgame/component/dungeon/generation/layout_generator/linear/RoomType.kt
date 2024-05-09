package de.fuballer.mcendgame.component.dungeon.generation.layout_generator.linear

import de.fuballer.mcendgame.component.dungeon.generation.data.SpawnLocation
import org.bukkit.util.Vector

data class RoomType(
    val schematicData: ByteArray,
    val size: Vector, // 8 blocks (inclusive) -> 7 size
    val complexity: Int,
    val startLocation: Vector?,
    val doors: List<Door>,
    val spawnLocations: List<SpawnLocation>,
    val bossSpawnLocations: List<SpawnLocation>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RoomType

        if (!schematicData.contentEquals(other.schematicData)) return false
        if (complexity != other.complexity) return false

        return true
    }

    override fun hashCode(): Int {
        var result = schematicData.contentHashCode()
        result = 31 * result + complexity
        return result
    }

    fun isLinear() = doors.size == 2
}