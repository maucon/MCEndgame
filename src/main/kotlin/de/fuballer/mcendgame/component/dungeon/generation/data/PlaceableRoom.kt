package de.fuballer.mcendgame.component.dungeon.generation.data

import org.bukkit.util.Vector

data class PlaceableRoom(
    var name: String,
    var position: Vector,
    var rotation: Double,
)