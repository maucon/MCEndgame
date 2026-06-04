package de.fuballer.mcendgame.main.component.item.custom

import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.MaceItem

abstract class UniqueAttributesMaceItem(
    val settings: Properties,
) : MaceItem(settings), UniqueAttributesItemInterface {
    override fun getDefaultInstance() = getRolledStack(this, true)

    override fun getName(stack: ItemStack): MutableComponent = super.getName(stack).copy().withColor(getNameColor())
}