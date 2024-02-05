package de.fuballer.mcendgame.technical.extension

import de.fuballer.mcendgame.domain.CustomInventoryType
import de.fuballer.mcendgame.technical.registry.InventoryRegistry
import org.bukkit.inventory.Inventory

object InventoryExtension {
    fun Inventory.setCustomType(type: CustomInventoryType) {
        InventoryRegistry.register(this, type)
    }

    fun Inventory.getCustomType() = InventoryRegistry.getType(this)
}