package de.fuballer.mcendgame.component.item.attribute

import de.fuballer.mcendgame.component.item.attribute.data.VanillaAttributeType
import de.fuballer.mcendgame.technical.Order
import de.fuballer.mcendgame.util.extension.DecimalFormatExtension.formatDouble
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import java.text.DecimalFormat

private val SIMPLE = DecimalFormat("+#.#;-#.#")
private val PRECISE = DecimalFormat("+#.##;-#.##")

object VanillaAttributeTypes { // should always only have one double roll
    @Order(1)
    val MAX_HEALTH = VanillaAttributeType(Attribute.GENERIC_MAX_HEALTH, AttributeModifier.Operation.ADD_NUMBER) { "${SIMPLE.formatDouble(it[0])} Max Health" }

    @Order(2)
    val MAX_HEALTH_INCREASE = VanillaAttributeType(Attribute.GENERIC_MAX_HEALTH, AttributeModifier.Operation.ADD_SCALAR) { "${SIMPLE.formatDouble(it[0], 100)}% Max Health" }

    @Order(3)
    val KNOCKBACK_RESISTANCE = VanillaAttributeType(Attribute.GENERIC_KNOCKBACK_RESISTANCE, AttributeModifier.Operation.ADD_NUMBER) { "${SIMPLE.formatDouble(it[0], 10)} Knockback Resistance" }

    @Order(4)
    val MOVEMENT_SPEED = VanillaAttributeType(Attribute.GENERIC_MOVEMENT_SPEED, AttributeModifier.Operation.ADD_NUMBER) { "${PRECISE.formatDouble(it[0], 100)} Movement Speed" }

    @Order(4)
    val MOVEMENT_SPEED_INCREASE = VanillaAttributeType(Attribute.GENERIC_MOVEMENT_SPEED, AttributeModifier.Operation.ADD_SCALAR) { "${SIMPLE.formatDouble(it[0], 100)}% Movement Speed" }

    @Order(5)
    val ATTACK_DAMAGE = VanillaAttributeType(Attribute.GENERIC_ATTACK_DAMAGE, AttributeModifier.Operation.ADD_NUMBER) { "${SIMPLE.formatDouble(it[0])} Attack Damage" }

    @Order(6)
    val ATTACK_KNOCKBACK = VanillaAttributeType(Attribute.GENERIC_ATTACK_KNOCKBACK, AttributeModifier.Operation.ADD_NUMBER) { "${SIMPLE.formatDouble(it[0])} Attack Knockback" }

    @Order(7)
    val ATTACK_SPEED = VanillaAttributeType(Attribute.GENERIC_ATTACK_SPEED, AttributeModifier.Operation.ADD_NUMBER) { "${PRECISE.formatDouble(it[0])} Attack Speed" }

    @Order(8)
    val ATTACK_RANGE = VanillaAttributeType(Attribute.PLAYER_ENTITY_INTERACTION_RANGE, AttributeModifier.Operation.ADD_NUMBER) { "${PRECISE.formatDouble(it[0])} Attack Range" }

    @Order(9)
    val ARMOR = VanillaAttributeType(Attribute.GENERIC_ARMOR, AttributeModifier.Operation.ADD_NUMBER) { "${SIMPLE.formatDouble(it[0])} Armor" }

    @Order(10)
    val ARMOR_TOUGHNESS = VanillaAttributeType(Attribute.GENERIC_ARMOR_TOUGHNESS, AttributeModifier.Operation.ADD_NUMBER) { "${SIMPLE.formatDouble(it[0])} Armor Toughness" }

    @Order(11)
    val LUCK = VanillaAttributeType(Attribute.GENERIC_LUCK, AttributeModifier.Operation.ADD_NUMBER) { "${SIMPLE.formatDouble(it[0])} Luck" }

    @Order(12)
    val SIZE_INCREASE = VanillaAttributeType(Attribute.GENERIC_SCALE, AttributeModifier.Operation.ADD_SCALAR) { "${SIMPLE.formatDouble(it[0], 100)}% Size" }
}