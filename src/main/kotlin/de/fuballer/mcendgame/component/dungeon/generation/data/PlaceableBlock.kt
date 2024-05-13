package de.fuballer.mcendgame.component.dungeon.generation.data

import com.sk89q.worldedit.world.block.BlockType

data class PlaceableBlock(
    val x: Int,
    val y: Int,
    val z: Int,
    val rotation: Double,
    val type: BlockType
)