package de.fuballer.mcendgame.component.attribute

import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import java.text.DecimalFormat

private val DECIMAL_FORMAT = DecimalFormat("#.#")

enum class AttributeType(
    val lore: (roll: Double) -> String,
    val vanillaAttributeType: VanillaAttributeType? = null,
    val isVanillaAttributeType: Boolean = vanillaAttributeType != null
) {
    // vanilla
    MAX_HEALTH(
        { "+${DECIMAL_FORMAT.format(it)} Max Health" },
        VanillaAttributeType(
            Attribute.GENERIC_MAX_HEALTH, AttributeModifier.Operation.ADD_NUMBER
        )
    ),
    MAX_HEALTH_INCREASE(
        { "+${DECIMAL_FORMAT.format(it * 100)}% Max Health" },
        VanillaAttributeType(
            Attribute.GENERIC_MAX_HEALTH, AttributeModifier.Operation.ADD_SCALAR
        )
    ),
    KNOCKBACK_RESISTANCE(
        { "+${DECIMAL_FORMAT.format(it * 10)} Knockback Resistance" },
        VanillaAttributeType(
            Attribute.GENERIC_KNOCKBACK_RESISTANCE, AttributeModifier.Operation.ADD_NUMBER
        )
    ),
    MOVEMENT_SPEED(
        { "+${DECIMAL_FORMAT.format(it * 100)} Movement Speed" },
        VanillaAttributeType(
            Attribute.GENERIC_MOVEMENT_SPEED, AttributeModifier.Operation.ADD_NUMBER
        )
    ),
    ATTACK_DAMAGE(
        { "+${DECIMAL_FORMAT.format(it)} Attack Damage" },
        VanillaAttributeType(
            Attribute.GENERIC_ATTACK_DAMAGE, AttributeModifier.Operation.ADD_NUMBER
        )
    ),
    ATTACK_KNOCKBACK(
        { "+${DECIMAL_FORMAT.format(it)} Attack Knockback" },
        VanillaAttributeType(
            Attribute.GENERIC_ATTACK_KNOCKBACK, AttributeModifier.Operation.ADD_NUMBER
        )
    ),
    ATTACK_SPEED(
        { "+${DECIMAL_FORMAT.format(it)} Attack Speed" },
        VanillaAttributeType(
            Attribute.GENERIC_ATTACK_SPEED, AttributeModifier.Operation.ADD_NUMBER
        )
    ),
    ARMOR(
        { "+${DECIMAL_FORMAT.format(it)} Armor" },
        VanillaAttributeType(
            Attribute.GENERIC_ARMOR, AttributeModifier.Operation.ADD_NUMBER
        )
    ),
    ARMOR_TOUGHNESS(
        { "+${DECIMAL_FORMAT.format(it)} Armor Toughness" },
        VanillaAttributeType(
            Attribute.GENERIC_ARMOR_TOUGHNESS, AttributeModifier.Operation.ADD_NUMBER
        )
    ),
    LUCK(
        { "+${DECIMAL_FORMAT.format(it)} Luck" },
        VanillaAttributeType(
            Attribute.GENERIC_LUCK, AttributeModifier.Operation.ADD_NUMBER
        )
    ),
    SIZE_INCREASE(
        { "+${DECIMAL_FORMAT.format(it * 100)}% Size" },
        VanillaAttributeType(
            Attribute.GENERIC_LUCK, AttributeModifier.Operation.ADD_SCALAR // FIXME when size attribute gets implemented
        )
    ),

    // custom
    HEALTH_SCALED_SIZE({ " ${DECIMAL_FORMAT.format(it)}% increased size per 2 extra health" }),

    DISABLE_MELEE({ " Cannot deal melee damage" }),

    DODGE_CHANCE({ " ${DECIMAL_FORMAT.format(it * 100)}% chance to dodge hits" }),

    TWINFIRE_DUAL_WIELD({ " ${DECIMAL_FORMAT.format(it * 100)}% more damage while dual wielding Twinfire" }),

    ABSORPTION_ON_HIGH_DAMAGE_TAKEN({ " When taking at least ${DECIMAL_FORMAT.format(it)} damage, gain Absorption II" }),

    STEALTH({ " Cannot be targeted by enemies facing away" }),

    BACKSTAB({ " ${DECIMAL_FORMAT.format(it * 100)}% more damage with attacks from behind" }),

    ADDITIONAL_ARROWS({ " Shoot 2 additional arrows dealing ${DECIMAL_FORMAT.format(it * 100)}% damage" }),

    EFFECT_GAIN({ " ${DECIMAL_FORMAT.format(it * 100)}% chance to gain a random positive effect on kill" }),

    MORE_DAMAGE_AGAINST_FULL_LIFE({ " ${DECIMAL_FORMAT.format(it * 100)}% more damage against full life enemies" }),

    HEAL_ON_BLOCK({ " Heal ${DECIMAL_FORMAT.format(it)} health on block, with a cooldown of 7 seconds" }),

    INC_DAMAGE_PER_MISSING_HEART({ " ${DECIMAL_FORMAT.format(it * 100)}% increased damage per missing heart" }),

    SLOW_ON_HIT({ " Affect nearby enemies with Slowness II for ${DECIMAL_FORMAT.format(it)} seconds on hit" }),

    TAUNT({ "+${DECIMAL_FORMAT.format(it * 100)}% chance to taunt an enemy on hit" }),
}