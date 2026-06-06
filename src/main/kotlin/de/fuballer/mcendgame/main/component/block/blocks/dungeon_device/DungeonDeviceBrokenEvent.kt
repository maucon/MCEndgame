package de.fuballer.mcendgame.main.component.block.blocks.dungeon_device

import net.minecraft.world.level.block.entity.BlockEntity

/**
 * only server-side
 */
data class DungeonDeviceBrokenEvent(
    val blockEntity: BlockEntity
)
