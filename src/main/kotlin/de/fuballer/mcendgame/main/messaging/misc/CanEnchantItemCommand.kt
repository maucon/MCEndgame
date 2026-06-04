package de.fuballer.mcendgame.main.messaging.misc

import net.minecraft.world.item.ItemStack

data class CanEnchantItemCommand(
    val itemStack: ItemStack,
    var canEnchant: Boolean = true,
)