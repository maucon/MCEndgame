package de.fuballer.mcendgame.component.corruption

import de.fuballer.mcendgame.component.corruption.data.CorruptionChanceType
import de.fuballer.mcendgame.util.ItemCreatorUtil
import de.fuballer.mcendgame.util.random.RandomOption
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

object CorruptionSettings {
    private val BASE_ITEM = Material.FERMENTED_SPIDER_EYE
    private val ITEM_NAME = ChatColor.DARK_RED.toString() + "Heart of Corruption"
    private val ITEM_NAME_DOUBLE = ChatColor.DARK_RED.toString() + "Heart of Corruption"
    private val ITEM_LORE = listOf(ChatColor.WHITE.toString() + "Corrupts an item, modifying it unpredictably.")
    val ITEM_LORE_DOUBLE = listOf(ChatColor.WHITE.toString() + "Corrupts an item, modifying it unpredictably. Twice!")
    val CORRUPTION_TAG_LORE = listOf(ChatColor.DARK_RED.toString() + "Corrupted")

    val CORRUPTIONS = listOf(
        RandomOption(40, CorruptionChanceType.CORRUPT_ENCHANTS),
        RandomOption(40, CorruptionChanceType.CORRUPT_STATS),
        RandomOption(10, CorruptionChanceType.CORRUPT_DESTROY),
        RandomOption(10, CorruptionChanceType.DO_NOTHING)
    )

    val ALTERNATE_CORRUPTIONS = listOf(
        RandomOption(65, CorruptionChanceType.CORRUPT_ENCHANTS),
        RandomOption(10, CorruptionChanceType.CORRUPT_DESTROY),
        RandomOption(25, CorruptionChanceType.DO_NOTHING)
    )

    const val PRESENT_ENCHANT_WEIGHT_MULTIPLIER = 3

    private val CORRUPTION_ITEM = ItemCreatorUtil.create(
        ItemStack(BASE_ITEM),
        ITEM_NAME,
        ITEM_LORE
    )
    private val DOUBLE_CORRUPTION_ITEM = ItemCreatorUtil.create(
        ItemStack(BASE_ITEM),
        ITEM_NAME_DOUBLE,
        ITEM_LORE_DOUBLE,
        Enchantment.ARROW_FIRE, 1, true,
        ItemFlag.HIDE_ENCHANTS
    )

    fun getCorruptionItem() = CORRUPTION_ITEM.clone()
    fun getDoubleCorruptionItem() = DOUBLE_CORRUPTION_ITEM.clone()

    fun isAllowedInventoryType(inventoryType: InventoryType) = inventoryType != InventoryType.GRINDSTONE
            && inventoryType != InventoryType.ANVIL
            && inventoryType != InventoryType.ENCHANTING
            && inventoryType != InventoryType.SMITHING
}
