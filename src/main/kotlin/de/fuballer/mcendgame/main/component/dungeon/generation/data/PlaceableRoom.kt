package de.fuballer.mcendgame.main.component.dungeon.generation.data

import net.minecraft.core.Vec3i

data class PlaceableRoom(
    val type: RoomType,
    val position: Vec3i,
    val rotation90: Int,
    val extraBlocks: List<PlaceableBlock> = listOf()
)
