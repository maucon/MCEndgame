package de.fuballer.mcendgame.util

import de.fuballer.mcendgame.component.corruption.CorruptionSettings
import de.fuballer.mcendgame.component.statitem.StatItemSettings
import de.fuballer.mcendgame.domain.equipment.Equipment
import org.bukkit.ChatColor
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.text.DecimalFormat
import java.util.*
import kotlin.math.abs

object AttributeUtil {
    private val decimalFormat = DecimalFormat("#.##")

    fun getActualAttributeValue(attribute: Attribute, value: Double): Double {
        if (attribute == Attribute.GENERIC_ATTACK_DAMAGE) return value - 1
        if (attribute == Attribute.GENERIC_ATTACK_SPEED) return value - 4

        return value
    }

    fun setAttributeLore(item: ItemStack, corrupted: Boolean) {
        val meta = item.itemMeta ?: return
        val equipment = StatItemSettings.MATERIAL_TO_EQUIPMENT[item.type] ?: return

        meta.lore = getAttributeLore(equipment, meta, corrupted)
        item.itemMeta = meta
    }

    fun getAttributeLore(
        equipment: Equipment,
        itemMeta: ItemMeta,
        corrupted: Boolean
    ): List<String> {
        var attributeLore = mutableListOf<String>()
        attributeLore.add("")
        attributeLore.add(equipment.lore)

        val enchantDamage = if (itemMeta.hasEnchant(Enchantment.DAMAGE_ALL)) 0.5 else 0.0
        val damageIncrease: Double = enchantDamage + 0.5 * itemMeta.getEnchantLevel(Enchantment.DAMAGE_ALL)

        val baseAttributes = equipment.baseAttributes
        val baseAttributesMap: MutableMap<Attribute, Double> = EnumMap(Attribute::class.java)

        baseAttributes.forEach { (attribute, value) ->
            val attributeValue = value + if (attribute == Attribute.GENERIC_ATTACK_DAMAGE) damageIncrease else 0.0
            attributeLore.add(getAttributeLine(attribute, attributeValue, true))
            baseAttributesMap[attribute] = value
        }

        val attributes = itemMeta.attributeModifiers
        attributes?.also {
            for (attribute in attributes.keySet()) {
                var ignoredBase = false
                val attributeModifiers = attributes[attribute].toList()

                for (attributeModifier in attributeModifiers) {
                    if (baseAttributesMap.containsKey(attribute)
                        && abs(getActualAttributeValue(attribute, baseAttributesMap[attribute]!!) - attributeModifier.amount) < 0.0001 // TODO what the fuck
                        && !ignoredBase
                    ) {
                        ignoredBase = true
                        continue
                    }

                    attributeLore.add(getAttributeLine(attribute, attributeModifier.amount, false))
                }
            }
        }

        if (attributeLore.size <= 2) {
            attributeLore = mutableListOf()
        }
        if (corrupted) {
            attributeLore.addAll(CorruptionSettings.CORRUPTION_TAG_LORE)
        }

        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
        return attributeLore
    }

    fun getDisplayedValue(attribute: Attribute, value: Double) =
        when (attribute) {
            Attribute.GENERIC_KNOCKBACK_RESISTANCE -> value * 10
            Attribute.GENERIC_MOVEMENT_SPEED -> value * 100
            else -> value
        }

    fun getAttributeAsString(attribute: Attribute) =
        when (attribute) {
            Attribute.GENERIC_ATTACK_DAMAGE -> "Attack Damage"
            Attribute.GENERIC_ARMOR -> "Armor"
            Attribute.GENERIC_ARMOR_TOUGHNESS -> "Armor Toughness"
            Attribute.GENERIC_ATTACK_KNOCKBACK -> "Attack Knockback"
            Attribute.GENERIC_ATTACK_SPEED -> "Attack Speed"
            Attribute.GENERIC_FLYING_SPEED -> "Flying Speed"
            Attribute.GENERIC_FOLLOW_RANGE -> "Follow Range"
            Attribute.GENERIC_KNOCKBACK_RESISTANCE -> "Knockback Resistance"
            Attribute.GENERIC_LUCK -> "Luck"
            Attribute.GENERIC_MAX_HEALTH -> "Max Health"
            Attribute.GENERIC_MOVEMENT_SPEED -> "Movement Speed"
            Attribute.HORSE_JUMP_STRENGTH -> "Horse Jump Strength"
            Attribute.ZOMBIE_SPAWN_REINFORCEMENTS -> "Zombie Spawn Reinforcements"
        }

    fun addAttributeBaseStats(
        equipment: Equipment,
        meta: ItemMeta,
        slot: EquipmentSlot?
    ) {
        val baseAttributes = equipment.baseAttributes

        baseAttributes.forEach { (attribute, value) ->
            val actualValue = getActualAttributeValue(attribute, value)

            meta.addAttributeModifier(
                attribute,
                AttributeModifier(
                    UUID.randomUUID(),
                    attribute.key.key,
                    actualValue,
                    AttributeModifier.Operation.ADD_NUMBER,
                    slot
                )
            )
        }
    }

    fun removeAttributeBaseStats(
        equipment: Equipment?,
        meta: ItemMeta
    ): Boolean {
        val baseAttributes = equipment?.baseAttributes ?: return false

        for ((attribute, value) in baseAttributes) {
            val attributeModifiers = meta.getAttributeModifiers(attribute) ?: continue
            val actualValue = getActualAttributeValue(attribute, value)

            attributeModifiers
                .find { abs(it.amount - actualValue) < 0.00001 }
                ?.let { meta.removeAttributeModifier(attribute, it) }
                ?: return false
        }
        return true
    }

    fun setEntityAttribute(
        entity: LivingEntity,
        attribute: Attribute,
        value: Double
    ) {
        val attributeInstance = entity.getAttribute(attribute) ?: return
        attributeInstance.baseValue = value
    }

    private fun getAttributeLine(
        attribute: Attribute,
        value: Double,
        base: Boolean
    ): String {
        var color = ChatColor.BLUE
        var prefix = "+"

        if (base
            && (attribute == Attribute.GENERIC_ATTACK_DAMAGE || attribute == Attribute.GENERIC_ATTACK_SPEED)
        ) {
            color = ChatColor.DARK_GREEN
            prefix = " "
        }

        val decimalValue = decimalFormat.format(getDisplayedValue(attribute, value)).replace(',', '.')
        val attributeName = getAttributeAsString(attribute)

        return "$color$prefix$decimalValue $attributeName"
    }
}
