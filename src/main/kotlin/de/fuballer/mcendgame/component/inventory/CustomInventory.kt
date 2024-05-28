package de.fuballer.mcendgame.component.inventory

import org.bukkit.inventory.Inventory

abstract class CustomInventory : Inventory {
    lateinit var type: CustomInventoryType
}