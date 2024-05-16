package de.fuballer.mcendgame.component.dungeon.transition

import de.fuballer.mcendgame.component.dungeon.transition.db.DungeonTransitionEntity
import de.fuballer.mcendgame.component.dungeon.transition.db.DungeonTransitionRepository
import de.fuballer.mcendgame.event.*
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.WorldExtension.isDungeonWorld
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerRespawnEvent

@Component
class DungeonTransitionService(
    private val dungeonTransitionRepo: DungeonTransitionRepository
) : Listener {
    @EventHandler
    fun on(event: DungeonGeneratedEvent) {
        val entity = DungeonTransitionEntity(event.world.name, event.respawnLocation)
        dungeonTransitionRepo.save(entity)
    }

    @EventHandler
    fun on(event: PlayerChangedWorldEvent) {
        val player = event.player

        if (player.world.isDungeonWorld()) {
            val playerDungeonJoinEvent = PlayerDungeonJoinEvent(player, player.world, player.location)
            EventGateway.apply(playerDungeonJoinEvent)
        } else if (event.from.isDungeonWorld()) {
            val playerDungeonLeaveEvent = PlayerDungeonLeaveEvent(player)
            EventGateway.apply(playerDungeonLeaveEvent)
        }
    }

    @EventHandler
    fun on(event: PlayerJoinEvent) {
        val player = event.player

        if (player.world.isDungeonWorld()) {
            val playerDungeonJoinEvent = PlayerDungeonJoinEvent(player, player.world, player.location)
            EventGateway.apply(playerDungeonJoinEvent)
        } else {
            val playerDungeonLeaveEvent = PlayerDungeonLeaveEvent(player)
            EventGateway.apply(playerDungeonLeaveEvent)
        }
    }

    @EventHandler
    fun on(event: PlayerQuitEvent) {
        val player = event.player

        if (player.world.isDungeonWorld()) {
            val playerDungeonLeaveEvent = PlayerDungeonLeaveEvent(player)
            EventGateway.apply(playerDungeonLeaveEvent)
        } else {
            val playerDungeonJoinEvent = PlayerDungeonJoinEvent(player, player.world, player.location)
            EventGateway.apply(playerDungeonJoinEvent)
        }
    }

    @EventHandler
    fun on(event: DungeonEntityDeathEvent) {
        val entity = event.entity
        if (entity !is Player) return

        val playerDungeonLeaveEvent = PlayerDungeonLeaveEvent(entity)
        EventGateway.apply(playerDungeonLeaveEvent)
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun on(event: PlayerRespawnEvent) {
        val player = event.player
        val world = player.world

        val entity = dungeonTransitionRepo.findById(world.name) ?: return
        event.respawnLocation = entity.respawnLocation
    }

    @EventHandler
    fun on(event: DungeonWorldDeleteEvent) {
        dungeonTransitionRepo.deleteById(event.world.name)
    }
}