package de.fuballer.mcendgame.component.item.attribute

import de.fuballer.mcendgame.component.item.attribute.data.CustomAttributeType
import de.fuballer.mcendgame.util.extension.DecimalFormatExtension.formatDouble
import java.text.DecimalFormat

private val SIMPLE = DecimalFormat("#.#")

object CustomAttributeTypes {
    val INCREASED_PROJECTILE_DAMAGE = CustomAttributeType { "+${SIMPLE.formatDouble(it[0], 100)}% Projectile Damage" }

    val INCREASED_ARROW_VELOCITY = CustomAttributeType { "+${SIMPLE.formatDouble(it[0], 100)}% Arrow Velocity" }

    val DISABLE_MELEE = CustomAttributeType { " Cannot deal Melee Damage" }

    val DODGE_CHANCE = CustomAttributeType { " ${SIMPLE.formatDouble(it[0], 100)}% Dodge" }

    val TWINFIRE_DUAL_WIELD = CustomAttributeType { " ${SIMPLE.formatDouble(it[0], 100)}% more Damage while Dual Wielding Twinfire" }

    val ABSORPTION_ON_HIGH_DAMAGE_TAKEN = CustomAttributeType { " Gain Absorption II when taking ${SIMPLE.formatDouble(it[0])} Damage" }

    val REGEN_ON_DAMAGE_TAKEN = CustomAttributeType { " Gain Regeneration II for ${SIMPLE.formatDouble(it[0])}s when damaged" }

    val STEALTH = CustomAttributeType { " Cannot be targeted by Enemies facing away" }

    val BACKSTAB = CustomAttributeType { " ${SIMPLE.formatDouble(it[0], 100)}% more Damage with Backstabs" }

    val ADDITIONAL_ARROWS = CustomAttributeType { "+2 Arrows with ${SIMPLE.formatDouble(it[0], 100)}% Damage" }

    val EFFECT_GAIN = CustomAttributeType { " ${SIMPLE.formatDouble(it[0], 100)}% Chance to gain a positive Effect on Kill" }

    val MORE_DAMAGE_AGAINST_FULL_LIFE = CustomAttributeType { " ${SIMPLE.formatDouble(it[0], 100)}% more Damage against Full Life Enemies" }

    val HEAL_ON_BLOCK = CustomAttributeType { "+${SIMPLE.formatDouble(it[0], 100)}% Heal on Block (5s Cooldown)" }

    val INCREASED_DAMAGE_PER_MISSING_HEART = CustomAttributeType { "+${SIMPLE.formatDouble(it[0], 100)}% Damage per missing Heart" }

    val SLOW_ON_HIT = CustomAttributeType { " Slow nearby Enemies for ${SIMPLE.formatDouble(it[0])}s on Hit" }

    val TAUNT = CustomAttributeType { "+${SIMPLE.formatDouble(it[0], 100)}% Taunt on Hit" }

    val CRITICAL_DAMAGE = CustomAttributeType { " ${SIMPLE.formatDouble(it[0], 100)}% more Damage with Critical Hits" }

    val CRITICAL_EXECUTE = CustomAttributeType { " ${SIMPLE.formatDouble(it[0], 100)}% Health Execute on Critical Hits" }

    val RANDOMIZED_DAMAGE_TAKEN = CustomAttributeType { " Damage taken is randomized between 25% and ${SIMPLE.formatDouble(it[0], 100)}%" }

    val NEGATIVE_EFFECT_IMMUNITY = CustomAttributeType { " Immune to negative Effects" }

    val HEALTH_RESERVATION = CustomAttributeType { "+${SIMPLE.formatDouble(it[0], 100)}% Health Reservation" }

    val REDUCED_DAMAGE_TAKEN = CustomAttributeType { "-${SIMPLE.formatDouble(it[0], 100)}% Damage taken" }

    val INCREASED_DAMAGE = CustomAttributeType { "+${SIMPLE.formatDouble(it[0], 100)}% Damage" }
}