package de.fuballer.mcendgame.component.dungeon.generation.layout_generator.linear

import org.bukkit.util.Vector

data class RoomType(
    val name: String,
    val size: Vector, // 8 blocks (inclusive) -> 7 size
    val complexity: Int,
    val doors: List<Door>,
    val spawnLocations: List<Vector>
)