package de.fuballer.mcendgame.component.dungeon.generation.layout_generator.linear

import de.fuballer.mcendgame.component.dungeon.generation.data.SpawnLocation

data class TileLocations(
    val doors: List<Door>,
    val spawnLocations: List<SpawnLocation>
)