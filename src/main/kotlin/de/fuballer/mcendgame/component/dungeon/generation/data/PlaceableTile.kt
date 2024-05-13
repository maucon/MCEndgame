package de.fuballer.mcendgame.component.dungeon.generation.data

import org.bukkit.util.Vector

data class PlaceableTile(
    val tileData: ByteArray,
    val position: Vector,
    val rotation: Double,
    val extraBlocks: List<PlaceableBlock> = listOf()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PlaceableTile

        if (!tileData.contentEquals(other.tileData)) return false
        if (position != other.position) return false
        if (rotation != other.rotation) return false

        return true
    }

    override fun hashCode(): Int {
        var result = tileData.contentHashCode()
        result = 31 * result + position.hashCode()
        result = 31 * result + rotation.hashCode()
        return result
    }
}