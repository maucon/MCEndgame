package de.fuballer.mcendgame.main.component.corruption

import de.fuballer.mcendgame.main.functional.item_tag.ItemTag
import de.fuballer.mcendgame.main.functional.item_tag.ItemTagsExtensions.addItemTag
import de.fuballer.mcendgame.main.functional.item_tag.ItemTagsExtensions.hasItemTag
import de.fuballer.mcendgame.main.functional.item_tag.ItemTagsExtensions.removeItemTag
import net.minecraft.world.item.ItemStack

object CorruptionExtensions {
    fun ItemStack.setCorrupted() = addItemTag(ItemTag.CORRUPTED)
    fun ItemStack.unsetCorrupted() = removeItemTag(ItemTag.CORRUPTED)
    fun ItemStack.isCorrupted() = hasItemTag(ItemTag.CORRUPTED)
}