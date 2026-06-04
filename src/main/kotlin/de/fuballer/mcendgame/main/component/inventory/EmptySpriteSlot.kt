package de.fuballer.mcendgame.main.component.inventory

import net.minecraft.resources.Identifier
import net.minecraft.world.Container
import net.minecraft.world.inventory.Slot

class EmptySpriteSlot(
    inventory: Container,
    index: Int,
    x: Int,
    y: Int,
    val sprite: Identifier,
) : Slot(inventory, index, x, y) {
    override fun getNoItemIcon() = sprite
}