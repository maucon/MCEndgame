package de.fuballer.mcendgame.component.dungeon.tweaks.antibug

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.WorldExtension.isDungeonWorld
import org.bukkit.Material
import org.bukkit.entity.Animals
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemConsumeEvent

@Component
class DungeonAntiBugService : Listener {
    @EventHandler
    fun on(event: PlayerItemConsumeEvent) {
        if (!event.player.world.isDungeonWorld()) return
        if (event.item.type == Material.CHORUS_FRUIT) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun on(event: ProjectileLaunchEvent) {
        if (!event.entity.world.isDungeonWorld()) return
        if (event.entity.type == EntityType.ENDER_PEARL) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun on(event: PlayerInteractEvent) {
        if (!event.player.world.isDungeonWorld()) return

        if (event.action != Action.RIGHT_CLICK_BLOCK) return

        val mainHandMaterial = event.player.inventory.itemInMainHand.type
        val offHandMaterial = event.player.inventory.itemInOffHand.type

        if (!DungeonAntiBugSettings.BOATS.contains(mainHandMaterial)
            && !DungeonAntiBugSettings.BOATS.contains(offHandMaterial)
        ) return

        event.isCancelled = true
    }

    @EventHandler
    fun on(event: PlayerInteractEntityEvent) {
        if (!event.rightClicked.world.isDungeonWorld()) return
        if (event.rightClicked !is Animals) return

        event.isCancelled = true
    }
}