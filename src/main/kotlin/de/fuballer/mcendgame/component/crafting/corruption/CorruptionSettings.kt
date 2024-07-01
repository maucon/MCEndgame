package de.fuballer.mcendgame.component.crafting.corruption

import de.fuballer.mcendgame.component.item.attribute.data.SingleValueAttribute
import de.fuballer.mcendgame.util.ItemCreatorUtil
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setCorruptionRounds
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setCraftingItem
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setUnmodifiable
import de.fuballer.mcendgame.util.random.RandomOption
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

object CorruptionSettings {
    private val BASE_ITEM = Material.PAPER
    private val ITEM_NAME = ChatColor.DARK_RED.toString() + "Orb of Corruption"
    private val ITEM_LORE = listOf("${ChatColor.GRAY}${ChatColor.ITALIC}Corrupt an item, modifying it unpredictably")
    private val ITEM_LORE_DOUBLE = listOf("${ChatColor.GRAY}${ChatColor.ITALIC}Corrupt an item, modifying it unpredictably. Twice!")
    val CORRUPTION_TAG_LORE = ChatColor.DARK_RED.toString() + "Corrupted"

    val CORRUPTIONS = listOf(
        RandomOption(40, CorruptionModification.CORRUPT_ENCHANTS),
        RandomOption(40, CorruptionModification.CORRUPT_ATTRIBUTES),
        RandomOption(10, CorruptionModification.DESTROY),
        RandomOption(10, CorruptionModification.DO_NOTHING)
    )

    val ALTERNATE_CORRUPTIONS = listOf(
        RandomOption(65, CorruptionModification.CORRUPT_ENCHANTS),
        RandomOption(10, CorruptionModification.DESTROY),
        RandomOption(25, CorruptionModification.DO_NOTHING)
    )

    const val PRESENT_ENCHANT_WEIGHT_MULTIPLIER = 3

    fun corruptAttributePercentRoll(attribute: SingleValueAttribute, random: Double) {
        attribute.percentRoll += (random * 0.6 - 0.3)
    }

    fun corruptAttributePercentRollForCustomItems(attribute: SingleValueAttribute, random: Double) {
        attribute.percentRoll += (random - 0.5)
    }

    private val CORRUPTION_ITEM = ItemCreatorUtil.create(
        ItemStack(BASE_ITEM),
        ITEM_NAME,
        ITEM_LORE
    ).apply {
        setCorruptionRounds(1)
        setCraftingItem()
        setUnmodifiable()
    }

    private val DOUBLE_CORRUPTION_ITEM = ItemCreatorUtil.create(
        ItemStack(BASE_ITEM),
        ITEM_NAME,
        ITEM_LORE_DOUBLE,
        Enchantment.FLAME, 1, true,
        ItemFlag.HIDE_ENCHANTS
    ).apply {
        setCorruptionRounds(2)
        setCraftingItem()
        setUnmodifiable()
    }

    fun getCorruptionItem() = CORRUPTION_ITEM.clone()
    fun getDoubleCorruptionItem() = DOUBLE_CORRUPTION_ITEM.clone()
}