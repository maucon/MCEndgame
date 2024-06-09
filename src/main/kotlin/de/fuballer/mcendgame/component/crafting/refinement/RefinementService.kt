package de.fuballer.mcendgame.component.crafting.refinement

import de.fuballer.mcendgame.component.crafting.AnvilCraftingBaseService
import de.fuballer.mcendgame.component.item.attribute.AttributeUtil
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.ItemStackExtension.isCustomItemType
import de.fuballer.mcendgame.util.extension.ItemStackExtension.isRefinement
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setCustomAttributes
import org.bukkit.inventory.ItemStack

@Component
class RefinementService : AnvilCraftingBaseService() {
    override fun isBaseValid(base: ItemStack): Boolean {
        val hasMultipleRollableCustomAttributes = AttributeUtil.getSingleValueAttributes(base).size >= 2

        return !base.isCustomItemType()
                && hasMultipleRollableCustomAttributes
    }

    override fun isCraftingItemValid(craftingItem: ItemStack) = craftingItem.isRefinement()

    override fun getResultPreview(base: ItemStack) = base

    override fun getResult(base: ItemStack, craftingItem: ItemStack): ItemStack {
        val attributes = AttributeUtil.getSingleValueAttributes(base).toMutableList()

        val sacrificedAttribute = attributes.random()
        attributes.remove(sacrificedAttribute)

        val enhancedAttribute = attributes.random()
        enhancedAttribute.percentRoll += RefinementSettings.refineAttributeValue(sacrificedAttribute.percentRoll)

        base.setCustomAttributes(attributes)
        return base
    }
}