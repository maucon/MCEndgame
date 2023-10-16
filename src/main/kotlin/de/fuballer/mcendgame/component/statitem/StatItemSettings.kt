package de.fuballer.mcendgame.component.statitem

import de.fuballer.mcendgame.domain.equipment.Equipment
import de.fuballer.mcendgame.domain.equipment.armor.Boots
import de.fuballer.mcendgame.domain.equipment.armor.Chestplate
import de.fuballer.mcendgame.domain.equipment.armor.Helmet
import de.fuballer.mcendgame.domain.equipment.armor.Leggings
import de.fuballer.mcendgame.domain.equipment.tool.*
import de.fuballer.mcendgame.random.RandomOption
import de.fuballer.mcendgame.random.SortableRandomOption
import org.bukkit.ChatColor
import org.bukkit.Material
import kotlin.math.pow

object StatItemSettings {
    const val COMMAND_NAME = "item-info"
    val STAT_ITEM_COMMAND_NO_ITEM = "${ChatColor.RED}No Item in Mainhand!"
    val STAT_ITEM_COMMAND_NO_ATTRIBUTES = "${ChatColor.RED}Item can't have attributes!"
    val STAT_ITEM_COMMAND_ITEM_TYPE_COLOR = "${ChatColor.BLACK}${ChatColor.UNDERLINE}"
    val STAT_ITEM_COMMAND_ATTRIBUTE_COLOR = "${ChatColor.DARK_GREEN}"
    val STAT_ITEM_COMMAND_VALUE_COLOR = "${ChatColor.BLACK}"
    const val STAT_ITEM_BOOK_AUTHOR = "MCEndgameTp"
    const val STAT_ITEM_BOOK_TITLE = "ItemStats"

    val SLOT_LORE_COLOR = ChatColor.GRAY

    private const val STAT_VALUE_RANDOM_EXPONENT_PER_TIER = .05
    private const val STAT_VALUE_EXPONENT_TIER_OFFSET = 10

    fun calculateStatValueScaling(randomValue: Double, mapTier: Int): Double {
        if (mapTier <= STAT_VALUE_EXPONENT_TIER_OFFSET) return randomValue
        return 1 - randomValue.pow(1 + STAT_VALUE_RANDOM_EXPONENT_PER_TIER * (mapTier - STAT_VALUE_EXPONENT_TIER_OFFSET))
    }

    private const val ENCHANT_TRIES_PER_TIER = 0.5
    fun calculateEnchantTries(mapTier: Int) = 1 + (mapTier * ENCHANT_TRIES_PER_TIER).toInt()

    val STAT_AMOUNTS = listOf(
        SortableRandomOption(500, 0, 0),
        SortableRandomOption(300, 1, 1),
        SortableRandomOption(100, 2, 2),
        SortableRandomOption(25, 3, 3),
        SortableRandomOption(5, 4, 4),
    )

    private const val EQUIPMENT_ROLL_TRIES_PER_TIER = 0.25
    fun calculateEquipmentRollTries(mapTier: Int) = 1 + (mapTier * EQUIPMENT_ROLL_TRIES_PER_TIER).toInt()

    val HELMETS = listOf(
        SortableRandomOption(500, 0, null),
        SortableRandomOption(500, 1, Helmet.LEATHER),
        SortableRandomOption(500, 2, Helmet.GOLDEN),
        SortableRandomOption(500, 3, Helmet.CHAINMAIL),
        SortableRandomOption(500, 4, Helmet.IRON),
        SortableRandomOption(100, 5, Helmet.TURTLE),
        SortableRandomOption(800, 6, Helmet.DIAMOND),
        SortableRandomOption(500, 7, Helmet.NETHERITE),
    )
    val LEGGINGS = listOf(
        SortableRandomOption(500, 0, null),
        SortableRandomOption(500, 1, Leggings.LEATHER),
        SortableRandomOption(500, 2, Leggings.GOLDEN),
        SortableRandomOption(500, 3, Leggings.CHAINMAIL),
        SortableRandomOption(500, 4, Leggings.IRON),
        SortableRandomOption(800, 6, Leggings.DIAMOND),
        SortableRandomOption(500, 7, Leggings.NETHERITE)
    )
    val CHESTPLATES = listOf(
        SortableRandomOption(500, 0, null),
        SortableRandomOption(500, 1, Chestplate.LEATHER),
        SortableRandomOption(500, 2, Chestplate.GOLDEN),
        SortableRandomOption(500, 3, Chestplate.CHAINMAIL),
        SortableRandomOption(500, 4, Chestplate.IRON),
        SortableRandomOption(800, 6, Chestplate.DIAMOND),
        SortableRandomOption(500, 7, Chestplate.NETHERITE)
    )
    val BOOTS = listOf(
        SortableRandomOption(500, 0, null),
        SortableRandomOption(500, 1, Boots.LEATHER),
        SortableRandomOption(500, 2, Boots.GOLDEN),
        SortableRandomOption(500, 3, Boots.CHAINMAIL),
        SortableRandomOption(500, 4, Boots.IRON),
        SortableRandomOption(800, 6, Boots.DIAMOND),
        SortableRandomOption(500, 7, Boots.NETHERITE)
    )

