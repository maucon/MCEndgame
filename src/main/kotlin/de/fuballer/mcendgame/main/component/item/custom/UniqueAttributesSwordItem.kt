package de.fuballer.mcendgame.main.component.item.custom

import net.minecraft.item.ItemStack
import net.minecraft.item.SwordItem
import net.minecraft.item.ToolMaterial
import net.minecraft.text.MutableText

abstract class UniqueAttributesSwordItem(
    toolMaterial: ToolMaterial,
    val settings: Settings,
) : SwordItem(toolMaterial, settings), UniqueAttributesItemInterface {
    override fun getDefaultStack() = getRolledStack(this, true)

    override fun getName(stack: ItemStack): MutableText = super.getName(stack).copy().withColor(getNameColor())
}