package de.fuballer.mcendgame.main.component.custom_attribute.effects.projectile

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getAllCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.messaging.misc.EntityShotArrowEvent
import de.fuballer.mcendgame.main.util.extension.mixin.PersistentProjectileEntityMixinExtension.setPierceLevel
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber

@Injectable
class PierceAllService {
    @EventSubscriber(sync = true)
    fun on(event: EntityShotArrowEvent) {
        val pierceAttributes = event.owner.getAllCustomAttributes()[CustomAttributeTypes.PIERCE_ALL] ?: return
        if (pierceAttributes.isEmpty()) return
        event.arrow.setPierceLevel(Byte.MAX_VALUE)
    }
}