    private val SWORDS = listOf(
        SortableRandomOption(500, 1, Sword.WOODEN),
        SortableRandomOption(500, 2, Sword.GOLDEN),
        SortableRandomOption(500, 3, Sword.STONE),
        SortableRandomOption(500, 4, Sword.IRON),
        SortableRandomOption(800, 5, Sword.DIAMOND),
        SortableRandomOption(500, 6, Sword.NETHERITE),
    )
    private val AXES = listOf(
        SortableRandomOption(500, 1, Axe.WOODEN),
        SortableRandomOption(500, 2, Axe.GOLDEN),
        SortableRandomOption(500, 3, Axe.STONE),
        SortableRandomOption(500, 4, Axe.IRON),
        SortableRandomOption(800, 5, Axe.DIAMOND),
        SortableRandomOption(500, 6, Axe.NETHERITE)
    )
    private val PICKAXES = listOf(
        SortableRandomOption(500, 1, Pickaxe.WOODEN),
        SortableRandomOption(500, 2, Pickaxe.GOLDEN),
        SortableRandomOption(500, 3, Pickaxe.STONE),
        SortableRandomOption(500, 4, Pickaxe.IRON),
        SortableRandomOption(800, 5, Pickaxe.DIAMOND),
        SortableRandomOption(500, 6, Pickaxe.NETHERITE)
    )
    private val SHOVELS = listOf(
        SortableRandomOption(500, 1, Shovel.WOODEN),
        SortableRandomOption(500, 2, Shovel.GOLDEN),
        SortableRandomOption(500, 3, Shovel.STONE),
        SortableRandomOption(500, 4, Shovel.IRON),
        SortableRandomOption(800, 5, Shovel.DIAMOND),
        SortableRandomOption(500, 6, Shovel.NETHERITE),
    )
    private val HOES = listOf(
        SortableRandomOption(500, 1, Hoe.WOODEN),
        SortableRandomOption(500, 2, Hoe.GOLDEN),
        SortableRandomOption(500, 3, Hoe.STONE),
        SortableRandomOption(500, 4, Hoe.IRON),
        SortableRandomOption(800, 5, Hoe.DIAMOND),
        SortableRandomOption(500, 6, Hoe.NETHERITE),
    )
    private val BOWS = listOf(
        SortableRandomOption(100, 0, Tool.BOW)
    )
    private val TRIDENTS = listOf(
        SortableRandomOption(100, 0, Tool.TRIDENT)
    )
    const val OFFHAND_OTHER_OVER_MAINHAND_PROBABILITY = 0.25
    val OTHER_ITEMS = listOf(
        RandomOption(10, Tool.FISHING_ROD),
        RandomOption(50, Tool.SHIELD)
    )
    val MAINHAND_PROBABILITIES = listOf<RandomOption<List<SortableRandomOption<out Equipment>>?>>(
        RandomOption(10, null),
        RandomOption(10, SWORDS),
        RandomOption(10, AXES),
        RandomOption(10, PICKAXES),
        RandomOption(10, SHOVELS),
        RandomOption(10, HOES),
        RandomOption(1, TRIDENTS),
    )
    val RANGED_MAINHAND_PROBABILITIES = listOf<RandomOption<List<SortableRandomOption<out Equipment>>>>(
        RandomOption(100, BOWS),
    )

    val SMITHING_MAP: Map<Material, Equipment> = mapOf(
        Material.NETHERITE_SWORD to Sword.DIAMOND,
        Material.NETHERITE_AXE to Axe.DIAMOND,
        Material.NETHERITE_SHOVEL to Shovel.DIAMOND,
        Material.NETHERITE_PICKAXE to Pickaxe.DIAMOND,
        Material.NETHERITE_HOE to Hoe.DIAMOND,
        Material.NETHERITE_HELMET to Helmet.DIAMOND,
        Material.NETHERITE_CHESTPLATE to Chestplate.DIAMOND,
        Material.NETHERITE_LEGGINGS to Leggings.DIAMOND,
        Material.NETHERITE_BOOTS to Boots.DIAMOND
    )

    val MATERIAL_TO_EQUIPMENT = mutableMapOf<Material, Equipment>()
        .apply {
            putAll(Boots.values().associateBy { it.material })
            putAll(Chestplate.values().associateBy { it.material })
            putAll(Helmet.values().associateBy { it.material })
            putAll(Leggings.values().associateBy { it.material })
            putAll(Axe.values().associateBy { it.material })
            putAll(Hoe.values().associateBy { it.material })
            putAll(Pickaxe.values().associateBy { it.material })
            putAll(Shovel.values().associateBy { it.material })
            putAll(Sword.values().associateBy { it.material })
            putAll(Tool.values().associateBy { it.material })
        }
}
