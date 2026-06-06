package de.fuballer.mcendgame.main.component.custom_attribute.effects

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asIntRoll
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getAllCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.messaging.misc.LivingEntityDamagedEvent
import de.fuballer.mcendgame.main.util.extension.EntityExtension.isEnemy
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects

@Injectable
class RegenerationWhenHitByEnemyService {
    @EventSubscriber(sync = true)
    fun on(event: LivingEntityDamagedEvent) {
        val damaged = event.damaged
        val attacker = event.damageSource.entity ?: return
        if (!damaged.isEnemy(attacker)) return

        val attributes = damaged.getAllCustomAttributes()[CustomAttributeTypes.REGENERATION_WHEN_HIT_BY_ENEMY] ?: return

        attributes.forEach {
            val duration = it.rolls[1].asIntRoll().getValue() * 20
            val amplifier = it.rolls[0].asIntRoll().getValue() - 1
            val effectInstance = MobEffectInstance(MobEffects.REGENERATION, duration, amplifier, false, true, true)
            damaged.addEffect(effectInstance)
        }
    }
}