package de.fuballer.mcendgame.component.custom_entity.ability.runner

import de.fuballer.mcendgame.component.custom_entity.ability.AbilitySettings
import de.fuballer.mcendgame.util.PluginUtil.runTaskTimer
import org.bukkit.entity.Creature
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

class InactivityRunner(
    private val entity: Creature,
    private val callback: () -> Unit
) {
    private var task: BukkitTask? = null

    fun run() {
        task = AbilityRunnable(entity, callback)
            .runTaskTimer(0, AbilitySettings.IDLE_CHECK_PERIOD)
    }

    fun cancel() = task?.cancel()

    class AbilityRunnable(
        private val entity: Creature,
        private val callback: () -> Unit
    ) : BukkitRunnable() {
        private var ticksSinceNoTarget = 0L

        override fun run() {
            if (entity.isDead || ticksSinceNoTarget >= AbilitySettings.MAX_IDLE_TIME) {
                callback.invoke()
                this.cancel()

                return
            }

            if (entity.target != null) { // entity has target
                ticksSinceNoTarget = 0
            } else {
                ticksSinceNoTarget += AbilitySettings.IDLE_CHECK_PERIOD
            }
        }
    }
}