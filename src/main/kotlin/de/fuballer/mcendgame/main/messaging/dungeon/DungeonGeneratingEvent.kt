package de.fuballer.mcendgame.main.messaging.dungeon

import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItem
import net.minecraft.world.entity.player.Player

/**
 * should not be used in a blocking way
 */
data class DungeonGeneratingEvent(
    val player: Player,
    val affectingAspects: Map<AspectItem, Int>
)