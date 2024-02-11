package de.fuballer.mcendgame.component.corruption

import de.fuballer.mcendgame.component.attribute.RollableAttribute
import de.fuballer.mcendgame.component.corruption.data.CorruptionChanceType
import de.fuballer.mcendgame.technical.extension.ItemStackExtension.setCorruptionRounds
import de.fuballer.mcendgame.technical.extension.ItemStackExtension.setUnmodifiable
import de.fuballer.mcendgame.util.ItemCreatorUtil
import de.fuballer.mcendgame.util.random.RandomOption
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

object CorruptionSettings {
    private val BASE_ITEM = Material.STICK
    private val ITEM_NAME = ChatColor.DARK_RED.toString() + "Orb of Corruption"
    private val ITEM_LORE = listOf("${ChatColor.GRAY}${ChatColor.ITALIC}Corrupts an item, modifying it unpredictably")
    private val ITEM_LORE_DOUBLE = listOf("${ChatColor.GRAY}${ChatColor.ITALIC}Corrupts an item, modifying it unpredictably. Twice!")
    val CORRUPTION_TAG_LORE = ChatColor.DARK_RED.toString() + "Corrupted"

    val CORRUPTIONS = listOf(
        RandomOption(40, CorruptionChanceType.CORRUPT_ENCHANTS),
        RandomOption(40, CorruptionChanceType.CORRUPT_ATTRIBUTES),
        RandomOption(10, CorruptionChanceType.DESTROY),
        RandomOption(10, CorruptionChanceType.DO_NOTHING)
    )

    val ALTERNATE_CORRUPTIONS = listOf(
        RandomOption(65, CorruptionChanceType.CORRUPT_ENCHANTS),
        RandomOption(10, CorruptionChanceType.DESTROY),
        RandomOption(25, CorruptionChanceType.DO_NOTHING)
    )

    const val PRESENT_ENCHANT_WEIGHT_MULTIPLIER = 3

    fun corruptAttributeValue(bounds: RollableAttribute, value: Double, random: Double) = value + 0.3 * (bounds.max - bounds.min) * (random - 0.5)

    private val CORRUPTION_ITEM = ItemCreatorUtil.create(
        ItemStack(BASE_ITEM),
        ITEM_NAME,
        ITEM_LORE
    ).apply {
        setCorruptionRounds(1)
        setUnmodifiable()
    }

    private val DOUBLE_CORRUPTION_ITEM = ItemCreatorUtil.create(
        ItemStack(BASE_ITEM),
        ITEM_NAME,
        ITEM_LORE_DOUBLE,
        Enchantment.ARROW_FIRE, 1, true,
        ItemFlag.HIDE_ENCHANTS
    ).apply {
        setCorruptionRounds(2)
        setUnmodifiable()
    }

    fun getCorruptionItem() = CORRUPTION_ITEM.clone()
    fun getDoubleCorruptionItem() = DOUBLE_CORRUPTION_ITEM.clone()
}
