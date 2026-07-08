package de.fuballer.mcendgame.main.component.inventory

import net.minecraft.inventory.Inventory
import net.minecraft.screen.slot.Slot
import net.minecraft.util.Identifier

class EmptySpriteSlot(
    inventory: Inventory,
    index: Int,
    x: Int,
    y: Int,
    val sprite: Identifier,
) : Slot(inventory, index, x, y)