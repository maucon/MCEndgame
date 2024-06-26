package de.fuballer.mcendgame.component.statistics

import de.fuballer.mcendgame.component.statistics.db.StatisticsEntity
import de.fuballer.mcendgame.component.statistics.db.StatisticsRepository
import de.fuballer.mcendgame.event.*
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.framework.stereotype.LifeCycleListener
import de.fuballer.mcendgame.util.extension.EntityExtension.isBoss
import de.fuballer.mcendgame.util.extension.WorldExtension.isDungeonWorld
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerJoinEvent
import kotlin.math.max

@Component
class StatisticsService(
    private val statisticsRepo: StatisticsRepository
) : Listener, LifeCycleListener {
    override fun terminate() {
        statisticsRepo.flush()
    }

    @EventHandler
    fun on(event: PlayerJoinEvent) {
        val player = event.player.uniqueId
        if (!statisticsRepo.exists(player)) {
            statisticsRepo.save(StatisticsEntity(player))
        }
    }

    @EventHandler
    fun on(event: DungeonEntityDeathEvent) {
        if (event.entity is Player) {
            val player = event.entity.uniqueId

            val statistics = statisticsRepo.findById(player) ?: return
            statistics.deaths++

            statisticsRepo.save(statistics)
            return
        }

        val monster = event.entity as? Monster ?: return
        val killer = monster.killer ?: return
        val player = killer as? Player ?: return

        onMonsterKilledByPlayer(player, monster)
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun on(event: EntityDamageByEntityEvent) {
        if (!event.entity.world.isDungeonWorld()) return

        var damager = event.damager
        damager = testIfDamagerIsArrow(damager)
        damager = testIfDamagerIsPotion(damager)

        if (damager is Player) {
            onEntityDamagedByPlayer(damager, event)
        }
        if (event.entity is Player) {
            onPlayerDamagedByEntity(event.entity as Player, event)
        }
    }

    @EventHandler
    fun on(event: DungeonCompleteEvent) {
        for (player in event.world.players) {
            val statistics = statisticsRepo.findById(player.uniqueId) ?: return
            statistics.dungeonsCompleted++
            statistics.highestCompletedDungeon = max(event.mapTier, statistics.highestCompletedDungeon)

            statisticsRepo.save(statistics)
        }
    }

    @EventHandler
    fun on(event: DungeonOpenEvent) {
        val statistics = statisticsRepo.findById(event.player.uniqueId) ?: return
        statistics.dungeonsOpened++

        statisticsRepo.save(statistics)
    }

    @EventHandler
    fun on(event: KillStreakUpdatedEvent) {
        for (player in event.players) {
            val statistics = statisticsRepo.findById(player.uniqueId) ?: return
            statistics.highestKillstreak = max(event.streak, statistics.highestKillstreak)

            statisticsRepo.save(statistics)
        }
    }

    @EventHandler
    fun on(@Suppress("UNUSED_PARAMETER") event: DungeonWorldDeleteEvent) {
        statisticsRepo.flush()
    }

    private fun onMonsterKilledByPlayer(player: Player, monster: Monster) {
        val statistics = statisticsRepo.findById(player.uniqueId) ?: return
        statistics.totalKills++

        if (monster.isBoss()) {
            statistics.bossKills++
        }

        statisticsRepo.save(statistics)
    }

    private fun testIfDamagerIsArrow(damager: Entity): Entity {
        if (damager !is Arrow) return damager

        val shooter = damager.shooter
        if (shooter is LivingEntity) return shooter

        return damager
    }

    private fun testIfDamagerIsPotion(damager: Entity): Entity {
        if (damager !is ThrownPotion) return damager

        val shooter = damager.shooter
        if (shooter is LivingEntity) return shooter

        return damager
    }

    private fun onEntityDamagedByPlayer(
        player: Player,
        event: EntityDamageByEntityEvent
    ) {
        if (event.entity !is Monster) return

        val statistics = statisticsRepo.findById(player.uniqueId) ?: return
        statistics.damageDealt += event.finalDamage
        statistics.rawDamageDealt += event.damage

        statisticsRepo.save(statistics)
    }

    private fun onPlayerDamagedByEntity(
        player: Player,
        event: EntityDamageByEntityEvent
    ) {
        val statistics = statisticsRepo.findById(player.uniqueId) ?: return
        statistics.damageTaken += event.finalDamage
        statistics.rawDamageTaken += event.damage

        statisticsRepo.save(statistics)
    }
}
