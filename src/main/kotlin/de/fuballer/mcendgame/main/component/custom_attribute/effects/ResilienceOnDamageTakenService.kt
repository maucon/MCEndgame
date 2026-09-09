package de.fuballer.mcendgame.main.component.custom_attribute.effects

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asDoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getAllCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttributeType
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.status_effect.CustomStatusEffects
import de.fuballer.mcendgame.main.messaging.misc.LivingEntityDamagedEvent
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.world.effect.MobEffectInstance
import kotlin.math.min
import kotlin.random.Random

@Injectable
class ResilienceOnDamageTakenService {
    @EventSubscriber(sync = true)
    fun on(event: LivingEntityDamagedEvent) {
        if (event.amount <= 0) return

        val damaged = event.damaged
        val customAttributes = damaged.getAllCustomAttributes()
        val stacksGained = getGuaranteedCount(customAttributes) + getChanceCount(customAttributes)
        if (stacksGained == 0) return

        val currentResilience = damaged.getEffect(CustomStatusEffects.RESILIENCE)?.amplifier ?: -1
        val newResilience = min(currentResilience + stacksGained, 9)

        damaged.addEffect(MobEffectInstance(CustomStatusEffects.RESILIENCE, 199, newResilience, false, true, true))
    }

    private fun getGuaranteedCount(
        attributes: Map<CustomAttributeType, List<CustomAttribute>>,
    ) = attributes[CustomAttributeTypes.RESILIENCE_ON_DAMAGE_TAKEN]?.size ?: 0

    private fun getChanceCount(
        attributes: Map<CustomAttributeType, List<CustomAttribute>>,
    ): Int {
        val chanceAttributes = attributes[CustomAttributeTypes.RESILIENCE_ON_DAMAGE_TAKEN_CHANCE] ?: return 0
        return chanceAttributes
            .map { it.rolls[0].asDoubleRoll().getValue() }
            .filter { Random.nextDouble() <= it }
            .size
    }
}