package de.fuballer.mcendgame.main.component.item.custom

import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ShieldItem

abstract class UniqueAttributesShieldItem(
    val settings: Properties,
) : ShieldItem(settings), UniqueAttributesItemInterface {
    override fun getDefaultInstance() = getRolledStack(this, true)

    override fun getName(stack: ItemStack): MutableComponent = super.getName(stack).copy().withColor(getNameColor())
}