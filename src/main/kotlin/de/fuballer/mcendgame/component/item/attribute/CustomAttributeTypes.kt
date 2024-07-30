package de.fuballer.mcendgame.component.item.attribute

import de.fuballer.mcendgame.component.item.attribute.data.CustomAttributeType
import de.fuballer.mcendgame.technical.Order
import de.fuballer.mcendgame.util.extension.DecimalFormatExtension.formatDouble
import java.text.DecimalFormat

private val SIMPLE = DecimalFormat("+#.#;-#.#")
private val SIMPLE_NO_SIGN = DecimalFormat("#.#")

object CustomAttributeTypes {
    @Order(1)
    val INCREASED_PROJECTILE_DAMAGE = CustomAttributeType { "${SIMPLE.formatDouble(it[0], 100)}% Projectile Damage" }

    @Order(2)
    val INCREASED_ARROW_VELOCITY = CustomAttributeType { "${SIMPLE.formatDouble(it[0], 100)}% Arrow Velocity" }

    @Order(3)
    val DISABLE_MELEE = CustomAttributeType { " Cannot deal Melee Damage" }

    @Order(4)
    val DODGE_CHANCE = CustomAttributeType { " ${SIMPLE_NO_SIGN.formatDouble(it[0], 100)}% Dodge" }

    @Order(5)
    val TWINFIRE_DUAL_WIELD = CustomAttributeType { " ${SIMPLE_NO_SIGN.formatDouble(it[0], 100)}% more Damage while Dual Wielding Twinfire" }

    @Order(6)
    val ABSORPTION_ON_HIGH_DAMAGE_TAKEN = CustomAttributeType { " Gain Absorption II when taking ${SIMPLE.formatDouble(it[0])} Damage" }

    @Order(7)
    val REGEN_ON_DAMAGE_TAKEN = CustomAttributeType { " Gain Regeneration II for ${SIMPLE.formatDouble(it[0])}s when damaged" }

    @Order(8)
    val STEALTH = CustomAttributeType { " Cannot be targeted by Enemies facing away" }

    @Order(9)
    val BACKSTAB = CustomAttributeType { " ${SIMPLE_NO_SIGN.formatDouble(it[0], 100)}% more Damage with Backstabs" }

    @Order(10)
    val ADDITIONAL_ARROWS = CustomAttributeType { "+${it[0]} Arrows with ${SIMPLE_NO_SIGN.formatDouble(it[1], 100)}% Damage" } // arrow count should always be divisible by 2

    @Order(11)
    val EFFECT_GAIN = CustomAttributeType { " ${SIMPLE_NO_SIGN.formatDouble(it[0], 100)}% Chance to gain a positive Effect on Kill" }

    @Order(12)
    val MORE_DAMAGE_AGAINST_FULL_LIFE = CustomAttributeType { " ${SIMPLE_NO_SIGN.formatDouble(it[0], 100)}% more Damage against Full Life Enemies" }

    @Order(13)
    val HEAL_ON_BLOCK = CustomAttributeType { "${SIMPLE.formatDouble(it[0], 100)}% Heal on Block (5s Cooldown)" }

    @Order(14)
    val INCREASED_DAMAGE_PER_MISSING_HEART = CustomAttributeType { "${SIMPLE.formatDouble(it[0], 100)}% Damage per missing Heart" }

    @Order(15)
    val SLOW_ON_HIT = CustomAttributeType { " Slow nearby Enemies for ${SIMPLE.formatDouble(it[0])}s on Hit" }

    @Order(16)
    val TAUNT = CustomAttributeType { "${SIMPLE.formatDouble(it[0], 100)}% Taunt on Hit" }

    @Order(17)
    val CRITICAL_DAMAGE = CustomAttributeType { " ${SIMPLE_NO_SIGN.formatDouble(it[0], 100)}% more Damage with Critical Hits" }

    @Order(18)
    val CRITICAL_EXECUTE = CustomAttributeType { " ${SIMPLE_NO_SIGN.formatDouble(it[0], 100)}% Health Execute on Critical Hits" }

    @Order(19)
    val RANDOMIZED_DAMAGE_TAKEN = CustomAttributeType { " Damage taken is randomized between ${SIMPLE_NO_SIGN.formatDouble(it[0], 100)}% and ${SIMPLE_NO_SIGN.formatDouble(it[1], 100)}%" }

    @Order(20)
    val NEGATIVE_EFFECT_IMMUNITY = CustomAttributeType { " Immune to negative Effects" }

    @Order(21)
    val HEALTH_RESERVATION = CustomAttributeType { "${SIMPLE.formatDouble(it[0], 100)}% Health Reservation" }

    @Order(22)
    val REDUCED_DAMAGE_TAKEN = CustomAttributeType { "-${SIMPLE_NO_SIGN.formatDouble(it[0], 100)}% Damage taken" }

    @Order(23)
    val INCREASED_DAMAGE = CustomAttributeType { "${SIMPLE.formatDouble(it[0], 100)}% Damage" }

    @Order(24)
    val TEST = CustomAttributeType { "${SIMPLE.formatDouble(it[0], 100)} ${it[1]} ${it[2]}" }
}