package de.fuballer.mcendgame.main.util.extension

import de.fuballer.mcendgame.main.component.item.equipment.Equipment
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.ItemStack

object ItemStackExtension {
    fun ItemStack.isSameIgnoringDurability(other: ItemStack): Boolean {
        if (!`is`(other.item)) return false
        val ownComponents = components.filter { it != DataComponents.DAMAGE }.associateBy { it.type }
        val otherComponents = other.components.filter { it != DataComponents.DAMAGE }.associateBy { it.type }
        return ownComponents == otherComponents
    }

    fun ItemStack.isForgeable() = Equipment.fromItem(item) != null
}