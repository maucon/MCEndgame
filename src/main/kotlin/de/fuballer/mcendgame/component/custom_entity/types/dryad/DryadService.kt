package de.fuballer.mcendgame.component.custom_entity.types.dryad

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.EntityExtension.getCustomEntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

@Component
class DryadService : Listener {
    @EventHandler(ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        if (event.damager.getCustomEntityType() != DryadEntityType) return

        event.onHitPotionEffects.add(DryadSettings.POISON_EFFECT)
    }
}
