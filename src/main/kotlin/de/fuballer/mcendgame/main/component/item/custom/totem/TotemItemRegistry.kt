package de.fuballer.mcendgame.main.component.item.custom.totem

import de.fuballer.mcendgame.main.util.minecraft.RegistryUtil
import net.minecraft.world.item.Item
import net.minecraft.world.item.Rarity

object TotemItemRegistry {
    val NAME_MAP = mutableMapOf<String, Item>()

    fun registerTotemItem(factory: (Item.Properties) -> Item, name: String, rarity: Rarity = Rarity.UNCOMMON): TotemItem {
        val item = RegistryUtil.registerTotemItem(factory, name, rarity)
        NAME_MAP[name] = item
        return item
    }
}