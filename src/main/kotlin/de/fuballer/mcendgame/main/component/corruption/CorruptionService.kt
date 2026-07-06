package de.fuballer.mcendgame.main.component.corruption

import de.fuballer.mcendgame.main.component.corruption.CorruptionExtensions.setCorrupted
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.updateCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.data.AttributeRoll
import de.fuballer.mcendgame.main.component.item.equipment.Equipment
import de.fuballer.mcendgame.main.configuration.RuntimeConfig
import de.fuballer.mcendgame.main.util.random.RandomUtil
import net.minecraft.component.type.ItemEnchantmentsComponent
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.item.ItemStack
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.EnchantmentTags
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
        val builder = ItemEnchantmentsComponent.Builder(stack.enchantments)
        val enchantments = builder.enchantments
        if (enchantments.isEmpty()) return stack.copy()

        val chosenEnchantment = enchantments.random()
        val newLevel = builder.getLevel(chosenEnchantment) + change
        builder.set(chosenEnchantment, newLevel)

        val result = stack.copy()
        EnchantmentHelper.set(result, builder.build())
        return result
    }

    private fun addEnchant(
        stack: ItemStack,
        notPresentEnchants: List<RegistryKey<Enchantment>>,
    ): ItemStack {
        if (notPresentEnchants.isEmpty()) return stack.copy()

        val chosenEnchant = notPresentEnchants.random()
        val registry = RuntimeConfig.SERVER.registryManager.getWrapperOrThrow(RegistryKeys.ENCHANTMENT)
        val entry = registry.getOptional(chosenEnchant).getOrNull() ?: return stack.copy()

        val builder = ItemEnchantmentsComponent.Builder(stack.enchantments)
        builder.set(entry, 1)

        val result = stack.copy()
        EnchantmentHelper.set(result, builder.build())
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
    ): List<RegistryKey<Enchantment>> {
        val possibleEnchants = getPossibleEnchants(stack)
        val nonCurseEnchants = filterCurseEnchants(possibleEnchants, false)
        return getNotPresentEnchants(stack, nonCurseEnchants)
    }

    private fun getNotPresentCurseEnchants(
        stack: ItemStack,
    ): List<RegistryKey<Enchantment>> {
        val possibleEnchants = getPossibleEnchants(stack)
        val curseEnchants = filterCurseEnchants(possibleEnchants, true)
        return getNotPresentEnchants(stack, curseEnchants)
    }

    private fun getNotPresentEnchants(
        stack: ItemStack,
        possibleEnchants: List<RegistryKey<Enchantment>>,
    ): List<RegistryKey<Enchantment>> {
        val presentEnchants = stack.enchantments.enchantments.mapNotNull { it.key.getOrNull() }
        return possibleEnchants.filter { !presentEnchants.contains(it) }
    }

    //TODO make enchants that are removed (via data packs for example) not selectable
    private fun getPossibleEnchants(
        stack: ItemStack,
    ): List<RegistryKey<Enchantment>> {
        val equipment = Equipment.fromItem(stack.item) ?: return listOf()
        return equipment.rollableEnchants.map { it.option.enchantment }.distinct()
    }

    private fun filterCurseEnchants(
        enchants: List<RegistryKey<Enchantment>>,
        getCurses: Boolean,
    ): List<RegistryKey<Enchantment>> {
        val registry = RuntimeConfig.SERVER.registryManager.getWrapperOrThrow(RegistryKeys.ENCHANTMENT)
        return enchants.filter {
            val entry = registry.getOptional(it).getOrNull() ?: return@filter false
            entry.isIn(EnchantmentTags.CURSE) == getCurses
        }
    }
}