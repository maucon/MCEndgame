package de.fuballer.mcendgame.main.component.custom_attribute.effects.spell

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getAllCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.damage.custom_type.CustomDamageTypes
import de.fuballer.mcendgame.main.messaging.misc.LivingEntityDamagedEvent
import de.fuballer.mcendgame.main.util.extension.DamageTypeExtension.isOf
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.world.entity.LivingEntity

private const val IGNITE_TICKS = 80

@Injectable
class SpellIgniteService {
    @EventSubscriber(sync = true)
    fun on(event: LivingEntityDamagedEvent) {
        if (!event.damageSource.type().isOf(CustomDamageTypes.SPELL)) return
        val attacker = event.damageSource.entity as? LivingEntity ?: return
        if (!attacker.getAllCustomAttributes().containsKey(CustomAttributeTypes.SPELL_IGNITE)) return
        event.victim.igniteForTicks(IGNITE_TICKS)
    }
}