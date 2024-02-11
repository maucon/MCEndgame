package de.fuballer.mcendgame.component.custom_entity.types.reaper

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.technical.extension.EntityExtension.getCustomEntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

@Component
class ReaperService : Listener {
    @EventHandler(ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        if (event.damager.getCustomEntityType() != ReaperEntityType) return

        event.onHitPotionEffects.add(ReaperSettings.DARKNESS_EFFECT)
    }
}
