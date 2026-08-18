package de.fuballer.mcendgame.main.component.item.custom.crystal

import de.fuballer.mcendgame.main.component.block.blocks.crystalforge.CrystalForgeSettings
import de.fuballer.mcendgame.main.component.corruption.CorruptionExtensions.isCorrupted
import de.fuballer.mcendgame.main.component.dungeon.loot.drop.ItemColor
import de.fuballer.mcendgame.main.util.extension.ItemStackExtension.isForgeable
import net.minecraft.ChatFormatting
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.ItemLore
import java.awt.Color

abstract class CrystalItem(
    settings: Properties,
) : Item(settings) {
    companion object {
        const val DESCRIPTION_BASE_KEY = "item.mcendgame.crystal.description."
    }

    abstract val forgeColor: Color

    abstract val description: MutableComponent

    open fun producesSecondaryOutput() = false

    override fun getDefaultInstance(): ItemStack {
        val stack = super.defaultInstance

        val list = mutableListOf<Component>()
        list.add(description.withStyle { style -> style.withItalic(false).withColor(ChatFormatting.GRAY) })
        stack.set(DataComponents.LORE, ItemLore(list))

        return stack
    }

    override fun getName(stack: ItemStack): MutableComponent = super.getName(stack).copy().withColor(ItemColor.CRYSTAL.intColor)

    open fun canForge(
        stack: ItemStack,
        secondaryOutputSlotFilled: Boolean,
    ): MutableComponent? {
        if (stack.isEmpty) return CrystalForgeSettings.getForgeErrorText("no_item")
        if (!stack.isForgeable()) return CrystalForgeSettings.getForgeErrorText("item_not_forgeable")
        if (stack.isCorrupted()) return CrystalForgeSettings.getForgeErrorText("item_corrupted")
        if (secondaryOutputSlotFilled && producesSecondaryOutput()) return CrystalForgeSettings.getForgeErrorText("secondary_output_slot_not_empty")
        return null
    }

    abstract fun forge(stack: ItemStack): CrystalForgeOutput

    data class CrystalForgeOutput(
        val main: ItemStack,
        val secondary: ItemStack?,
    ) {
        constructor(stack: ItemStack) : this(stack, null)
    }
}