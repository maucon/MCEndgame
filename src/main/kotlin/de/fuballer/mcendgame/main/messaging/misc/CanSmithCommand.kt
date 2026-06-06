package de.fuballer.mcendgame.main.messaging.misc

import net.minecraft.world.item.crafting.SmithingRecipeInput

data class CanSmithCommand(
    val input: SmithingRecipeInput,
    var canSmith: Boolean = true,
)