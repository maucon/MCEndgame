package de.fuballer.mcendgame.main.component.item.custom

import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

abstract class UniqueAttributesItem(
    val settings: Properties,
) : Item(settings), UniqueAttributesItemInterface {
    override fun getDefaultInstance() = getRolledStack(this, true)

    override fun getName(stack: ItemStack): MutableComponent = super.getName(stack).copy().withColor(getNameColor())
}