package de.fuballer.mcendgame.main.component.custom_attribute.effects

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asIntRoll
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getAllCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.messaging.misc.LivingEntityDamagedEvent
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity

@Injectable
class SlownessOnHitService {
    @EventSubscriber(sync = true)
    fun on(event: LivingEntityDamagedEvent) {
        val attacker = event.damageSource.entity as? LivingEntity ?: return

        val attributes = attacker.getAllCustomAttributes()[CustomAttributeTypes.SLOWNESS_ON_HIT] ?: return

        val damaged = event.damaged
        attributes.forEach {
            val duration = it.rolls[1].asIntRoll().getValue() * 20
            val amplifier = it.rolls[0].asIntRoll().getValue() - 1
            val effectInstance = MobEffectInstance(MobEffects.SLOWNESS, duration, amplifier, false, true, true)
            damaged.addEffect(effectInstance)
        }
    }
}