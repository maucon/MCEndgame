package de.fuballer.mcendgame.component.dungeon.killstreak

import de.fuballer.mcendgame.component.dungeon.killstreak.db.KillStreakEntity
import de.fuballer.mcendgame.component.dungeon.killstreak.db.KillStreakRepository
import de.fuballer.mcendgame.event.*
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.technical.TimerTask
import de.fuballer.mcendgame.technical.persistent_data.TypeKeys
import de.fuballer.mcendgame.util.DungeonUtil
import de.fuballer.mcendgame.util.PersistentDataUtil
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.Server
import org.bukkit.World
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerChangedWorldEvent
import java.util.*
import kotlin.math.min

@Component
class KillStreakService(
    private val killStreakRepo: KillStreakRepository,
    private val server: Server
) : Listener {
    @EventHandler
    fun on(event: DungeonEntityDeathEvent) {
        val entity = event.entity as? LivingEntity ?: return
        val world = entity.world

        if (!PersistentDataUtil.getBooleanValue(entity, TypeKeys.IS_ENEMY)) return
        if (PersistentDataUtil.getBooleanValue(entity, TypeKeys.IS_MINION)) return

        val killStreak = killStreakRepo.findById(world.name) ?: return
        killStreak.streak++
        killStreak.timer = KillStreakSettings.TIMER_MS.toLong()
        killStreak.bossBar.setTitle("${killStreak.streak}")
        killStreak.bossBar.progress = 1.0

        killStreakRepo.save(killStreak)

        val killStreakIncreaseEvent = KillStreakIncreaseEvent(killStreak.streak, entity.world)
        EventGateway.apply(killStreakIncreaseEvent)
    }

    @EventHandler
    fun on(event: EntityDamageByEntityEvent) {
        if (WorldUtil.isNotDungeonWorld(event.entity.world)) return
        if (event.damage < KillStreakSettings.MIN_DMG_FOR_EXTRA_TIME) return
        if (!PersistentDataUtil.getBooleanValue(event.entity, TypeKeys.IS_ENEMY)) return

        val entity = event.entity as? LivingEntity ?: return
        val damager = event.damager
        if (!DungeonUtil.isPlayerOrPlayerProjectile(damager)) return

        val killStreak = killStreakRepo.findById(entity.world.name) ?: return
        killStreak.timer = min(
            KillStreakSettings.TIMER_MS.toLong(),
            killStreak.timer + KillStreakSettings.TIME_PER_HIT
        )
        killStreak.bossBar.progress = killStreak.timer.toDouble() / KillStreakSettings.TIMER_MS

        killStreakRepo.save(killStreak)
    }

    @EventHandler
    fun on(event: PlayerDungeonLeaveEvent) {
        val player = event.player
        val world = event.player.world

        removePlayerFromBossBar(player, world)
    }

    @EventHandler
    fun on(event: PlayerDungeonJoinEvent) {
        val player = event.player
        val world = event.world

        addPlayerToBossBar(player, world)
    }

    @EventHandler
    fun on(event: PlayerChangedWorldEvent) {
        val player = event.player
        val world = event.player.world

        if (WorldUtil.isDungeonWorld(world)) {
            addPlayerToBossBar(player, world)
        } else {
            removePlayerFromBossBar(player, event.from)
        }
    }

    @EventHandler
    fun on(event: DungeonWorldDeleteEvent) {
        val worldName = event.world.name

        val killStreak = killStreakRepo.findById(worldName) ?: return
        killStreak.updateTask?.cancel()

        killStreakRepo.delete(worldName)
    }

    @EventHandler
    fun on(event: DungeonOpenEvent) {
        val name = event.dungeonWorld.name
        if (killStreakRepo.exists(name)) return

        val bossBar = server.createBossBar("0", KillStreakSettings.BAR_COLOR, KillStreakSettings.BAR_STYLE)
            .apply { progress = 0.0 }
        val killStreak = KillStreakEntity(name, bossBar)

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
