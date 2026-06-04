package de.fuballer.mcendgame.main.component.item.custom.totem

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.setCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import net.minecraft.ChatFormatting
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.ItemLore
import kotlin.math.min

abstract class TotemItem(
    settings: Properties,
) : Item(settings) {
    companion object {
        const val TIER_KEY = "item.mcendgame.totem.tier"
    }

    abstract val maxTier: Int
    abstract val type: TotemType

    abstract fun getCustomAttributes(tier: Int): List<CustomAttribute>

    fun getStack(tier: Int = 0): ItemStack {
        val stack = super.defaultInstance

        val limitedRarity = min(tier, maxTier)
        addLore(stack, tier)
        stack.setCustomAttributes(getCustomAttributes(limitedRarity), EquipmentSlotGroup.CHEST)

        return stack
    }

    private fun addLore(stack: ItemStack, tier: Int) {
        val lore = listOf(
            Component.translatable(TIER_KEY, tier).withStyle { style -> style.withItalic(false).withColor(ChatFormatting.GRAY) },
            type.getLore().withStyle { style -> style.withItalic(false).withColor(ChatFormatting.GRAY) },
        )
        stack.set(DataComponents.LORE, ItemLore(lore))
    }

    override fun getName(stack: ItemStack): MutableComponent = super.getName(stack).copy().withColor(type.color.intColor)

    override fun getDefaultInstance() = getStack()

    fun getMaxTierStack() = getStack(maxTier)
}