package de.fuballer.mcendgame.util

import de.fuballer.mcendgame.component.inventory.CustomInventoryType
import de.fuballer.mcendgame.configuration.PluginConfiguration
import de.fuballer.mcendgame.util.extension.InventoryExtension.setCustomType
import net.kyori.adventure.text.Component
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory

object InventoryUtil {
    fun createInventory(
        type: InventoryType,
        title: Component,
        customType: CustomInventoryType
    ): Inventory {
        val inventory = PluginConfiguration.server().createInventory(null, type, title)
        inventory.setCustomType(customType)

        return inventory
    }

    fun createInventory(
        size: Int,
        title: Component,
        customType: CustomInventoryType
    ): Inventory {
        val inventory = PluginConfiguration.server().createInventory(null, size, title)
        inventory.setCustomType(customType)

        return inventory
    }
}