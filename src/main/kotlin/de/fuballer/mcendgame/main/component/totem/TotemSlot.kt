package de.fuballer.mcendgame.main.component.totem

import de.fuballer.mcendgame.main.component.item.custom.totem.TotemItem
import de.fuballer.mcendgame.main.component.item.custom.totem.TotemType
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.resources.Identifier
import net.minecraft.world.Container
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

private val BACKGROUND_SPRITE: Identifier = IdentifierUtil.default("container/slot/totem")

class TotemSlot(
    inventory: Container,
    index: Int,
    x: Int,
    y: Int,
    val type: TotemType,
) : Slot(inventory, index, x, y) {
    override fun mayPlace(stack: ItemStack) = (stack.item as? TotemItem)?.type == type

    override fun getNoItemIcon() = BACKGROUND_SPRITE
}