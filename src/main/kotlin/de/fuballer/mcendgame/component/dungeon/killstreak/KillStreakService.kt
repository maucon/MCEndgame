package de.fuballer.mcendgame.component.dungeon.killstreak

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.component.dungeon.killstreak.db.KillStreakEntity
import de.fuballer.mcendgame.component.dungeon.killstreak.db.KillStreakRepository
import de.fuballer.mcendgame.event.*
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.SchedulingUtil
import de.fuballer.mcendgame.util.extension.EntityExtension.isEnemy
import de.fuballer.mcendgame.util.extension.EntityExtension.isMinion
import de.fuballer.mcendgame.util.extension.WorldExtension.isDungeonWorld
import org.bukkit.Server
import org.bukkit.World
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerChangedWorldEvent
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

        if (!entity.isEnemy()) return
        if (entity.isMinion()) return

        val killStreak = killStreakRepo.findById(world.name) ?: return
        killStreak.streak++
        killStreak.timer = KillStreakSettings.MAX_TIMER
        killStreak.bossBar.setTitle("${killStreak.streak}")
        killStreak.bossBar.progress = 1.0

        killStreakRepo.save(killStreak)

        val killStreakIncreaseEvent = KillStreakIncreaseEvent(killStreak.streak, entity.world)
        EventGateway.apply(killStreakIncreaseEvent)
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        val player = event.damager as? Player ?: return
        if (!player.world.isDungeonWorld()) return

        if (event.cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK
            && player.attackCooldown < KillStreakSettings.MIN_ATTACK_COOLDOWN_FOR_EXTRA_TIME
        ) return

        val damaged = event.damaged
        if (!damaged.isEnemy()) return

        val killStreak = killStreakRepo.findById(damaged.world.name) ?: return
        killStreak.timer = min(
            KillStreakSettings.MAX_TIMER,
            killStreak.timer + KillStreakSettings.TIME_PER_HIT
        )
        killStreak.bossBar.progress = killStreak.timer.toDouble() / KillStreakSettings.MAX_TIMER

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

        if (world.isDungeonWorld()) {
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

        killStreak.updateTask = SchedulingUtil.runTaskTimer(KillStreakSettings.TIMER_PERIOD) {
            updateKillStreak(killStreak)
        }

        killStreakRepo.save(killStreak)
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
            killStreak.bossBar.progress = killStreak.timer.toDouble() / KillStreakSettings.MAX_TIMER

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
