package de.fuballer.mcendgame.component.dungeon.antibug

import de.fuballer.mcendgame.framework.stereotype.Service
import de.fuballer.mcendgame.helper.WorldHelper
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.event.block.Action
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemConsumeEvent

class DungeonAntiBugService : Service {
    fun onPlayerItemConsume(event: PlayerItemConsumeEvent) {
        if (WorldHelper.isNotDungeonWorld(event.player.world)) return
        if (event.item.type == Material.CHORUS_FRUIT)
            event.isCancelled = true
    }

    fun onProjectileLaunch(event: ProjectileLaunchEvent) {
        if (WorldHelper.isNotDungeonWorld(event.entity.world)) return
        if (event.entity.type == EntityType.ENDER_PEARL)
            event.isCancelled = true
    }

    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (WorldHelper.isNotDungeonWorld(event.player.world)) return

        if (event.action != Action.RIGHT_CLICK_BLOCK) return

        val mainHandMaterial = event.player.inventory.itemInMainHand.type
        val offHandMaterial = event.player.inventory.itemInOffHand.type
        if (!DungeonAntiBugSettings.BOATS.contains(mainHandMaterial)
            && !DungeonAntiBugSettings.BOATS.contains(offHandMaterial)
        ) return

        event.isCancelled = true
    }
}
