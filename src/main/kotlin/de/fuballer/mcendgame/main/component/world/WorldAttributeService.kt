package de.fuballer.mcendgame.main.component.world

import de.fuballer.mcendgame.main.component.custom_attribute.data.VanillaAttributeType
import de.fuballer.mcendgame.main.messaging.misc.PlayerAfterDimensionChangeEvent
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.resetWorldAttributesUpdate
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.getVanillaTypeAttributesHistory
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber

@Injectable
class WorldAttributeService {
    @EventSubscriber(sync = true)
    fun on(event: PlayerAfterDimensionChangeEvent) {
        val player = event.player
        player.resetWorldAttributesUpdate()

        val history = event.oldWorld.getVanillaTypeAttributesHistory(player)
        history.filter { it.action == WorldAttributeAction.ADD }
            .forEach {
                val attribute = it.attribute
                val type = attribute.type as? VanillaAttributeType ?: return@forEach
                val attributeInstance = player.getAttribute(type.attribute) ?: return@forEach
                val identifier = IdentifierUtil.defaultCustomAttribute(attribute)
                attributeInstance.removeModifier(identifier)
            }
    }
}