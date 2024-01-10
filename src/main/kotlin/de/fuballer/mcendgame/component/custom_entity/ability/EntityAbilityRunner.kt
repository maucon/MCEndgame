package de.fuballer.mcendgame.component.custom_entity.ability

import de.fuballer.mcendgame.component.custom_entity.data.CustomEntityType
import de.fuballer.mcendgame.util.PluginUtil.runTaskTimer
import de.fuballer.mcendgame.util.random.RandomUtil
import org.bukkit.entity.Creature
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

class EntityAbilityRunner(
    private val entity: Creature,
    private val customEntityType: CustomEntityType,
    private val mapTier: Int
) {
    private var task: BukkitTask? = null

    fun run() {
        if (customEntityType.abilities == null) return
        val abilityCooldown = AbilitySettings.getAbilityCooldown(mapTier)

        task = EntityAbilityRunnable(entity, customEntityType, abilityCooldown)
            .runTaskTimer(0, AbilitySettings.ABILITY_CHECK_PERIOD)
    }

    fun cancel() = task?.cancel()

    fun isCancelled() = task?.isCancelled ?: true

    class EntityAbilityRunnable(
        private val entity: Creature,
        private val customEntityType: CustomEntityType,
        private val abilityCooldown: Int
    ) : BukkitRunnable() {
        private var ticksSinceAbility = 0L
        private var noTargetCount = 0

        override fun run() {
            if (entity.isDead) {
                this.cancel()
                return
            }

            ticksSinceAbility += AbilitySettings.ABILITY_CHECK_PERIOD
            if (ticksSinceAbility < abilityCooldown) return

            ticksSinceAbility = 0

            val usedAbility = castAbility()
            noTargetCount = if (usedAbility) 0 else noTargetCount + 1

            if (noTargetCount * abilityCooldown >= AbilitySettings.MAX_IDLE_TIME) {
                this.cancel()
            }
        }

        private fun castAbility(): Boolean {
            val target = entity.target ?: return false

            val abilities = customEntityType.abilities!! // checked when starting the EntityAbilityRunner
            val pickedAbility = RandomUtil.pick(abilities).option
            pickedAbility.cast(entity, target)

            return true
        }
    }
}