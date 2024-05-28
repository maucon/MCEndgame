package de.fuballer.mcendgame.util.extension

import de.fuballer.mcendgame.component.inventory.CustomInventory
import de.fuballer.mcendgame.component.inventory.CustomInventoryType
import org.bukkit.inventory.Inventory

object InventoryExtension {
    fun Inventory.setCustomType(type: CustomInventoryType) {
        val customInventory = this as CustomInventory
        customInventory.type = type
    }

    fun Inventory.getCustomType(): CustomInventoryType? {
        val customInventory = this as? CustomInventory ?: return null
        return customInventory.type
    }
}