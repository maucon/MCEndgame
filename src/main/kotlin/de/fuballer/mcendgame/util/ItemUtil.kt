package de.fuballer.mcendgame.util

import de.fuballer.mcendgame.component.crafting.corruption.CorruptionSettings
import de.fuballer.mcendgame.component.item.attribute.AttributeUtil
import de.fuballer.mcendgame.component.item.attribute.data.BaseAttribute
import de.fuballer.mcendgame.component.item.attribute.data.CustomAttribute
import de.fuballer.mcendgame.component.item.attribute.data.SingleValueAttribute
import de.fuballer.mcendgame.component.item.attribute.data.VanillaAttributeType
import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.component.item.equipment.Equipment
import de.fuballer.mcendgame.util.extension.ItemStackExtension.getCustomAttributes
import de.fuballer.mcendgame.util.extension.ItemStackExtension.getCustomItemType
import de.fuballer.mcendgame.util.extension.ItemStackExtension.isUnmodifiable
import org.bukkit.ChatColor
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.ItemMeta
import java.text.DecimalFormat
import java.util.*
import kotlin.random.Random

private val DECIMAL_FORMAT = DecimalFormat("#.##")

object ItemUtil {
    fun isVanillaItem(item: ItemStack): Boolean {
        val itemMeta = item.itemMeta ?: return true
        return !itemMeta.hasLore()
    }

    fun updateAttributesAndLore(item: ItemStack) {
        val itemMeta = item.itemMeta ?: return

        val equipment = Equipment.fromMaterial(item.type) ?: return
        val baseAttributes = equipment.baseAttributes
        val customAttributes = item.getCustomAttributes() ?: listOf()
        val slotLore = Equipment.getLoreForSlot(equipment.slot)

        updateAttributes(item, itemMeta, baseAttributes, customAttributes, equipment)
        updateLore(item, itemMeta, baseAttributes, customAttributes, slotLore)

        item.itemMeta = itemMeta
    }

    fun setRandomDurability(item: ItemStack): ItemStack {
        val itemMeta = item.itemMeta

        if (itemMeta is Damageable) {
            itemMeta.damage = (Random.nextDouble() * item.type.maxDurability).toInt()
            item.itemMeta = itemMeta
        }

        return item
    }

    fun isItemRenaming(renameText: String?, base: ItemStack): Boolean {
        if (renameText.isNullOrEmpty()) return false

        val itemMeta = base.itemMeta ?: return false
        if (!itemMeta.hasDisplayName()) return false

        var oldName = itemMeta.displayName
        if (oldName.startsWith("§")) { // remove color codes
            oldName = oldName.substring(2)
        }

        return oldName != renameText
    }

    private fun updateAttributes(
        item: ItemStack,
        itemMeta: ItemMeta,
        baseAttributes: List<BaseAttribute>,
        customAttributes: List<CustomAttribute>,
        equipment: Equipment
    ) {
        itemMeta.attributeModifiers?.let {
            it.forEach { attribute, _ -> itemMeta.removeAttributeModifier(attribute) }
        }

        val customItemType = item.getCustomItemType()
        val hasBaseAttributes = customItemType?.usesEquipmentBaseStats != false

        if (hasBaseAttributes) {
            addAllVanillaAttributes(itemMeta, baseAttributes, equipment.slot.group)
        }
        val customAttributeSlot = if (equipment.slotDependentAttributes) equipment.slot.group else EquipmentSlotGroup.ANY
        addAllVanillaApplicableCustomAttributes(itemMeta, customAttributes, customAttributeSlot)
    }

    private fun addAllVanillaAttributes(
        itemMeta: ItemMeta,
        attributes: List<BaseAttribute>,
        slot: EquipmentSlotGroup
    ) {
        attributes
            .filter { it.type.isVanillaAttributeType }
            .forEach {
                val attribute = it.type.vanillaAttributeType!!.attribute
                val realRoll = getActualAttributeValue(attribute, it.roll)
                addAttribute(
                    itemMeta,
                    attribute,
                    realRoll,
                    it.type.vanillaAttributeType.scaleType,
                    slot
                )
            }
    }

    private fun addAllVanillaApplicableCustomAttributes(
        itemMeta: ItemMeta,
        attributes: List<CustomAttribute>,
        slot: EquipmentSlotGroup
    ) {
        attributes
            .filter { it.type.isVanillaAttributeType }
            .forEach {
                val attribute = it.type.vanillaAttributeType!!.attribute
                val roll = (it as SingleValueAttribute).getAbsoluteRoll()
                addAttribute(
                    itemMeta,
                    attribute,
                    roll,
                    it.type.vanillaAttributeType.scaleType,
                    slot
                )
            }
    }

    private fun getActualAttributeValue(attribute: Attribute, roll: Double): Double {
        if (attribute == Attribute.GENERIC_ATTACK_DAMAGE) return roll - 1
        if (attribute == Attribute.GENERIC_ATTACK_SPEED) return roll - 4
        return roll
    }

