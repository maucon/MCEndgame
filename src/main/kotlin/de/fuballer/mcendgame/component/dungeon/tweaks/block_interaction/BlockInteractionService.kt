package de.fuballer.mcendgame.component.dungeon.tweaks.block_interaction

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent

@Component
class BlockInteractionService : Listener {
    @EventHandler
    fun on(event: BlockBreakEvent) {
        if (WorldUtil.isNotDungeonWorld(event.player.world)) return
        if (event.player.gameMode == GameMode.CREATIVE) return

        if (BlockInteractionSettings.BREAKABLE_BLOCKS.contains(event.block.type)) {
            event.isDropItems = false
            return
        }

        event.isCancelled = true
    }

    @EventHandler
    fun on(event: BlockPlaceEvent) {
        if (WorldUtil.isNotDungeonWorld(event.player.world)) return
        if (event.player.gameMode == GameMode.CREATIVE) return

        event.isCancelled = true
    }
}
