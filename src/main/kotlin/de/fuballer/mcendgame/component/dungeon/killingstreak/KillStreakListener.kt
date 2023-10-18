package de.fuballer.mcendgame.component.dungeon.killingstreak

import de.fuballer.mcendgame.event.DungeonOpenEvent
import de.fuballer.mcendgame.event.DungeonWorldDeleteEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.framework.stereotype.EventListener
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

@Component
class KillStreakListener(
    private val killStreakService: KillStreakService
) : EventListener {
    @EventHandler
    fun onEntityDeath(event: EntityDeathEvent) = killStreakService.onEntityDeath(event)

    @EventHandler
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) = killStreakService.onEntityDamageByEntity(event)

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) = killStreakService.onPlayerQuit(event)

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) = killStreakService.onPlayerJoin(event)

    @EventHandler
    fun onPlayerChangedWorldEvent(event: PlayerChangedWorldEvent) = killStreakService.onPlayerChangedWorld(event)

    @EventHandler
    fun onDungeonWorldDelete(event: DungeonWorldDeleteEvent) = killStreakService.onDungeonWorldDelete(event)

    @EventHandler
    fun onDungeonOpen(event: DungeonOpenEvent) = killStreakService.onDungeonOpen(event)
}
