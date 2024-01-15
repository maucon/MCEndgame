package de.fuballer.mcendgame.domain.attribute

import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier

enum class AttributeType(
    val lore: (roll: Double) -> String,
    val applicableAttributeType: ApplicableAttributeType? = null
) {
    // vanilla
    MAX_HEALTH(
        { "+$it Maximum Health" },
        ApplicableAttributeType(
            Attribute.GENERIC_MAX_HEALTH, AttributeModifier.Operation.ADD_NUMBER
        )
    ),
    KNOCKBACK_RESISTANCE(
        { "+$it Knockback Resistance" },
        ApplicableAttributeType(
            Attribute.GENERIC_KNOCKBACK_RESISTANCE, AttributeModifier.Operation.ADD_NUMBER
        )
    ),
    MOVEMENT_SPEED(
        { "+${it * 100} Movement Speed" },
        ApplicableAttributeType(
            Attribute.GENERIC_MOVEMENT_SPEED, AttributeModifier.Operation.ADD_NUMBER
        )
    ),
    ATTACK_DAMAGE(
        { " $it Attack Damage" },
        ApplicableAttributeType(
            Attribute.GENERIC_ATTACK_DAMAGE, AttributeModifier.Operation.ADD_NUMBER
        )
    ),
    ATTACK_KNOCKBACK(
        { "+$it Attack Knockback" },
        ApplicableAttributeType(
            Attribute.GENERIC_ATTACK_KNOCKBACK, AttributeModifier.Operation.ADD_NUMBER
        )
    ),
    ATTACK_SPEED(
        { " $it Attack Speed" },
        ApplicableAttributeType(
            Attribute.GENERIC_ATTACK_SPEED, AttributeModifier.Operation.ADD_NUMBER
        )
    ),
    ARMOR(
        { "+$it Armor" },
        ApplicableAttributeType(
            Attribute.GENERIC_ARMOR, AttributeModifier.Operation.ADD_NUMBER
        )
    ),
    ARMOR_TOUGHNESS(
        { "+$it Armor Toughness" },
        ApplicableAttributeType(
            Attribute.GENERIC_ARMOR_TOUGHNESS, AttributeModifier.Operation.ADD_NUMBER
        )
    ),
    LUCK(
        { "+$it Luck" },
        ApplicableAttributeType(
            Attribute.GENERIC_LUCK, AttributeModifier.Operation.ADD_NUMBER
        )
    ),

    // custom
    HEALTH_SCALED_SIZE(
        { "${it}% increased size per 2 extra health" }
    )
}