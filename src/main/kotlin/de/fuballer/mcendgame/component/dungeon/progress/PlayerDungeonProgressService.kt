package de.fuballer.mcendgame.component.dungeon.progress

import de.fuballer.mcendgame.component.dungeon.progress.db.PlayerDungeonProgressEntity
import de.fuballer.mcendgame.component.dungeon.progress.db.PlayerDungeonProgressRepository
import de.fuballer.mcendgame.event.DungeonCompleteEvent
import de.fuballer.mcendgame.framework.stereotype.Service
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerRespawnEvent
import java.util.*
import kotlin.math.max

class PlayerDungeonProgressService(
    private val playerDungeonProgressRepo: PlayerDungeonProgressRepository
) : Service {
    fun onDungeonComplete(event: DungeonCompleteEvent) {
        for (player in event.world.players) {
            val completedTier = getPlayerDungeonLevel(player.uniqueId).level

            if (completedTier <= event.mapTier) {
                val (_, level, progress) = increasePlayerDungeonLevel(player.uniqueId)
                player.sendMessage(PlayerDungeonProgressSettings.getProgressMessage(level, progress))
            } else {
                player.sendMessage(PlayerDungeonProgressSettings.NO_PROGRESS_MESSAGE)
            }
        }
    }

    fun onEntityDeath(event: EntityDeathEvent) {
        val player = event.entity
        if (WorldUtil.isNotDungeonWorld(player.world)) return
        if (player !is Player) return

        val entity = playerDungeonProgressRepo.findById(player.uniqueId)
            ?: PlayerDungeonProgressEntity(player.uniqueId)

        entity.level = max(entity.level - 1, 1)
        entity.progress = 0

        playerDungeonProgressRepo.save(entity)
        playerDungeonProgressRepo.flush()
    }

    fun onPlayerRespawn(event: PlayerRespawnEvent) {
        val player = event.player
        if (WorldUtil.isNotDungeonWorld(player.world)) return

        val (_, level, progress) = getPlayerDungeonLevel(player.uniqueId)
        player.sendMessage(PlayerDungeonProgressSettings.getRegressMessage(level, progress))
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
            entity.level++
        }

        playerDungeonProgressRepo.save(entity)
        playerDungeonProgressRepo.flush()

        return entity
    }
}
