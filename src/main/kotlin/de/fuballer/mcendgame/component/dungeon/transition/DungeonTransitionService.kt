package de.fuballer.mcendgame.component.dungeon.transition

import de.fuballer.mcendgame.component.dungeon.transition.db.DungeonTransitionEntity
import de.fuballer.mcendgame.component.dungeon.transition.db.DungeonTransitionRepository
import de.fuballer.mcendgame.event.*
import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.framework.stereotype.LifeCycleListener
import de.fuballer.mcendgame.util.extension.WorldExtension.isDungeonWorld
import org.bukkit.Server
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerRespawnEvent

@Service
class DungeonTransitionService(
    private val dungeonTransitionRepo: DungeonTransitionRepository,
    private val server: Server
) : Listener, LifeCycleListener {
    override fun terminate() {
        for (player in server.onlinePlayers) {
            val world = player.world
            if (!world.isDungeonWorld()) continue

            val entity = dungeonTransitionRepo.findById(world.name) ?: continue
            player.teleport(entity.teleportLocation)
        }
    }

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
            val playerDungeonLeaveEvent = PlayerDungeonLeaveEvent(player, event.from)
            EventGateway.apply(playerDungeonLeaveEvent)
        }
    }

    @EventHandler
    fun on(event: PlayerQuitEvent) {
        val player = event.player
        val world = player.world
        if (!world.isDungeonWorld()) return

        val entity = dungeonTransitionRepo.findById(world.name) ?: return
        player.teleport(entity.teleportLocation)
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun on(event: PlayerRespawnEvent) {
        val player = event.player
        val world = player.world

        val entity = dungeonTransitionRepo.findById(world.name) ?: return
        event.respawnLocation = entity.teleportLocation
    }

    @EventHandler
    fun on(event: DungeonWorldDeleteEvent) {
        dungeonTransitionRepo.deleteById(event.world.name)
    }
}