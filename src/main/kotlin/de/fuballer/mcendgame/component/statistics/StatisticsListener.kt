package de.fuballer.mcendgame.component.statistics

import de.fuballer.mcendgame.event.DungeonCompleteEvent
import de.fuballer.mcendgame.event.DungeonOpenEvent
import de.fuballer.mcendgame.event.KillStreakIncreaseEvent
import de.fuballer.mcendgame.event.PlayerDungeonLeaveEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.framework.stereotype.EventListener
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerJoinEvent

@Component
class StatisticsListener(
    private val statisticsService: StatisticsService
) : EventListener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) = statisticsService.onPlayerJoin(event)

    @EventHandler
    fun onEntityDeath(event: EntityDeathEvent) = statisticsService.onEntityDeath(event)

    @EventHandler
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) = statisticsService.onEntityDamageByEntity(event)

    @EventHandler
    fun onDungeonComplete(event: DungeonCompleteEvent) = statisticsService.onDungeonComplete(event)

    @EventHandler
    fun onDungeonOpen(event: DungeonOpenEvent) = statisticsService.onDungeonOpen(event)

    @EventHandler
    fun onKillStreakIncrease(event: KillStreakIncreaseEvent) = statisticsService.onKillStreakIncrease(event)

    @EventHandler
    fun onPlayerDungeonLeave(event: PlayerDungeonLeaveEvent) = statisticsService.onPlayerDungeonLeave(event)
}
