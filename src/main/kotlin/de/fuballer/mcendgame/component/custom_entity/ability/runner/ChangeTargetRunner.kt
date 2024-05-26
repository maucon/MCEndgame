package de.fuballer.mcendgame.component.custom_entity.ability.runner

import de.fuballer.mcendgame.component.custom_entity.ability.AbilitySettings
import de.fuballer.mcendgame.util.DungeonUtil
import de.fuballer.mcendgame.util.PluginUtil.runTaskTimer
import org.bukkit.entity.Creature
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import kotlin.random.Random
import kotlin.random.nextLong

class ChangeTargetRunner(
    private val entity: Creature
) {
    private var task: BukkitTask? = null

    fun run() {
        task = AbilityRunnable(entity)
            .runTaskTimer(0, AbilitySettings.CHANGE_TARGET_CHECK_PERIOD)
    }

    fun cancel() = task?.cancel()

    class AbilityRunnable(
        private val entity: Creature
    ) : BukkitRunnable() {
        private var ticksSinceCast = 0L
        private var cooldown = getCooldown()

        override fun run() {
            if (ticksSinceCast >= cooldown) {
                changeTarget()

                ticksSinceCast = 0
                cooldown = getCooldown()
            }

            ticksSinceCast += AbilitySettings.CHANGE_TARGET_CHECK_PERIOD
        }

        private fun getCooldown() = Random.nextLong(AbilitySettings.CHANGE_TARGET_MIN_COOLDOWN..AbilitySettings.CHANGE_TARGET_MAX_COOLDOWN)

        private fun changeTarget() {
            val targets = DungeonUtil.getNearbyPlayers(entity, AbilitySettings.DEFAULT_TARGET_RANGE)
            if (targets.isEmpty()) return

            entity.target = targets.random()
        }
    }
}