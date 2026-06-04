package de.fuballer.mcendgame.main.component.custom_attribute.effects

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asIntRoll
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getAllCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.messaging.misc.LivingEntityDeathEvent
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects

private val EFFECTS = mapOf(
    CustomAttributeTypes.STRENGTH_ON_KILL to MobEffects.STRENGTH,
    CustomAttributeTypes.SPEED_ON_KILL to MobEffects.SPEED,
    CustomAttributeTypes.HASTE_ON_KILL to MobEffects.HASTE,
)

@Injectable
class StatusEffectOnKillService {
    @EventSubscriber(sync = true)
    fun on(event: LivingEntityDeathEvent) {
        val killer = event.killer ?: return

        EFFECTS.forEach { attribute, effect ->
            killer.getAllCustomAttributes()[attribute]?.forEach {
                val duration = it.rolls[1].asIntRoll().getValue() * 20
                val amplifier = it.rolls[0].asIntRoll().getValue() - 1
                val effectInstance = MobEffectInstance(effect, duration, amplifier, false, true, true)
                killer.addEffect(effectInstance)
            }
        }
    }
}