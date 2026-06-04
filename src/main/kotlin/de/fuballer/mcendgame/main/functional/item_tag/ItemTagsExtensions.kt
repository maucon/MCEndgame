package de.fuballer.mcendgame.main.functional.item_tag

import net.minecraft.world.item.ItemStack

object ItemTagsExtensions {
    fun ItemStack.addItemTag(itemTag: ItemTag) {
        val itemTags = get(ItemTagComponentType.COMPONENT_TYPE) ?: mutableSetOf()
        itemTags.add(itemTag)

        set(ItemTagComponentType.COMPONENT_TYPE, itemTags)
    }

    fun ItemStack.removeItemTag(itemTag: ItemTag) {
        val itemTags = get(ItemTagComponentType.COMPONENT_TYPE) ?: return
        itemTags.remove(itemTag)

        set(ItemTagComponentType.COMPONENT_TYPE, itemTags)
    }

    fun ItemStack.hasItemTag(itemTag: ItemTag): Boolean {
        val itemTags = get(ItemTagComponentType.COMPONENT_TYPE) ?: return false
        return itemTags.contains(itemTag)
    }
}