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
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player

@Injectable
class AbsorbStatFromSlainBossService {
    @EventSubscriber(sync = true)
    fun on(event: DungeonBossDeathEvent) {
        val equipment = event.killer?.getEquipment() ?: return

        var message: Component? = null
        equipment.forEach { (_, stack) ->
            val bossType = event.bossEntity.type

            val attributes = stack.getCustomAttributes().toMutableList()
            val absorbAttribute = attributes.firstOrNull { it.type == CustomAttributeTypes.ABSORB_STAT_FROM_SLAIN_BOSS } ?: return@forEach

            if (stack.hasAbsorbedBoss(bossType)) {
                if (message == null) message = AbsorbStatFromSlainBossSettings.ATTRIBUTE_ALREADY_ABSORBED_MESSAGE
                return@forEach
            }

            val newAttributes = AbsorbStatFromSlainBossSettings.getAttributeForBoss(bossType, absorbAttribute.slot) ?: return@forEach
            message = AbsorbStatFromSlainBossSettings.ATTRIBUTE_ABSORBED_MESSAGE

            val index = attributes.indexOf(absorbAttribute)
            attributes.removeAt(index)
            attributes.addAll(index, newAttributes)

            stack.addAbsorbedBoss(bossType)
            stack.setCustomAttributes(attributes, absorbAttribute.slot)
        }

        message?.let { (event.killer as? Player)?.sendSystemMessage(it) }
    }
}