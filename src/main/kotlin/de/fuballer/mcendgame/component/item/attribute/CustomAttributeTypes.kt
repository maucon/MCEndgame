package de.fuballer.mcendgame.component.item.attribute

import de.fuballer.mcendgame.component.item.attribute.data.CustomAttributeType
import de.fuballer.mcendgame.technical.Order
import de.fuballer.mcendgame.util.extension.DecimalFormatExtension.formatDouble
import java.text.DecimalFormat

private val SIMPLE = DecimalFormat("+#.#;-#.#")
private val SIMPLE_NO_SIGN = DecimalFormat("#.#")

object CustomAttributeTypes {
    @Order(101)
    val INCREASED_PROJECTILE_DAMAGE = CustomAttributeType { "${SIMPLE.formatDouble(it[0], 100)}% Projectile Damage" }

    @Order(102)
    val INCREASED_ARROW_VELOCITY = CustomAttributeType { "${SIMPLE.formatDouble(it[0], 100)}% Arrow Velocity" }

    @Order(103)
    val DISABLE_MELEE = CustomAttributeType { " Cannot deal Melee Damage" }

    @Order(104)
    val DODGE_CHANCE = CustomAttributeType { " ${SIMPLE_NO_SIGN.formatDouble(it[0], 100)}% Dodge" }

    @Order(105)
    val TWINFIRE_DUAL_WIELD = CustomAttributeType { " ${SIMPLE_NO_SIGN.formatDouble(it[0], 100)}% more Damage while Dual Wielding Twinfire" }

    @Order(106)
    val ABSORPTION_ON_HIGH_DAMAGE_TAKEN = CustomAttributeType { " Gain Absorption II when taking ${SIMPLE_NO_SIGN.formatDouble(it[0])} Damage" }

    @Order(107)
    val REGEN_ON_DAMAGE_TAKEN = CustomAttributeType { " Gain Regeneration II for ${SIMPLE_NO_SIGN.formatDouble(it[0])}s when damaged" }

    @Order(108)
    val STEALTH = CustomAttributeType { " Cannot be targeted by Enemies facing away" }

    @Order(109)
    val BACKSTAB = CustomAttributeType { " ${SIMPLE_NO_SIGN.formatDouble(it[0], 100)}% more Damage with Backstabs" }

    @Order(110)
    val ADDITIONAL_ARROWS = CustomAttributeType { "+${it[0]} Arrows with ${SIMPLE_NO_SIGN.formatDouble(it[1], 100)}% Damage" } // arrow count should always be divisible by 2

    @Order(111)
    val EFFECT_GAIN = CustomAttributeType { " ${SIMPLE_NO_SIGN.formatDouble(it[0], 100)}% Chance to gain a positive Effect on Kill" }

    @Order(112)
    val MORE_DAMAGE_AGAINST_FULL_LIFE = CustomAttributeType { " ${SIMPLE_NO_SIGN.formatDouble(it[0], 100)}% more Damage against Full Life Enemies" }

    @Order(113)
    val HEAL_ON_BLOCK = CustomAttributeType { "${SIMPLE.formatDouble(it[0], 100)}% Heal on Block (5s Cooldown)" }

    @Order(114)
    val INCREASED_MELEE_DAMAGE_PER_MISSING_HEART = CustomAttributeType { "${SIMPLE.formatDouble(it[0], 100)}% Melee Damage per missing Heart" }

    @Order(115)
    val SLOW_ON_HIT = CustomAttributeType { " Slow nearby Enemies for ${SIMPLE_NO_SIGN.formatDouble(it[0])}s on Hit" }

    @Order(116)
    val TAUNT = CustomAttributeType { "${SIMPLE.formatDouble(it[0], 100)}% Taunt on Hit" }

    @Order(117)
    val CRITICAL_DAMAGE = CustomAttributeType { " ${SIMPLE_NO_SIGN.formatDouble(it[0], 100)}% more Damage with Critical Hits" }

    @Order(118)
    val CRITICAL_EXECUTE = CustomAttributeType { " ${SIMPLE_NO_SIGN.formatDouble(it[0], 100)}% Health Execute on Critical Hits" }

    @Order(119)
    val RANDOMIZED_DAMAGE_TAKEN = CustomAttributeType { " Damage taken is randomized between ${SIMPLE_NO_SIGN.formatDouble(it[0], 100)}% and ${SIMPLE_NO_SIGN.formatDouble(it[1], 100)}%" }

    @Order(120)
    val NEGATIVE_EFFECT_IMMUNITY = CustomAttributeType { " Immune to negative Effects" }

    @Order(121)
    val HEALTH_RESERVATION = CustomAttributeType { "${SIMPLE.formatDouble(it[0], 100)}% Health Reservation" }

    @Order(122)
    val REDUCED_DAMAGE_TAKEN = CustomAttributeType { "-${SIMPLE_NO_SIGN.formatDouble(it[0], 100)}% Damage taken" }

    @Order(123)
    val REDUCED_PROJECTILE_DAMAGE_TAKEN = CustomAttributeType { "-${SIMPLE_NO_SIGN.formatDouble(it[0], 100)}% Projectile Damage taken" }

    @Order(124)
    val INCREASED_DAMAGE = CustomAttributeType { "${SIMPLE.formatDouble(it[0], 100)}% Damage" }
}