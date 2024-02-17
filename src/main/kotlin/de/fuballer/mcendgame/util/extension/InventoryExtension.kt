package de.fuballer.mcendgame.util.extension

import de.fuballer.mcendgame.component.inventory.CustomInventoryType
import de.fuballer.mcendgame.component.inventory.InventoryRegistry
import org.bukkit.inventory.Inventory

object InventoryExtension {
    fun Inventory.setCustomType(type: CustomInventoryType) {
        InventoryRegistry.register(this, type)
    }

    fun Inventory.getCustomType() = InventoryRegistry.getType(this)
}