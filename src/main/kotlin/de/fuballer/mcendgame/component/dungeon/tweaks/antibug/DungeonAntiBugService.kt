package de.fuballer.mcendgame.component.dungeon.tweaks.antibug

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.WorldUtil
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
        if (WorldUtil.isNotDungeonWorld(event.player.world)) return
        if (event.item.type == Material.CHORUS_FRUIT) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun on(event: ProjectileLaunchEvent) {
        if (WorldUtil.isNotDungeonWorld(event.entity.world)) return
        if (event.entity.type == EntityType.ENDER_PEARL) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun on(event: PlayerInteractEvent) {
        if (WorldUtil.isNotDungeonWorld(event.player.world)) return

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
        if (WorldUtil.isNotDungeonWorld(event.rightClicked.world)) return
        if (event.rightClicked !is Animals) return

        event.isCancelled = true
    }
}