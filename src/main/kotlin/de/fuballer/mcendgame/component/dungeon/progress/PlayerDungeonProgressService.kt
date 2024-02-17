package de.fuballer.mcendgame.component.dungeon.progress

import de.fuballer.mcendgame.component.dungeon.progress.db.PlayerDungeonProgressEntity
import de.fuballer.mcendgame.component.dungeon.progress.db.PlayerDungeonProgressRepository
import de.fuballer.mcendgame.event.DungeonCompleteEvent
import de.fuballer.mcendgame.event.DungeonEntityDeathEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.WorldExtension.isDungeonWorld
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerRespawnEvent
import java.util.*
import kotlin.math.max

@Component
class PlayerDungeonProgressService(
    private val playerDungeonProgressRepo: PlayerDungeonProgressRepository
) : Listener {
    @EventHandler
    fun on(event: DungeonCompleteEvent) {
        for (player in event.world.players) {
            val completedTier = getPlayerDungeonLevel(player.uniqueId).tier

            if (completedTier <= event.mapTier) {
                val (_, tier, progress) = increasePlayerDungeonLevel(player.uniqueId)
                player.sendMessage(PlayerDungeonProgressSettings.getProgressMessage(tier, progress))
            } else {
                player.sendMessage(PlayerDungeonProgressSettings.NO_PROGRESS_MESSAGE)
            }
        }
    }

    @EventHandler
    fun on(event: DungeonEntityDeathEvent) {
        val player = event.entity
        if (player !is Player) return

        val entity = playerDungeonProgressRepo.findById(player.uniqueId)
            ?: PlayerDungeonProgressEntity(player.uniqueId)

        entity.tier = max(entity.tier - 1, 1)
        entity.progress = 0

        playerDungeonProgressRepo.save(entity)
        playerDungeonProgressRepo.flush()
    }

    @EventHandler
    fun on(event: PlayerRespawnEvent) {
        val player = event.player
        if (!player.world.isDungeonWorld()) return

        val (_, tier, progress) = getPlayerDungeonLevel(player.uniqueId)
        player.sendMessage(PlayerDungeonProgressSettings.getRegressMessage(tier, progress))
    }

    fun getPlayerDungeonLevel(player: UUID): PlayerDungeonProgressEntity {
        playerDungeonProgressRepo.findById(player)
            ?.let { return it }

        return PlayerDungeonProgressEntity(player)
            .also { playerDungeonProgressRepo.save(it) }
    }

    private fun increasePlayerDungeonLevel(player: UUID): PlayerDungeonProgressEntity {
        val entity = playerDungeonProgressRepo.findById(player)
            ?: PlayerDungeonProgressEntity(player)

        if (++entity.progress >= PlayerDungeonProgressSettings.DUNGEON_LEVEL_INCREASE_THRESHOLD) {
            entity.progress = 0
            entity.tier++
        }

        playerDungeonProgressRepo.save(entity)
        playerDungeonProgressRepo.flush()

        return entity
    }
}
