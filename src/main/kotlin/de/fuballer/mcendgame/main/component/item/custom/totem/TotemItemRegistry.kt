package de.fuballer.mcendgame.main.component.item.custom.totem

import de.fuballer.mcendgame.main.util.minecraft.RegistryUtil
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Rarity

object TotemItemRegistry {
    val NAME_MAP = mutableMapOf<String, Item>()

    fun registerTotemItem(
        factory: (Item.Properties) -> Item,
        itemKey: ResourceKey<Item>,
        rarity: Rarity = Rarity.UNCOMMON
    ): TotemItem {
        val item = RegistryUtil.registerTotemItem(factory, itemKey, rarity)
        NAME_MAP[itemKey.identifier().path] = item
        return item
    }
}