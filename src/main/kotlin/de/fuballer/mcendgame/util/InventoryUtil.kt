package de.fuballer.mcendgame.util

import de.fuballer.mcendgame.component.inventory.CustomInventory
import de.fuballer.mcendgame.component.inventory.CustomInventoryType
import de.fuballer.mcendgame.configuration.PluginConfiguration
import de.fuballer.mcendgame.util.extension.InventoryExtension.setCustomType
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory

object InventoryUtil {
    fun createInventory(
        type: InventoryType,
        title: String,
        customType: CustomInventoryType
    ): Inventory {
        val inventory = PluginConfiguration.server().createInventory(null, type, title) as CustomInventory
        inventory.setCustomType(customType)

        return inventory
    }

    fun createInventory(
        size: Int,
        title: String,
        customType: CustomInventoryType
    ): Inventory {
        val inventory = PluginConfiguration.server().createInventory(null, size, title) as CustomInventory
        inventory.setCustomType(customType)

        return inventory
    }
}