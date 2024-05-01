package de.fuballer.mcendgame.component.dungeon.generation.data

import org.bukkit.util.Vector

data class SpawnLocation(
    val location: Vector,
    val type: SpawnLocationType
)

enum class SpawnLocationType {
    NORMAL,
    SPECIAL,
    BOSS
}