package de.fuballer.mcendgame.component.dungeon.player_list

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.component.dungeon.player_list.db.DungeonPlayerListEntity
import de.fuballer.mcendgame.component.dungeon.player_list.db.DungeonPlayerListRepository
import de.fuballer.mcendgame.component.dungeon.player_list.db.PlayerStats
import de.fuballer.mcendgame.event.*
import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.util.EntityUtil
import de.fuballer.mcendgame.util.extension.EntityExtension.isBoss
import de.fuballer.mcendgame.util.extension.EntityExtension.isEnemy
import de.fuballer.mcendgame.util.extension.EntityExtension.isMinion
import de.fuballer.mcendgame.util.extension.WorldExtension.isDungeonWorld
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

@Service
class DungeonPlayerListService(
    private val dungeonPlayerListRepo: DungeonPlayerListRepository
) : Listener {
    @EventHandler
    fun on(event: DungeonGeneratedEvent) {
        val entity = DungeonPlayerListEntity(event.world, event.seed, event.tier)
        dungeonPlayerListRepo.save(entity)
    }

    @EventHandler
    fun on(event: PlayerDungeonJoinEvent) {
        val player = event.player
        val entity = dungeonPlayerListRepo.findById(event.dungeonWorld) ?: return

        val playerStats = entity.playerStats[player] ?: PlayerStats()
        entity.playerStats[player] = playerStats
        dungeonPlayerListRepo.save(entity)

        updatePlayerListForPlayer(entity, player)
    }

    @EventHandler
    fun on(event: DungeonEnemySpawnedEvent) {
        if (event.minions) return
        val entity = dungeonPlayerListRepo.findById(event.world) ?: return

        entity.remainingData.remaining += event.entities.size
        dungeonPlayerListRepo.save(entity)

        for (player in entity.playerStats.keys) {
            updatePlayerListForPlayer(entity, player)
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        if (!event.isDungeonWorld) return
        val entity = dungeonPlayerListRepo.findById(event.world) ?: return

        updateIfPlayerIsDamaged(event, entity)
        updateIfPlayerDealtDamage(event, entity)
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun on(event: DungeonEntityDeathEvent) {
        val entity = event.entity
        val playerListEntity = dungeonPlayerListRepo.findById(entity.world) ?: return

        val remainingData = playerListEntity.remainingData

        if (entity.isBoss()) {
            remainingData.bossesSlain++
        }
        if (entity.isEnemy() && !entity.isMinion()) {
            remainingData.remaining--
        }

        playerListEntity.playerStats[entity.killer]?.also {
            it.kills++
        }

        dungeonPlayerListRepo.save(playerListEntity)

        for (player in playerListEntity.playerStats.keys) {
            updatePlayerListForPlayer(playerListEntity, player)
        }
    }

    @EventHandler
    fun on(event: DungeonCompleteEvent) {
        val entity = dungeonPlayerListRepo.findById(event.world) ?: return

        entity.remainingData.dungeonCompleted = true
        dungeonPlayerListRepo.save(entity)

        for (player in entity.playerStats.keys) {
            updatePlayerListForPlayer(entity, player)
        }
    }

    @EventHandler
    fun on(event: PlayerDungeonLeaveEvent) {
        val player = event.player

        player.sendPlayerListHeaderAndFooter(Component.empty(), Component.empty())
        player.playerListName(null)
    }

    @EventHandler
    fun on(event: DungeonWorldDeleteEvent) {
        dungeonPlayerListRepo.deleteById(event.world)
    }

    private fun updateIfPlayerIsDamaged(
        event: DamageCalculationEvent,
        entity: DungeonPlayerListEntity
    ) {
        val player = event.damaged as? Player ?: return
        val playerStats = entity.playerStats[player] ?: return

        playerStats.damageTaken += event.getFinalDamage()
        dungeonPlayerListRepo.save(entity)

        updatePlayerListForPlayer(entity, player)
    }

    private fun updateIfPlayerDealtDamage(
        event: DamageCalculationEvent,
        entity: DungeonPlayerListEntity
    ) {
        val player = EntityUtil.getPlayerDamager(event.damager) ?: return
        val playerStats = entity.playerStats[player] ?: return

        playerStats.damageDealt += event.getFinalDamage()
        dungeonPlayerListRepo.save(entity)

        updatePlayerListForPlayer(entity, player)
    }

    private fun updatePlayerListForPlayer(
        entity: DungeonPlayerListEntity,
        player: Player
    ) {
        if (!player.world.isDungeonWorld()) return

        val playerStats = entity.playerStats[player]!!

        val footer = DungeonPlayerListSettings.getFooter(entity)
        val playerNameComponent = DungeonPlayerListSettings.getPlayerNameComponent(player, playerStats)

        player.sendPlayerListHeaderAndFooter(Component.empty(), footer)
        player.playerListName(playerNameComponent)
    }
}