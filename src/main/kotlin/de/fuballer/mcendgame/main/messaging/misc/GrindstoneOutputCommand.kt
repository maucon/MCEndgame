package de.fuballer.mcendgame.main.messaging.misc

import net.minecraft.world.item.ItemStack

data class GrindstoneOutputCommand(
    val firstInput: ItemStack,
    val secondInput: ItemStack,
    var output: ItemStack,
)