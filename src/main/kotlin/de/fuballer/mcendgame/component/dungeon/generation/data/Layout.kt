package de.fuballer.mcendgame.component.dungeon.generation.data

data class Layout(
    val startLocation: SpawnLocation,
    val tiles: List<PlaceableTile>,
    val enemySpawnLocations: List<SpawnLocation>,
    val bossSpawnLocations: List<SpawnLocation>
)