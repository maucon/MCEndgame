package de.fuballer.mcendgame.component.dungeon.generation.data

import org.bukkit.util.Vector

data class Layout(
    val tiles: List<PlaceableTile>,
    val spawnLocations: List<Vector> // TODO replace for more control (e.g. what mob type)
)

data class PlaceableTile(
    val schematicName: String,
    val position: Vector,
    val rotation: Double
)