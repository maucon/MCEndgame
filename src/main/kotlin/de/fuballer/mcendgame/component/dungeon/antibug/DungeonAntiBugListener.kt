package de.fuballer.mcendgame.component.dungeon.antibug

import de.fuballer.mcendgame.framework.stereotype.EventListener
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemConsumeEvent

class DungeonAntiBugListener(
    private val dungeonAntiBugService: DungeonAntiBugService
) : EventListener {
    @EventHandler
    fun onPlayerItemConsume(event: PlayerItemConsumeEvent) = dungeonAntiBugService.onPlayerItemConsume(event)

    @EventHandler
    fun onProjectileLaunch(event: ProjectileLaunchEvent) = dungeonAntiBugService.onProjectileLaunch(event)

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) = dungeonAntiBugService.onPlayerInteract(event)
}
