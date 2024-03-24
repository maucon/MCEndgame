package de.fuballer.mcendgame.component.crafting.reshaping

import de.fuballer.mcendgame.component.crafting.AnvilCraftingBaseService
import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.ItemUtil
import de.fuballer.mcendgame.util.extension.ItemStackExtension.getCustomItemType
import de.fuballer.mcendgame.util.extension.ItemStackExtension.isReshaping
import org.bukkit.inventory.ItemStack

@Component
class ReshapingService : AnvilCraftingBaseService() {
    override val repairCost = 23

    override fun isBaseValid(base: ItemStack) =
        base.getCustomItemType() != null

    override fun isCraftingItemValid(craftingItem: ItemStack) =
        craftingItem.isReshaping()

    override fun getResultPreview(base: ItemStack) = ReshapingSettings.getPreviewItem()

    override fun getResult(base: ItemStack, craftingItem: ItemStack): ItemStack {
        val options = CustomItemType.REGISTRY.values.toMutableList()
        options.remove(base.getCustomItemType()!!)

        val newType = options.random()
        return ItemUtil.createCustomItem(newType)
    }
}