    private fun addAttribute(
        itemMeta: ItemMeta,
        attribute: Attribute,
        value: Double,
        operation: AttributeModifier.Operation,
        slot: EquipmentSlotGroup
    ) {
        itemMeta.addAttributeModifier(
            attribute,
            AttributeModifier(
                PluginUtil.createNamespacedKey(UUID.randomUUID().toString()),
                value,
                operation,
                slot
            )
        )
    }

    private fun updateLore(
        item: ItemStack,
        itemMeta: ItemMeta,
        baseAttributes: List<BaseAttribute>,
        customAttributes: List<CustomAttribute>,
        slotLore: String
    ) {
        val lore = mutableListOf<String>()

        val customItemType = item.getCustomItemType()
        val hasBaseAttributes = customItemType?.usesEquipmentBaseStats != false
        val updateBaseAttributes = hasBaseAttributes && baseAttributes.isNotEmpty()

        val enchantments = item.enchantments
        val updateEnchantmentAttributes = enchantments.isNotEmpty()

        if (updateBaseAttributes) {
            baseAttributes.forEach {
                val attributeLine = getBaseAttributeLine(it)
                lore.add(attributeLine)
            }
        }
        if (updateEnchantmentAttributes) {
            for (enchantment in enchantments) {
                val enchantmentAttributeLine = getEnchantmentAttributeLine(enchantment.key, enchantment.value) ?: continue
                lore.add(enchantmentAttributeLine)
            }
        }
        if (lore.isNotEmpty()) {
            lore.add(0, slotLore)
        }

        if (customAttributes.isNotEmpty()) {
            lore.add("")
            lore.add(Equipment.GENERIC_SLOT_LORE)

            val attributes = getSortedCustomAttributes(customItemType, customAttributes)

            attributes.forEach {
                val attributeLine = getCustomAttributeLine(it)
                lore.add(attributeLine)
            }
        }
        if (lore.isNotEmpty()) {
            lore.add(0, "")
        }
        if (item.isUnmodifiable()) {
            lore.add(0, CorruptionSettings.CORRUPTION_TAG_LORE)
        }

        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
        itemMeta.lore = lore
    }

    private fun getSortedCustomAttributes(
        customItemType: CustomItemType?,
        customAttributes: List<CustomAttribute>
    ): List<CustomAttribute> {
        if (customItemType == null) {
            return customAttributes.sortedBy { it.type.ordinal }
        }

        val attributeTypeOrder = customItemType.attributes.map { it.type }
        return customAttributes.sortedWith(compareBy { attributeTypeOrder.indexOf(it.type) })
    }

    private fun getBaseAttributeLine(
        attribute: BaseAttribute
    ): String {
        var attributeLore = AttributeUtil.getCorrectSignLore(attribute)
        val vanillaAttributeType = attribute.type.vanillaAttributeType!!

        if (isNotPlayerBaseAttribute(vanillaAttributeType)) return "${ChatColor.BLUE}$attributeLore"

        attributeLore = attributeLore.replaceFirstChar { " " }
        return "${ChatColor.DARK_GREEN}$attributeLore"
    }

    private fun getEnchantmentAttributeLine(
        enchantment: Enchantment,
        level: Int,
    ): String? {
        val line = when (enchantment) {
            Enchantment.DEPTH_STRIDER -> "+${DECIMAL_FORMAT.format(level / 3.0)} Water Movement Efficiency"
            Enchantment.SWIFT_SNEAK -> "+${DECIMAL_FORMAT.format(0.15 * level)} Sneaking Speed"
            Enchantment.AQUA_AFFINITY -> "+${400 * level}% Submerged Mining Speed"
            Enchantment.RESPIRATION -> "+${1 * level} Oxygen Bonus"
            Enchantment.SWEEPING_EDGE -> "+${DECIMAL_FORMAT.format(level / (level + 1.0))} Sweeping Damage Ratio"
            Enchantment.EFFICIENCY -> "+${2 + (1 until level).sumOf { 1 + 2 * it }} Mining Efficiency"
            Enchantment.FIRE_PROTECTION -> "-${15 * level}% Burning Time"
            Enchantment.BLAST_PROTECTION -> "+${DECIMAL_FORMAT.format(0.15 * level)} Explosion Knockback Resistance"
            else -> return null
        }

        return "${ChatColor.BLUE}$line"
    }

    private fun getCustomAttributeLine(attribute: CustomAttribute): String {
        val attributeLore = AttributeUtil.getCorrectSignLore(attribute)
        return "${ChatColor.BLUE}$attributeLore"
    }

    private fun isNotPlayerBaseAttribute(vanillaAttributeType: VanillaAttributeType) =
        vanillaAttributeType.attribute != Attribute.GENERIC_ATTACK_DAMAGE
                && vanillaAttributeType.attribute != Attribute.GENERIC_ATTACK_SPEED
}