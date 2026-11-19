package de.fuballer.mcendgame.main.component.custom_attribute.effects.absorb_stat_from_boss

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.setCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.messaging.dungeon.DungeonBossDeathEvent
import de.fuballer.mcendgame.main.util.extension.EntityExtension.getEquipment
import de.fuballer.mcendgame.main.util.extension.ItemStackExtension.addAbsorbedBoss
import de.fuballer.mcendgame.main.util.extension.ItemStackExtension.hasAbsorbedBoss
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber

@Injectable
class AbsorbStatFromSlainBossService {
    @EventSubscriber(sync = true)
    fun on(event: DungeonBossDeathEvent) {
        val equipment = event.killer?.getEquipment() ?: return

        equipment.forEach { (_, stack) ->
            val bossType = event.bossEntity.type
            if (stack.hasAbsorbedBoss(bossType)) return@forEach

            val attributes = stack.getCustomAttributes().toMutableList()
            val absorbAttribute = attributes.firstOrNull { it.type == CustomAttributeTypes.ABSORB_STAT_FROM_SLAIN_BOSS } ?: return@forEach
            val newAttributes = AbsorbStatFromSlainBossSettings.getAttributeForBoss(bossType, absorbAttribute.slot) ?: return@forEach

            val index = attributes.indexOf(absorbAttribute)
            attributes.removeAt(index)
            attributes.addAll(index, newAttributes)

            stack.addAbsorbedBoss(bossType)
            stack.setCustomAttributes(attributes, absorbAttribute.slot)
        }
    }
}