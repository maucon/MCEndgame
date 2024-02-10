package de.fuballer.mcendgame.component.custom_entity.types.leech

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.technical.extension.EntityExtension.getCustomEntityType
import de.fuballer.mcendgame.util.PluginUtil.runTaskLater
import org.bukkit.entity.Bee
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.scheduler.BukkitRunnable

@Component
class LeechService(
) : Listener {
    @EventHandler(ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        if (event.damager.getCustomEntityType() != LeechEntityType) return

        val leech = event.damager as Bee
        ResetStingRunnable(leech, event.damaged).runTaskLater(40) // fires after vanilla event
    }

    private class ResetStingRunnable(
        private val leech: Bee,
        private val target: LivingEntity
    ) : BukkitRunnable() {
        override fun run() {
            if (leech.isDead) {
                this.cancel()
                return
            }

            leech.setHasStung(false)
            leech.target = target
            leech.anger = Int.MAX_VALUE
        }
    }
}