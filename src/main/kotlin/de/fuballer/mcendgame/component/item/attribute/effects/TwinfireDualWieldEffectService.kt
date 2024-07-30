package de.fuballer.mcendgame.component.item.attribute.effects

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.component.item.attribute.CustomAttributeTypes
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.AttributeRollExtension.getFirstAsDouble
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

@Component
class TwinfireDualWieldEffectService : Listener {
    @EventHandler(ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        val twinfireAttributes = event.damagerAttributes[CustomAttributeTypes.TWINFIRE_DUAL_WIELD] ?: return
        if (twinfireAttributes.size < 2) return

        val moreDamageValues = twinfireAttributes.map { it.attributeRolls.getFirstAsDouble() }
        event.moreDamage.addAll(moreDamageValues)
    }
}