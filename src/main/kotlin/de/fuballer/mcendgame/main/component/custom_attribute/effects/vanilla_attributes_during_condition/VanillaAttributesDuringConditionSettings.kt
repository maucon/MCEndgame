package de.fuballer.mcendgame.main.component.custom_attribute.effects.vanilla_attributes_during_condition

import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttributeType
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.util.extension.EntityExtension.isDualWielding
import net.minecraft.core.Holder
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes

object VanillaAttributesDuringConditionSettings {
    const val UPDATE_INTERVAL = 10

    val ConditionedAttributes = listOf(
        ConditionedAttributesData(
            { it.isDualWielding() },
            CustomToVanillaAttribute(
                CustomAttributeTypes.MORE_ATTACK_SPEED_DUAL_WIELD,
                Attributes.ATTACK_SPEED,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL,
            ),
        ),
        ConditionedAttributesData(
            { it.hasEffect(MobEffects.POISON) },
            CustomToVanillaAttribute(
                CustomAttributeTypes.INCREASED_MOVEMENT_SPEED_WHILE_POISONED,
                Attributes.MOVEMENT_SPEED,
                AttributeModifier.Operation.ADD_MULTIPLIED_BASE,
            ),
            CustomToVanillaAttribute(
                CustomAttributeTypes.INCREASED_ATTACK_DAMAGE_WHILE_POISONED,
                Attributes.ATTACK_DAMAGE,
                AttributeModifier.Operation.ADD_MULTIPLIED_BASE,
            ),
        ),
        ConditionedAttributesData(
            { it.hasEffect(MobEffects.WITHER) },
            CustomToVanillaAttribute(
                CustomAttributeTypes.ARMOR_WHILE_WITHERED,
                Attributes.ARMOR,
                AttributeModifier.Operation.ADD_VALUE,
            ),
            CustomToVanillaAttribute(
                CustomAttributeTypes.INCREASED_ATTACK_DAMAGE_WHILE_WITHERED,
                Attributes.ATTACK_DAMAGE,
                AttributeModifier.Operation.ADD_MULTIPLIED_BASE,
            ),
        ),
    )

    data class ConditionedAttributesData(
        val condition: (LivingEntity) -> Boolean,
        val attributes: List<CustomToVanillaAttribute>,
    ) {
        constructor(
            condition: (LivingEntity) -> Boolean,
            vararg attribute: CustomToVanillaAttribute,
        ) : this(condition, attribute.toList())
    }

    data class CustomToVanillaAttribute(
        val customAttributeType: CustomAttributeType,
        val vanillaAttribute: Holder<Attribute>,
        val operation: AttributeModifier.Operation,
    )
}