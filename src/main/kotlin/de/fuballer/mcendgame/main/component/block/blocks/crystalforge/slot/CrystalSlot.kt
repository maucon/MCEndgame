package de.fuballer.mcendgame.main.component.block.blocks.crystalforge.slot

import de.fuballer.mcendgame.main.component.item.custom.crystal.CrystalItem
import net.minecraft.world.Container
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

class CrystalSlot(
    inventory: Container,
    index: Int,
    x: Int,
    y: Int,
) : Slot(inventory, index, x, y) {
    override fun mayPlace(stack: ItemStack) = stack.item is CrystalItem
}