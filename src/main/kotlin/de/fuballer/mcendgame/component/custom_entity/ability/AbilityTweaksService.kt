package de.fuballer.mcendgame.component.custom_entity.ability

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.EntityExtension.getHitCountBasedHealth
import de.fuballer.mcendgame.util.extension.EntityExtension.isForcedVehicle
import de.fuballer.mcendgame.util.extension.EntityExtension.setHitCountBasedHealth
import de.fuballer.mcendgame.util.extension.EventExtension.cancel
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.spigotmc.event.entity.EntityDismountEvent

@Component
class AbilityTweaksService : Listener {
    @EventHandler
    fun on(event: EntityDismountEvent) {
        val vehicle = event.dismounted
        if (!vehicle.isForcedVehicle()) return

        event.cancel()
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        if (event.cause == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) return

        val hitCountBasedHealth = event.damaged.getHitCountBasedHealth() ?: return

        val newHitCountBasedHealth = hitCountBasedHealth - 1
        event.damaged.setHitCountBasedHealth(newHitCountBasedHealth)
        if (newHitCountBasedHealth <= 0) event.damaged.remove()

        event.cancel()
    }
}