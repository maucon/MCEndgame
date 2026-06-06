package de.fuballer.mcendgame.main.component.item.custom

import de.fuballer.mcendgame.main.component.item.custom.armor.materials.CustomArmorMaterial
import de.fuballer.mcendgame.main.util.minecraft.RegistryUtil
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.equipment.ArmorType

object UniqueItemRegistry {
    val ENTRIES: Map<String, Item>
        get() = _entries

    private val _entries = mutableMapOf<String, Item>()

    fun registerArmorItem(factory: (Item.Properties) -> Item, material: CustomArmorMaterial, type: ArmorType, name: String): Item {
        val item = RegistryUtil.registerArmorItem(factory, material, type, name)
        _entries[name] = item
        return item
    }

    fun registerToolItem(
        factory: (Item.Properties) -> Item,
        settings: Item.Properties,
        name: String
    ): Item {
        val item = RegistryUtil.registerItem(factory, settings, name)
        _entries[name] = item
        return item
    }

    fun registerMiscItem(
        factory: (Item.Properties) -> Item,
        settings: Item.Properties,
        name: String
    ) = registerToolItem(factory, settings, name)
}