package de.fuballer.mcendgame.technical.registry

import de.fuballer.mcendgame.domain.CustomInventoryType
import org.bukkit.inventory.Inventory

object InventoryRegistry {
    private val map = HashMap<Inventory, CustomInventoryType>();

    fun register(inventory: Inventory, type: CustomInventoryType) {
        map[inventory] = type
    }

    fun unregister(inventory: Inventory) = map.remove(inventory)

    fun getType(inventory: Inventory) = map[inventory]
}