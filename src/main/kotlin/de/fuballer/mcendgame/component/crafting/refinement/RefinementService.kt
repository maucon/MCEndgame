package de.fuballer.mcendgame.component.crafting.refinement

import de.fuballer.mcendgame.component.crafting.AnvilCraftingBaseService
import de.fuballer.mcendgame.component.item.attribute.AttributeUtil
import de.fuballer.mcendgame.component.item.attribute.RollType
import de.fuballer.mcendgame.component.item.attribute.SingleValueAttribute
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.ItemStackExtension.isCustomItemType
import de.fuballer.mcendgame.util.extension.ItemStackExtension.isRefinement
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setCustomAttributes
import org.bukkit.inventory.ItemStack

@Component
class RefinementService : AnvilCraftingBaseService() {
    override fun isBaseValid(base: ItemStack): Boolean {
        val hasMultipleRollableCustomAttributes = AttributeUtil.getRollableCustomAttributes(base).size >= 2

        return !base.isCustomItemType()
                && hasMultipleRollableCustomAttributes
    }

    override fun isCraftingItemValid(craftingItem: ItemStack) = craftingItem.isRefinement()

    override fun getResultPreview(base: ItemStack) = base

    override fun getResult(base: ItemStack, craftingItem: ItemStack): ItemStack {
        val attributes = AttributeUtil.getRollableCustomAttributes(base).toMutableList()

        val sacrificedAttribute = attributes.random()
        attributes.remove(sacrificedAttribute)

        val sacrificedPercentage = when (sacrificedAttribute.rollType) {
            RollType.SINGLE -> (sacrificedAttribute as SingleValueAttribute).percentRoll
            RollType.STATIC -> throw IllegalStateException() // cannot happen
        }

        val enhancedAttribute = attributes.random()
        when (enhancedAttribute.rollType) {
            RollType.SINGLE -> {
                val attribute = enhancedAttribute as SingleValueAttribute
                attribute.percentRoll += RefinementSettings.refineAttributeValue(sacrificedPercentage)
            }

            RollType.STATIC -> throw IllegalStateException() // cannot happen
        }

        base.setCustomAttributes(attributes)
        return base
    }
}