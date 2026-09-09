package de.fuballer.mcendgame.main.component.custom_attribute.effects.heal

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asDoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getAllCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getHealingFactor
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.messaging.misc.LivingEntityDeathEvent
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber

@Injectable
class HealOnKillService {
    @EventSubscriber(sync = true)
    fun on(event: LivingEntityDeathEvent) {
        val killer = event.killer ?: return
        val attributes = killer.getAllCustomAttributes()[CustomAttributeTypes.HEAL_ON_KILL] ?: return
        val baseAmount = attributes.sumOf { it.rolls[0].asDoubleRoll().getValue() }

        val healFactor = killer.getHealingFactor()
        killer.heal((baseAmount * healFactor).toFloat())
    }
}