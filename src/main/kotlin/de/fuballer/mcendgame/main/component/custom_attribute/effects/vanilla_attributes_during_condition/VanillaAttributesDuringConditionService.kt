package de.fuballer.mcendgame.main.component.custom_attribute.effects.vanilla_attributes_during_condition

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asDoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getAllCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttributeType
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.initializer.Initializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.minecraft.core.Holder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeInstance
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import kotlin.math.abs

@Injectable
class VanillaAttributesDuringConditionService {
    @Initializer
    fun register() {
        ServerTickEvents.END_LEVEL_TICK.register { world ->
            world.allEntities.forEach { entity ->
                if (entity.tickCount % VanillaAttributesDuringConditionSettings.UPDATE_INTERVAL != 0) return@forEach
                val livingEntity = entity as? LivingEntity ?: return@forEach
                updateVanillaAttributesWhileCondition(livingEntity)
            }
        }
    }

    private fun updateVanillaAttributesWhileCondition(entity: LivingEntity) {
        VanillaAttributesDuringConditionSettings.ConditionedAttributes.forEach { (condition, attributes) ->
            val conditionMet = condition(entity)
            attributes.forEach {
                updateVanillaAttributeWhileCondition(entity, conditionMet, it.customAttributeType, it.vanillaAttribute, it.operation)
            }
        }
    }

    private fun updateVanillaAttributeWhileCondition(
        entity: LivingEntity,
        conditionMet: Boolean,
        customAttributeType: CustomAttributeType,
        vanillaAttribute: Holder<Attribute>,
        operation: AttributeModifier.Operation,
    ) {
        val attributeInstance: AttributeInstance = entity.getAttribute(vanillaAttribute) ?: return
        val attributeTypeKey = customAttributeType.key

        if (!conditionMet) {
            removeModifiers(attributeInstance, attributeTypeKey)
            return
        }

        val allAttributes: Map<CustomAttributeType, List<CustomAttribute>> = entity.getAllCustomAttributes()
        val attributes = allAttributes[customAttributeType]
        if (attributes.isNullOrEmpty()) {
            removeModifiers(attributeInstance, attributeTypeKey)
            return
        }

        val activeKeys = attributes.map { "${attributeTypeKey}_${it.id}" }.toSet()
        attributeInstance.modifiers
            .filter { it.id.path.startsWith("${attributeTypeKey}_") }
            .filter { it.id.path !in activeKeys }
            .forEach { attributeInstance.removeModifier(it) }

        attributes.forEach {
            val key = "${attributeTypeKey}_${it.id}"
            val identifier = IdentifierUtil.default(key)

            val value = it.rolls[0].asDoubleRoll().getValue()

            val existingModifier = attributeInstance.getModifier(identifier)
            if (existingModifier != null && abs(existingModifier.amount() - value) < 0.001) return@forEach

            val modifier = AttributeModifier(identifier, value, operation)
            attributeInstance.removeModifier(identifier)
            attributeInstance.addTransientModifier(modifier)
        }
    }

    private fun removeModifiers(
        attributeInstance: AttributeInstance,
        key: String,
    ) {
        attributeInstance.modifiers
            .filter { it.id.path.startsWith(key) }
            .forEach { attributeInstance.removeModifier(it) }
    }
}