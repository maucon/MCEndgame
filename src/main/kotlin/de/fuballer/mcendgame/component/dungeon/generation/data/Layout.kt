package de.fuballer.mcendgame.component.dungeon.generation.data

import org.bukkit.util.Vector

data class Layout(
    val startLocation: Vector,
    val tiles: List<PlaceableTile>,
    val spawnLocations: List<SpawnLocation>,
    val bossSpawnLocations: List<SpawnLocation>,
)