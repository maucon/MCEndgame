package de.fuballer.mcendgame.component.item.attribute

import de.fuballer.mcendgame.component.item.attribute.data.VanillaAttributeType
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import java.text.DecimalFormat

private val DECIMAL_FORMAT_BASE = DecimalFormat("#.#")
private val DECIMAL_FORMAT_PRECISE = DecimalFormat("#.##")

enum class AttributeType(
    val lore: (roll: Double) -> String,
    val vanillaAttributeType: VanillaAttributeType? = null,
    val isVanillaAttributeType: Boolean = vanillaAttributeType != null
) {
    // vanilla
    MAX_HEALTH(
        { "+${DECIMAL_FORMAT_BASE.format(it)} Max Health" },
        VanillaAttributeType(
            Attribute.GENERIC_MAX_HEALTH, AttributeModifier.Operation.ADD_NUMBER
        )
    ),
    MAX_HEALTH_INCREASE(
        { "+${DECIMAL_FORMAT_BASE.format(it * 100)}% Max Health" },
        VanillaAttributeType(
            Attribute.GENERIC_MAX_HEALTH, AttributeModifier.Operation.ADD_SCALAR
        )
    ),
    KNOCKBACK_RESISTANCE(
        { "+${DECIMAL_FORMAT_BASE.format(it * 10)} Knockback Resistance" },
        VanillaAttributeType(
            Attribute.GENERIC_KNOCKBACK_RESISTANCE, AttributeModifier.Operation.ADD_NUMBER
        )
    ),
    MOVEMENT_SPEED(
        { "+${DECIMAL_FORMAT_PRECISE.format(it * 100)} Movement Speed" },
        VanillaAttributeType(
            Attribute.GENERIC_MOVEMENT_SPEED, AttributeModifier.Operation.ADD_NUMBER
        )
    ),
    ATTACK_DAMAGE(
        { "+${DECIMAL_FORMAT_BASE.format(it)} Attack Damage" },
        VanillaAttributeType(
            Attribute.GENERIC_ATTACK_DAMAGE, AttributeModifier.Operation.ADD_NUMBER
        )
    ),
    ATTACK_KNOCKBACK(
        { "+${DECIMAL_FORMAT_BASE.format(it)} Attack Knockback" },
        VanillaAttributeType(
            Attribute.GENERIC_ATTACK_KNOCKBACK, AttributeModifier.Operation.ADD_NUMBER
        )
    ),
    ATTACK_SPEED(
        { "+${DECIMAL_FORMAT_PRECISE.format(it)} Attack Speed" },
        VanillaAttributeType(
            Attribute.GENERIC_ATTACK_SPEED, AttributeModifier.Operation.ADD_NUMBER
        )
    ),
    ATTACK_RANGE(
        { "+${DECIMAL_FORMAT_PRECISE.format(it)} Attack Range" },
        VanillaAttributeType(
            Attribute.PLAYER_ENTITY_INTERACTION_RANGE, AttributeModifier.Operation.ADD_NUMBER
        )
    ),
    ARMOR(
        { "+${DECIMAL_FORMAT_BASE.format(it)} Armor" },
        VanillaAttributeType(
            Attribute.GENERIC_ARMOR, AttributeModifier.Operation.ADD_NUMBER
        )
    ),
    ARMOR_TOUGHNESS(
        { "+${DECIMAL_FORMAT_BASE.format(it)} Armor Toughness" },
        VanillaAttributeType(
            Attribute.GENERIC_ARMOR_TOUGHNESS, AttributeModifier.Operation.ADD_NUMBER
        )
    ),
    LUCK(
        { "+${DECIMAL_FORMAT_BASE.format(it)} Luck" },
        VanillaAttributeType(
            Attribute.GENERIC_LUCK, AttributeModifier.Operation.ADD_NUMBER
        )
    ),
    SIZE_INCREASE(
        { "+${DECIMAL_FORMAT_BASE.format(it * 100)}% Size" },
        VanillaAttributeType(
            Attribute.GENERIC_SCALE, AttributeModifier.Operation.ADD_SCALAR
        )
    ),

    INCREASED_PROJECTILE_DAMAGE({ "+${DECIMAL_FORMAT_BASE.format(it * 100)}% Projectile Damage" }),

    INCREASED_ARROW_VELOCITY({ "+${DECIMAL_FORMAT_BASE.format(it * 100)}% Arrow Velocity" }),

    DISABLE_MELEE({ " Cannot deal Melee Damage" }),

    DODGE_CHANCE({ " ${DECIMAL_FORMAT_BASE.format(it * 100)}% Dodge" }),

    TWINFIRE_DUAL_WIELD({ " ${DECIMAL_FORMAT_BASE.format(it * 100)}% more Damage while Dual Wielding Twinfire" }),

    ABSORPTION_ON_HIGH_DAMAGE_TAKEN({ " Gain Absorption II when taking ${DECIMAL_FORMAT_BASE.format(it)} Damage" }),

    REGEN_ON_DAMAGE_TAKEN({ " Gain Regeneration II for ${DECIMAL_FORMAT_BASE.format(it)}s when damaged" }),

    STEALTH({ " Cannot be targeted by Enemies facing away" }),

    BACKSTAB({ " ${DECIMAL_FORMAT_BASE.format(it * 100)}% more Damage with Backstabs" }),

    ADDITIONAL_ARROWS({ " +2 Arrows with ${DECIMAL_FORMAT_BASE.format(it * 100)}% Damage" }),

    EFFECT_GAIN({ " ${DECIMAL_FORMAT_BASE.format(it * 100)}% Chance to gain a positive Effect on Kill" }),

    MORE_DAMAGE_AGAINST_FULL_LIFE({ " ${DECIMAL_FORMAT_BASE.format(it * 100)}% more Damage against Full Life Enemies" }),

    HEAL_ON_BLOCK({ "+${DECIMAL_FORMAT_BASE.format(it * 100)}% Heal on Block (5s Cooldown)" }),

    INCREASED_DAMAGE_PER_MISSING_HEART({ "+${DECIMAL_FORMAT_BASE.format(it * 100)}% Damage per missing Heart" }),

    SLOW_ON_HIT({ " Slow nearby Enemies for ${DECIMAL_FORMAT_BASE.format(it)}s on Hit" }),

    TAUNT({ "+${DECIMAL_FORMAT_BASE.format(it * 100)}% Taunt on Hit" }),

    CRITICAL_DAMAGE({ " ${DECIMAL_FORMAT_BASE.format(it * 100)}% more Damage with Critical Hits" }),

    CRITICAL_EXECUTE({ " ${DECIMAL_FORMAT_BASE.format(it * 100)}% Health Execute on Critical Hits" }),

    RANDOMIZED_DAMAGE_TAKEN({ " Damage taken is randomized between 25% and ${DECIMAL_FORMAT_BASE.format(it * 100)}%" }),

    NEGATIVE_EFFECT_IMMUNITY({ " Immune to negative Effects" }),

    HEALTH_RESERVATION({ "+${DECIMAL_FORMAT_BASE.format(it * 100)}% Health Reservation" }),

    REDUCED_DAMAGE_TAKEN({ "-${DECIMAL_FORMAT_BASE.format(it * 100)}% Damage taken" }),

    INCREASED_DAMAGE({ "+${DECIMAL_FORMAT_BASE.format(it * 100)}% Damage" }),
}