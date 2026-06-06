package de.fuballer.mcendgame.main.component.corruption

import de.fuballer.mcendgame.main.component.corruption.CorruptionExtensions.setCorrupted
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.updateCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.data.AttributeRoll
import de.fuballer.mcendgame.main.component.item.equipment.Equipment
import de.fuballer.mcendgame.main.configuration.RuntimeConfig
import de.fuballer.mcendgame.main.util.random.RandomUtil
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.EnchantmentTags
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.item.enchantment.ItemEnchantments
import kotlin.jvm.optionals.getOrNull

object CorruptionService {
    fun corrupt(
        stack: ItemStack,
    ): ItemStack {
        val possibleOutcomes = CorruptionSettings.CORRUPTION_OUTCOMES.filter { it.option.canApply(stack) }
        val outcome = RandomUtil.pickOne(possibleOutcomes).option

        val result = outcome.apply(stack.copy())
        result.setCorrupted()

        return result
    }

    fun canAddEnchant(stack: ItemStack) = getNotPresentNonCurseEnchants(stack).isNotEmpty()

    fun canAddCurseEnchant(stack: ItemStack) = getNotPresentCurseEnchants(stack).isNotEmpty()

    fun canChangeAttributeRoll(stack: ItemStack) = stack.getCustomAttributes().any { it.canBeEnhanced() }

    fun increaseEnchantLevel(stack: ItemStack) = changeRandomEnchantLevel(stack, 1)

    fun lowerEnchantLevel(stack: ItemStack) = changeRandomEnchantLevel(stack, -1)

    fun addNonCurseEnchant(stack: ItemStack): ItemStack {
        val notPresentEnchants = getNotPresentNonCurseEnchants(stack)
        return addEnchant(stack, notPresentEnchants)
    }

    fun addCurseEnchant(stack: ItemStack): ItemStack {
        val notPresentEnchants = getNotPresentCurseEnchants(stack)
        return addEnchant(stack, notPresentEnchants)
    }

    fun enhanceAttribute(stack: ItemStack): ItemStack {
        val enhanceValue = CorruptionSettings.getAttributeChange()
        return changeAttribute(stack, enhanceValue)
    }

    fun diminishAttribute(stack: ItemStack): ItemStack {
        val diminishValue = -CorruptionSettings.getAttributeChange()
        return changeAttribute(stack, diminishValue)
    }

    private fun changeRandomEnchantLevel(
        stack: ItemStack,
        change: Int,
    ): ItemStack {
        val builder = ItemEnchantments.Mutable(stack.enchantments)
        val enchantments = builder.keySet()
        if (enchantments.isEmpty()) return stack.copy()

        val chosenEnchantment = enchantments.random()
        val newLevel = builder.getLevel(chosenEnchantment) + change
        builder.set(chosenEnchantment, newLevel)

        val result = stack.copy()
        EnchantmentHelper.setEnchantments(result, builder.toImmutable())
        return result
    }

    private fun addEnchant(
        stack: ItemStack,
        notPresentEnchants: List<ResourceKey<Enchantment>>,
    ): ItemStack {
        if (notPresentEnchants.isEmpty()) return stack.copy()

        val chosenEnchant = notPresentEnchants.random()
        val registry = RuntimeConfig.SERVER.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
        val entry = registry.get(chosenEnchant).getOrNull() ?: return stack.copy()

        val builder = ItemEnchantments.Mutable(stack.enchantments)
        builder.set(entry, 1)

        val result = stack.copy()
        EnchantmentHelper.setEnchantments(result, builder.toImmutable())
        return result
    }

    private fun changeAttribute(
        stack: ItemStack,
        value: Double,
    ): ItemStack {
        val oldAttributes = stack.getCustomAttributes()
        val possibleAttributes = oldAttributes.filter { it.canBeEnhanced() }
        val chosenAttribute = possibleAttributes.randomOrNull() ?: return stack

        val enhancedAttribute = chosenAttribute.getSingleRollEnhanced(value, AttributeRoll.EnhancementType.CORRUPTION)

        val newAttributes = oldAttributes.toMutableList()
        val chosenAttributeIndex = oldAttributes.indexOf(chosenAttribute)
        newAttributes[chosenAttributeIndex] = enhancedAttribute

        val result = stack.copy()
        result.updateCustomAttributes(newAttributes)
        return result
    }

    private fun getNotPresentNonCurseEnchants(
        stack: ItemStack,
    ): List<ResourceKey<Enchantment>> {
        val possibleEnchants = getPossibleEnchants(stack)
        val nonCurseEnchants = filterCurseEnchants(possibleEnchants, false)
        return getNotPresentEnchants(stack, nonCurseEnchants)
    }

    private fun getNotPresentCurseEnchants(
        stack: ItemStack,
    ): List<ResourceKey<Enchantment>> {
        val possibleEnchants = getPossibleEnchants(stack)
        val curseEnchants = filterCurseEnchants(possibleEnchants, true)
        return getNotPresentEnchants(stack, curseEnchants)
    }

    private fun getNotPresentEnchants(
        stack: ItemStack,
        possibleEnchants: List<ResourceKey<Enchantment>>,
    ): List<ResourceKey<Enchantment>> {
        val presentEnchants = stack.enchantments.keySet().mapNotNull { it.unwrapKey().getOrNull() }
        return possibleEnchants.filter { !presentEnchants.contains(it) }
    }

    //TODO make enchants that are removed (via data packs for example) not selectable
    private fun getPossibleEnchants(
        stack: ItemStack,
    ): List<ResourceKey<Enchantment>> {
        val equipment = Equipment.fromItem(stack.item) ?: return listOf()
        return equipment.rollableEnchants.map { it.option.enchantment }.distinct()
    }

    private fun filterCurseEnchants(
        enchants: List<ResourceKey<Enchantment>>,
        getCurses: Boolean,
    ): List<ResourceKey<Enchantment>> {
        val registry = RuntimeConfig.SERVER.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
        return enchants.filter {
            val entry = registry.get(it).getOrNull() ?: return@filter false
            entry.`is`(EnchantmentTags.CURSE) == getCurses
        }
    }
}