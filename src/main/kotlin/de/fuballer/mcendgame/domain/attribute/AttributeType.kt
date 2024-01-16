package de.fuballer.mcendgame.domain.attribute

import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import java.text.DecimalFormat

private val DECIMAL_FORMAT = DecimalFormat("#.##")

enum class AttributeType(
    val lore: (roll: Double) -> String,
    val applicableAttributeType: ApplicableAttributeType? = null
) {
    // vanilla
    MAX_HEALTH(
        { "+${DECIMAL_FORMAT.format(it)} Max Health" },
        ApplicableAttributeType(
            Attribute.GENERIC_MAX_HEALTH, AttributeModifier.Operation.ADD_NUMBER
        )
    ),
    KNOCKBACK_RESISTANCE(
        { "+${DECIMAL_FORMAT.format(it * 10)} Knockback Resistance" },
        ApplicableAttributeType(
            Attribute.GENERIC_KNOCKBACK_RESISTANCE, AttributeModifier.Operation.ADD_NUMBER
        )
    ),
    MOVEMENT_SPEED(
        { "+${DECIMAL_FORMAT.format(it * 100)} Movement Speed" },
        ApplicableAttributeType(
            Attribute.GENERIC_MOVEMENT_SPEED, AttributeModifier.Operation.ADD_NUMBER
        )
    ),
    ATTACK_DAMAGE(
        { "+${DECIMAL_FORMAT.format(it)} Attack Damage" },
        ApplicableAttributeType(
            Attribute.GENERIC_ATTACK_DAMAGE, AttributeModifier.Operation.ADD_NUMBER
        )
    ),
    ATTACK_KNOCKBACK(
        { "+${DECIMAL_FORMAT.format(it)} Attack Knockback" },
        ApplicableAttributeType(
            Attribute.GENERIC_ATTACK_KNOCKBACK, AttributeModifier.Operation.ADD_NUMBER
        )
    ),
    ATTACK_SPEED(
        { "+${DECIMAL_FORMAT.format(it)} Attack Speed" },
        ApplicableAttributeType(
            Attribute.GENERIC_ATTACK_SPEED, AttributeModifier.Operation.ADD_NUMBER
        )
    ),
    ARMOR(
        { "+${DECIMAL_FORMAT.format(it)} Armor" },
        ApplicableAttributeType(
            Attribute.GENERIC_ARMOR, AttributeModifier.Operation.ADD_NUMBER
        )
    ),
    ARMOR_TOUGHNESS(
        { "+${DECIMAL_FORMAT.format(it)} Armor Toughness" },
        ApplicableAttributeType(
            Attribute.GENERIC_ARMOR_TOUGHNESS, AttributeModifier.Operation.ADD_NUMBER
        )
    ),
    LUCK(
        { "+${DECIMAL_FORMAT.format(it)} Luck" },
        ApplicableAttributeType(
            Attribute.GENERIC_LUCK, AttributeModifier.Operation.ADD_NUMBER
        )
    ),

    // custom
    HEALTH_SCALED_SIZE(
        { "${DECIMAL_FORMAT.format(it)}% increased size per 2 extra health" }
    ),
    HEALTH_SCALED_SPEED(
        { "${DECIMAL_FORMAT.format(it)}% increased speed per 2 extra health" }
    )
}