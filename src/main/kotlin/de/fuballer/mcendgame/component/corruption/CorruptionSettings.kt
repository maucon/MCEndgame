package de.fuballer.mcendgame.component.corruption

import de.fuballer.mcendgame.component.corruption.data.CorruptionChanceType
import de.fuballer.mcendgame.domain.attribute.RollableAttribute
import de.fuballer.mcendgame.domain.persistent_data.DataTypeKeys
import de.fuballer.mcendgame.util.ItemCreatorUtil
import de.fuballer.mcendgame.util.PersistentDataUtil
import de.fuballer.mcendgame.util.random.RandomOption
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

object CorruptionSettings {
    private val BASE_ITEM = Material.STICK
    private val ITEM_NAME = ChatColor.DARK_RED.toString() + "Heart of Corruption"
    private val ITEM_NAME_DOUBLE = ChatColor.DARK_RED.toString() + "Heart of Corruption"
    private val ITEM_LORE = listOf(ChatColor.WHITE.toString() + "Corrupts an item, modifying it unpredictably.")
    private val ITEM_LORE_DOUBLE = listOf(ChatColor.WHITE.toString() + "Corrupts an item, modifying it unpredictably. Twice!")
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
    ).also {
        val itemMeta = it.itemMeta!!
        PersistentDataUtil.setValue(itemMeta, DataTypeKeys.CORRUPTION_ROUNDS, 1)
        PersistentDataUtil.setValue(itemMeta, DataTypeKeys.CORRUPTED, true)
        it.itemMeta = itemMeta
    }

    private val DOUBLE_CORRUPTION_ITEM = ItemCreatorUtil.create(
        ItemStack(BASE_ITEM),
        ITEM_NAME_DOUBLE,
        ITEM_LORE_DOUBLE,
        Enchantment.ARROW_FIRE, 1, true,
        ItemFlag.HIDE_ENCHANTS
    ).also {
        val itemMeta = it.itemMeta!!
        PersistentDataUtil.setValue(itemMeta, DataTypeKeys.CORRUPTION_ROUNDS, 10)
        PersistentDataUtil.setValue(itemMeta, DataTypeKeys.CORRUPTED, true)
        it.itemMeta = itemMeta
    }

    fun getCorruptionItem() = CORRUPTION_ITEM.clone()
    fun getDoubleCorruptionItem() = DOUBLE_CORRUPTION_ITEM.clone()
}
