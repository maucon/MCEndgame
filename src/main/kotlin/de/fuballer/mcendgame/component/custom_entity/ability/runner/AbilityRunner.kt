package de.fuballer.mcendgame.component.custom_entity.ability.runner

import de.fuballer.mcendgame.component.custom_entity.ability.AbilitySettings
import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import de.fuballer.mcendgame.util.PluginUtil.runTaskTimer
import de.fuballer.mcendgame.util.random.RandomUtil
import org.bukkit.entity.Creature
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

class AbilityRunner(
    private val entity: Creature,
    private val customEntityType: CustomEntityType,
    private val abilityCooldown: Int
) {
    private var task: BukkitTask? = null

    fun run() {
        if (customEntityType.abilities == null) return

        task = AbilityRunnable(entity, customEntityType, abilityCooldown)
            .runTaskTimer(0, AbilitySettings.ABILITY_CHECK_PERIOD)
    }

    fun cancel() = task?.cancel()

    class AbilityRunnable(
        private val entity: Creature,
        private val customEntityType: CustomEntityType,
        private val abilityCooldown: Int
    ) : BukkitRunnable() {
        private var ticksSinceAbility = 0L

        override fun run() {
            if (ticksSinceAbility >= abilityCooldown) {
                ticksSinceAbility = 0

                castAbility()
            }

            ticksSinceAbility += AbilitySettings.ABILITY_CHECK_PERIOD
        }

        private fun castAbility() {
            val remainingOptions = customEntityType.abilities!!.toMutableList() // checked when starting the EntityAbilityRunner

            while (remainingOptions.isNotEmpty()) {
                val pick = RandomUtil.pick(remainingOptions)
                remainingOptions.remove(pick)

                val ability = pick.option
                if (!ability.canCast(entity)) continue

                ability.cast(entity)
                return
            }
        }
    }
}