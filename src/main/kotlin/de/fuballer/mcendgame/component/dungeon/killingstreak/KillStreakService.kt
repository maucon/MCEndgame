package de.fuballer.mcendgame.component.dungeon.killingstreak

import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.Keys
import de.fuballer.mcendgame.component.dungeon.killingstreak.db.KillStreakEntity
import de.fuballer.mcendgame.component.dungeon.killingstreak.db.KillStreakRepository
import de.fuballer.mcendgame.event.DungeonOpenEvent
import de.fuballer.mcendgame.event.DungeonWorldDeleteEvent
import de.fuballer.mcendgame.event.EventGateway
import de.fuballer.mcendgame.event.KillStreakIncreaseEvent
import de.fuballer.mcendgame.framework.stereotype.Service
import de.fuballer.mcendgame.helper.TimerTask
import de.fuballer.mcendgame.helper.WorldHelper
import org.bukkit.World
import org.bukkit.entity.EntityType
import org.bukkit.entity.Monster
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.persistence.PersistentDataType
import java.util.*
import kotlin.math.min

class KillStreakService(
    private val killStreakRepo: KillStreakRepository
) : Service {
    fun onEntityDeath(event: EntityDeathEvent) {
        val entity = event.entity as? Monster ?: return
        val world = entity.world
        if (WorldHelper.isNotDungeonWorld(world)) return

        if (event.entity.persistentDataContainer.has(Keys.IS_MINION, PersistentDataType.BOOLEAN))
            if (event.entity.persistentDataContainer.get(Keys.IS_MINION, PersistentDataType.BOOLEAN) == true) return

        val killStreak = killStreakRepo.findById(world.name) ?: return
        killStreak.streak++
        killStreak.timer = KillStreakSettings.TIMER_MS.toLong()
        killStreak.bossBar.setTitle("${killStreak.streak}")
        killStreak.bossBar.progress = 1.0

        killStreakRepo.save(killStreak)

        val killStreakIncreaseEvent = KillStreakIncreaseEvent(killStreak.streak, entity.world)
        EventGateway.apply(killStreakIncreaseEvent)
    }

    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        if (event.damage < KillStreakSettings.MIN_DMG_FOR_EXTRA_TIME) return
        val entity = event.entity as? Monster ?: return
        if (WorldHelper.isNotDungeonWorld(event.entity.world)) return

        val damager = event.damager
        if (damager is Projectile && damager.shooter !is Player) return
        if (damager.type != EntityType.PLAYER) return

        val killStreak = killStreakRepo.findById(entity.world.name) ?: return
        killStreak.timer = min(
            KillStreakSettings.TIMER_MS.toLong(),
            killStreak.timer + KillStreakSettings.TIME_PER_HIT
        )
        killStreak.bossBar.progress = killStreak.timer.toDouble() / KillStreakSettings.TIMER_MS

        killStreakRepo.save(killStreak)
    }

    fun onPlayerQuit(event: PlayerQuitEvent) {
        val player = event.player
        val world = event.player.world

        if (WorldHelper.isNotDungeonWorld(world)) return
        removePlayerFromBossBar(player, world)
    }

    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        val world = event.player.world
        if (WorldHelper.isNotDungeonWorld(world)) return

        addPlayerToBossBar(player, world)
    }

    fun onPlayerChangedWorld(event: PlayerChangedWorldEvent) {
        val player = event.player
        val world = event.player.world

        if (WorldHelper.isDungeonWorld(world)) {
            addPlayerToBossBar(player, world)
        } else {
            removePlayerFromBossBar(player, event.from)
        }
    }

    fun onDungeonWorldDelete(event: DungeonWorldDeleteEvent) {
        val worldName = event.world.name

        val killStreak = killStreakRepo.findById(worldName) ?: return
        killStreak.updateTask?.cancel()

        killStreakRepo.delete(worldName)
    }

    fun onDungeonOpen(event: DungeonOpenEvent) {
        val name = event.dungeonWorld.name
        if (killStreakRepo.exists(name)) return

        val killStreak = KillStreakEntity(name)

        val updateTask = TimerTask { updateKillStreak(killStreak) }
        killStreak.updateTask = updateTask

        killStreakRepo.save(killStreak)

        Timer().schedule(
            updateTask,
            KillStreakSettings.TIMER_PERIOD,
            KillStreakSettings.TIMER_PERIOD
        )
    }

    private fun addPlayerToBossBar(player: Player, world: World) {
        val killStreak = killStreakRepo.findById(world.name) ?: return
        killStreak.bossBar.addPlayer(player)

        killStreakRepo.save(killStreak)
    }

    private fun removePlayerFromBossBar(player: Player, world: World) {
        val killStreak = killStreakRepo.findById(world.name) ?: return
        killStreak.bossBar.removePlayer(player)

        killStreakRepo.save(killStreak)
    }

    private fun updateKillStreak(killStreak: KillStreakEntity) {
        if (killStreak.timer == 0L) return
        killStreak.timer -= KillStreakSettings.TIMER_PERIOD

        if (killStreak.timer > 0) {
            killStreak.bossBar.progress = killStreak.timer.toDouble() / KillStreakSettings.TIMER_MS

            killStreakRepo.save(killStreak)
            return
        }

        killStreak.timer = 0
        killStreak.streak = 0
        killStreak.bossBar.progress = 0.0
        killStreak.bossBar.setTitle("0")

        killStreakRepo.save(killStreak)
    }
}
