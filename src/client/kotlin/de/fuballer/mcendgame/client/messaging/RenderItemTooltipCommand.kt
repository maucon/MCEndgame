package de.fuballer.mcendgame.client.messaging

import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

data class RenderItemTooltipCommand(
    val itemStack: ItemStack,
    val context: Item.TooltipContext,
    val tooltipType: TooltipFlag,
    val texts: MutableList<Component>
)