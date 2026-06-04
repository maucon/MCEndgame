package de.fuballer.mcendgame.main.component.custom_attribute.effects

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributeUtil.isLowHealth
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asIntRoll
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getAllCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.messaging.misc.ServerLivingEntitiesEveryFiveTicksEvent
import de.fuballer.mcendgame.main.util.extension.EntityExtension.applyPeriodicEffectIfTicksPassed
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects

@Injectable
class ResistanceWhenLowHealthService {
    @EventSubscriber(sync = true)
    fun on(event: ServerLivingEntitiesEveryFiveTicksEvent) {
        event.entities.forEach { entity ->
            if (!entity.isLowHealth()) return@forEach

            val attributes = entity.getAllCustomAttributes()[CustomAttributeTypes.RESISTANCE_WHEN_LOW_HEALTH] ?: return@forEach
            val duration = attributes.maxOf { it.rolls[0].asIntRoll().getValue() } * 20

            val effectInstance = MobEffectInstance(MobEffects.RESISTANCE, duration, 0, false, true, true)
            entity.applyPeriodicEffectIfTicksPassed(effectInstance)
        }
    }
}