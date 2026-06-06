package de.fuballer.mcendgame.main.messaging.misc

import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.CraftingInput

data class CraftingResultCommand(
    val input: CraftingInput,
    var result: ItemStack,
)