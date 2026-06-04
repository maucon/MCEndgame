package de.fuballer.mcendgame.main.component.block.blocks.crystalforge.slot

import de.fuballer.mcendgame.main.util.extension.ItemStackExtension.isForgeable
import net.minecraft.world.Container
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

class ForgeableEquipmentSlot(
    inventory: Container,
    index: Int,
    x: Int,
    y: Int,
) : Slot(inventory, index, x, y) {
    override fun mayPlace(stack: ItemStack) = stack.isForgeable()
}