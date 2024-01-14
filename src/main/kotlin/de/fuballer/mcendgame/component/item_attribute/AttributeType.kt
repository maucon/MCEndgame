package de.fuballer.mcendgame.component.item_attribute

import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier

enum class AttributeType(
    val lore: (roll: Double) -> String,
    val vanillaAttributeType: VanillaAttributeType? = null
) {
    // vanilla
    MAX_HEALTH(
        { "+$it Maximum Health" },
        VanillaAttributeType(
            Attribute.GENERIC_MAX_HEALTH, AttributeModifier.Operation.ADD_NUMBER
        )
    ),
    KNOCKBACK_RESISTANCE(
        { "+$it Knockback Resistance" },
        VanillaAttributeType(
            Attribute.GENERIC_KNOCKBACK_RESISTANCE, AttributeModifier.Operation.ADD_NUMBER
        )
    ),
    MOVEMENT_SPEED(
        { "+${it * 100} Movement Speed" },
        VanillaAttributeType(
            Attribute.GENERIC_MOVEMENT_SPEED, AttributeModifier.Operation.ADD_NUMBER
        )
    ),
    ATTACK_DAMAGE(
        { " $it Attack Damage" },
        VanillaAttributeType(
            Attribute.GENERIC_ATTACK_DAMAGE, AttributeModifier.Operation.ADD_NUMBER
        )
    ),
    ATTACK_KNOCKBACK(
        { "+$it Attack Knockback" },
        VanillaAttributeType(
            Attribute.GENERIC_ATTACK_KNOCKBACK, AttributeModifier.Operation.ADD_NUMBER
        )
    ),
    ATTACK_SPEED(
        { " $it Attack Speed" },
        VanillaAttributeType(
            Attribute.GENERIC_ATTACK_SPEED, AttributeModifier.Operation.ADD_NUMBER
        )
    ),
    ARMOR(
        { "+$it Armor" },
        VanillaAttributeType(
            Attribute.GENERIC_ARMOR, AttributeModifier.Operation.ADD_NUMBER
        )
    ),
    ARMOR_TOUGHNESS(
        { "+$it Armor Toughness" },
        VanillaAttributeType(
            Attribute.GENERIC_ARMOR_TOUGHNESS, AttributeModifier.Operation.ADD_NUMBER
        )
    ),
    LUCK(
        { "+$it Luck" },
        VanillaAttributeType(
            Attribute.GENERIC_LUCK, AttributeModifier.Operation.ADD_NUMBER
        )
    ),

    // custom
    HEALTH_SCALED_SIZE(
        { "${it}% increased size per 2 extra health" }
    )
}