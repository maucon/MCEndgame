package de.fuballer.mcendgame.component.dungeon.tweaks.block_interaction

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.WorldExtension.isDungeonWorld
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent

@Component
class BlockInteractionService : Listener {
    @EventHandler(ignoreCancelled = true)
    fun on(event: BlockBreakEvent) {
        if (!event.player.world.isDungeonWorld()) return
        if (event.player.gameMode == GameMode.CREATIVE) return

        val isBlockNotBreakable = !BlockInteractionSettings.BREAKABLE_BLOCKS.contains(event.block.type)

        event.isCancelled = isBlockNotBreakable
        event.isDropItems = false
    }

    @EventHandler(ignoreCancelled = true)
    fun on(event: BlockPlaceEvent) {
        if (!event.player.world.isDungeonWorld()) return
        if (event.player.gameMode == GameMode.CREATIVE) return

        event.isCancelled = true
    }
}
