package de.fuballer.mcendgame.component.crafting.corruption

import de.fuballer.mcendgame.component.crafting.CraftingItemSettings
import de.fuballer.mcendgame.component.item.attribute.data.CustomAttribute
import de.fuballer.mcendgame.util.ItemCreator
import de.fuballer.mcendgame.util.TextComponent
import de.fuballer.mcendgame.util.extension.AttributeRollExtension.run
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setCorruptionRounds
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setCraftingItem
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setUnmodifiable
import de.fuballer.mcendgame.util.random.RandomOption
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

object CorruptionSettings {
    private val ITEM_NAME = TextComponent.create("Orb of Corruption", NamedTextColor.DARK_RED)
    private val ITEM_LORE = listOf(TextComponent.create("Corrupt an item, modifying it unpredictably"), *CraftingItemSettings.CRAFTING_ITEM_USAGE_DISCLAIMER)
    private val ITEM_LORE_DOUBLE = listOf(TextComponent.create("Corrupt an item, modifying it unpredictably. Twice!"), *CraftingItemSettings.CRAFTING_ITEM_USAGE_DISCLAIMER)
    val CORRUPTION_TAG_LORE = TextComponent.create("Corrupted", NamedTextColor.DARK_RED)

    val CORRUPTIONS = listOf(
        RandomOption(45, CorruptionModification.CORRUPT_ENCHANTS),
        RandomOption(45, CorruptionModification.CORRUPT_ATTRIBUTES),
        RandomOption(5, CorruptionModification.DESTROY),
        RandomOption(5, CorruptionModification.DO_NOTHING)
    )

    val CORRUPTIONS_WITHOUT_ATTRIBUTES = listOf(
        RandomOption(85, CorruptionModification.CORRUPT_ENCHANTS),
        RandomOption(5, CorruptionModification.DESTROY),
        RandomOption(10, CorruptionModification.DO_NOTHING)
    )

    const val CORRUPT_ENCHANTS_INCREASE_LEVEL_CHANCE = 0.7

    const val PRESENT_ENCHANT_WEIGHT_MULTIPLIER = 3

    fun corruptAttributeRoll(attribute: CustomAttribute) {
        for (attributeRoll in attribute.attributeRolls) {
            attributeRoll.run(
                { it.percentRoll += (Random.nextDouble() - 0.5) },
                { it.indexRoll = Random.nextInt(it.bounds.options.size) },
                { it.percentRoll += (Random.nextDouble() - 0.5) }
            )
        }
    }

    private val CORRUPTION_ITEM = ItemCreator.create(
        ItemStack(CraftingItemSettings.BASE_ITEM),
        ITEM_NAME,
        ITEM_LORE
    ).apply {
        setCorruptionRounds(1)
        setCraftingItem()
        setUnmodifiable()
    }

    private val DOUBLE_CORRUPTION_ITEM = ItemCreator.create(
        ItemStack(CraftingItemSettings.BASE_ITEM),
        ITEM_NAME,
        ITEM_LORE_DOUBLE,
        Enchantment.FLAME, 1, true,
        ItemFlag.HIDE_ENCHANTS
    ).apply {
        setCorruptionRounds(2)
        setCraftingItem()
        setUnmodifiable()
    }

    fun getCorruptItem() = CORRUPTION_ITEM.clone()
    fun getDoubleCorruptItem() = DOUBLE_CORRUPTION_ITEM.clone()
}