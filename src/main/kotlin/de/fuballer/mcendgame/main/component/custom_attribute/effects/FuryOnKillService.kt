package de.fuballer.mcendgame.main.component.custom_attribute.effects

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getAllCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.status_effect.CustomStatusEffects
import de.fuballer.mcendgame.main.messaging.misc.LivingEntityDeathEvent
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.world.effect.MobEffectInstance
import kotlin.math.min

@Injectable
class FuryOnKillService {
    @EventSubscriber(sync = true)
    fun on(event: LivingEntityDeathEvent) {
        val killer = event.killer ?: return
        val attributes = killer.getAllCustomAttributes()[CustomAttributeTypes.FURY_ON_KILL] ?: return

        val currentFury = killer.getEffect(CustomStatusEffects.FURY)?.amplifier ?: -1
        val newFury = min(currentFury + attributes.size, 9)

        killer.addEffect(MobEffectInstance(CustomStatusEffects.FURY, 199, newFury, false, true, true))
    }
}