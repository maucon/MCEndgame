package de.fuballer.mcendgame.main.component.dungeon.world

import net.minecraft.server.level.ServerLevel

/**
 * only server-side
 */
data class DungeonWorldClosedEvent(
    val dungeonWorld: ServerLevel,
)