package de.fuballer.mcendgame.component.statistics

import de.fuballer.mcendgame.component.dungeon.enemy.data.MobDamagePrefix
import de.fuballer.mcendgame.component.statistics.db.StatisticsEntity
import de.fuballer.mcendgame.component.statistics.db.StatisticsRepository
import de.fuballer.mcendgame.event.DungeonCompleteEvent
import de.fuballer.mcendgame.event.DungeonOpenEvent
import de.fuballer.mcendgame.event.KillStreakIncreaseEvent
import de.fuballer.mcendgame.event.PlayerDungeonLeaveEvent
import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.entity.*
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerJoinEvent
import kotlin.math.max

@Service
class StatisticsService(
    private val statisticsRepo: StatisticsRepository
) {
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player.uniqueId
        if (!statisticsRepo.exists(player)) {
            statisticsRepo.save(StatisticsEntity(player))
        }
    }

    fun onEntityDeath(event: EntityDeathEvent) {
        if (WorldUtil.isNotDungeonWorld(event.entity.world)) return

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

        onMonsterKilledByPlayer(monster, player)
    }

    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        if (WorldUtil.isNotDungeonWorld(event.entity.world)) return

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

    fun onDungeonComplete(event: DungeonCompleteEvent) {
        for (player in event.world.players) {
            val statistics = statisticsRepo.findById(player.uniqueId) ?: return
            statistics.dungeonsCompleted++
            statistics.highestCompletedDungeon = max(event.mapTier, statistics.highestCompletedDungeon)

            statisticsRepo.save(statistics)
        }
    }

    fun onDungeonOpen(event: DungeonOpenEvent) {
        val statistics = statisticsRepo.findById(event.player.uniqueId) ?: return
        statistics.dungeonsOpened++

        statisticsRepo.save(statistics)
    }

    fun onKillStreakIncrease(event: KillStreakIncreaseEvent) {
        val killStreak = event.killstreak

        for (player in event.world.players) {
            val statistics = statisticsRepo.findById(player.uniqueId) ?: return

            statistics.highestKillstreak = max(killStreak, statistics.highestKillstreak)
            if (statistics.killstreakThresholdsReached.containsKey(killStreak)) {
                statistics.killstreakThresholdsReached[killStreak] = statistics.killstreakThresholdsReached[killStreak]!! + 1
            }

            statisticsRepo.save(statistics)
        }
    }

    fun onPlayerDungeonLeave(@Suppress("UNUSED_PARAMETER") event: PlayerDungeonLeaveEvent) {
        statisticsRepo.flush()
    }

    private fun onMonsterKilledByPlayer(
        monster: Monster,
        player: Player
    ) {
        val statistics = statisticsRepo.findById(player.uniqueId) ?: return
        statistics.totalKills++

        testForMobType(monster, statistics)
        testForMobStrength(monster, statistics)

        statisticsRepo.save(statistics)
    }

    private fun testForMobType(
        monster: Monster,
        statistics: StatisticsEntity
    ) {
        val entityType = monster.type

        if (!statistics.mobTypeKills.containsKey(entityType)) return
        statistics.mobTypeKills[entityType] = statistics.mobTypeKills[entityType]!! + 1
    }

    private fun testForMobStrength(
        monster: Monster,
        statistics: StatisticsEntity
    ) {
        val prefix = monster.name.split(" ")[0]
        val mobDamagePrefix = MobDamagePrefix.fromPrefixStartsWith(prefix) ?: return

        statistics.strongerMobKills[mobDamagePrefix] = statistics.strongerMobKills[mobDamagePrefix]!! + 1
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
