package de.fuballer.mcendgame.component.crafting.reforge

import de.fuballer.mcendgame.component.crafting.AnvilCraftingBaseService
import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.ItemCreatorUtil
import de.fuballer.mcendgame.util.extension.ItemStackExtension.getCustomItemType
import de.fuballer.mcendgame.util.extension.ItemStackExtension.isCustomItemType
import de.fuballer.mcendgame.util.extension.ItemStackExtension.isReforgeCraftingItem
import org.bukkit.inventory.ItemStack

@Component
class ReforgeService : AnvilCraftingBaseService() {
    override fun isBaseValid(base: ItemStack) = base.isCustomItemType()

    override fun isCraftingItemValid(craftingItem: ItemStack) = craftingItem.isReforgeCraftingItem()

    override fun getResultPreview(base: ItemStack) = base

    override fun getResult(base: ItemStack, craftingItem: ItemStack): ItemStack {
        val options = CustomItemType.REGISTRY.values.toMutableList()
        options.remove(base.getCustomItemType()!!)

        val newType = options.random()
        return ItemCreatorUtil.createCustomItem(newType)
    }
}