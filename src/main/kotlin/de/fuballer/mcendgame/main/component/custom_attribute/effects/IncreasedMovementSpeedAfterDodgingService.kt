package de.fuballer.mcendgame.main.component.custom_attribute.effects

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asDoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asIntRoll
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getAllCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.messaging.misc.LivingEntityDodgedEvent
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.addTemporaryAttributeModifier
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil.defaultJava
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes

@Injectable
class IncreasedMovementSpeedAfterDodgingService {
    private val attributeModifierIdentifierBase = "increased_movement_speed_after_dodging_"

    @EventSubscriber(sync = true)
    fun on(event: LivingEntityDodgedEvent) {
        val entity = event.entity

        val customAttributes = entity.getAllCustomAttributes()[CustomAttributeTypes.INCREASED_MOVEMENT_SPEED_AFTER_DODGING] ?: return

        customAttributes.forEach { customAttribute ->
            val movementSpeed = customAttribute.rolls[0].asDoubleRoll().getValue()
            val duration = customAttribute.rolls[1].asIntRoll().getValue() * 20
            val identifier = defaultJava(attributeModifierIdentifierBase + customAttribute.id)

            entity.addTemporaryAttributeModifier(
                EntityAttributes.GENERIC_MOVEMENT_SPEED,
                identifier,
                duration,
                movementSpeed,
                EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
            )
        }
    }
}