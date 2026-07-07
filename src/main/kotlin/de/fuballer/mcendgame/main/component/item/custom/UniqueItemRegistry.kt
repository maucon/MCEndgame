package de.fuballer.mcendgame.main.component.item.custom

import de.fuballer.mcendgame.main.util.minecraft.RegistryKeyUtil
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object UniqueItemRegistry {
    val ENTRIES: Map<String, Item>
        get() = _entries

    private val _entries = mutableMapOf<String, Item>()

    fun registerItem(item: Item, name: String): Item {
        val itemID = RegistryKeyUtil.createItemKey(name)
        val item = Registry.register(Registries.ITEM, itemID, item)
        _entries[name] = item
        return item
    }
}