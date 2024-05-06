package de.fuballer.mcendgame.component.dungeon.generation.data

data class Layout(
    val tiles: List<PlaceableTile>,
    val spawnLocations: List<SpawnLocation>,
    val bossSpawnLocations: List<SpawnLocation>,
)