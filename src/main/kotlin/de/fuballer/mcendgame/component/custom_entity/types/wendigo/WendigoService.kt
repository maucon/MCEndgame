package de.fuballer.mcendgame.component.custom_entity.types.wendigo

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.EntityExtension.getCustomEntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

@Component
class WendigoService : Listener {
    @EventHandler
    fun on(event: DamageCalculationEvent) {
        if (event.damager.getCustomEntityType() != WendigoEntityType) return

        event.onHitPotionEffects.add(WendigoSettings.DARKNESS_EFFECT)
    }
}
