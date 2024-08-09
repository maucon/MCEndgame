package de.fuballer.mcendgame.component.dungeon.tweaks.block_interaction

import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.util.extension.EventExtension.cancel
import de.fuballer.mcendgame.util.extension.WorldExtension.isDungeonWorld
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent

@Service
class BlockInteractionService : Listener {
    @EventHandler(ignoreCancelled = true)
    fun on(event: BlockBreakEvent) {
        if (!event.player.world.isDungeonWorld()) return
        if (event.player.gameMode == GameMode.CREATIVE) return

        val isBlockBreakable = BlockInteractionSettings.BREAKABLE_BLOCKS.contains(event.block.type)
        if (!isBlockBreakable) {
            event.cancel()
            return
        }

        event.isDropItems = false
    }

    @EventHandler(ignoreCancelled = true)
    fun on(event: BlockPlaceEvent) {
        if (!event.player.world.isDungeonWorld()) return
        if (event.player.gameMode == GameMode.CREATIVE) return

        event.cancel()
    }
}
