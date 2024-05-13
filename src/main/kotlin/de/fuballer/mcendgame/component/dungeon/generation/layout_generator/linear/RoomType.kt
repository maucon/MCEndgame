package de.fuballer.mcendgame.component.dungeon.generation.layout_generator.linear

import de.fuballer.mcendgame.component.dungeon.generation.data.SpawnLocation
import org.bukkit.util.Vector

data class RoomType(
    val schematicData: ByteArray,
    val size: Vector, // 8 blocks (inclusive) -> 7 size
    val startLocation: SpawnLocation?,
    val doors: List<Door>,
    val spawnLocations: List<SpawnLocation>,
    val bossSpawnLocations: List<SpawnLocation>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RoomType

        return schematicData.contentEquals(other.schematicData)
    }

    override fun hashCode(): Int {
        return schematicData.contentHashCode()
    }

    fun isLinear() = doors.size == 2
    fun getComplexity() = spawnLocations.size + 1
}