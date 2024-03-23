package de.fuballer.mcendgame.component.crafting.refinement

import de.fuballer.mcendgame.component.crafting.AnvilCraftingBaseService
import de.fuballer.mcendgame.component.item.equipment.Equipment
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.ItemUtil
import de.fuballer.mcendgame.util.extension.ItemStackExtension.getCustomItemType
import de.fuballer.mcendgame.util.extension.ItemStackExtension.getRolledAttributes
import de.fuballer.mcendgame.util.extension.ItemStackExtension.isRefinement
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setRolledAttributes
import org.bukkit.inventory.ItemStack

@Component
class RefinementService : AnvilCraftingBaseService() {
    override val repairCost = 2

    override fun isBaseValid(base: ItemStack) =
        Equipment.existsByMaterial(base.type)
                && base.getCustomItemType() == null
                && hasMultipleAttributes(base)

    override fun isCraftingItemValid(craftingItem: ItemStack) =
        craftingItem.isRefinement()

    override fun getResultPreview(base: ItemStack) = base

    override fun updateResult(result: ItemStack, craftingItem: ItemStack) {
        val attributes = result.getRolledAttributes()?.toMutableList() ?: return
        val attributesBounds = ItemUtil.getEquipmentAttributes(result)

        val sacrificedAttribute = attributes.random()
        attributes.remove(sacrificedAttribute)

        val sacrificedAttributeBounds = attributesBounds.firstOrNull { it.type == sacrificedAttribute.type } ?: return
        val sacrificedAttributePercentage = (sacrificedAttribute.roll - sacrificedAttributeBounds.min) / (sacrificedAttributeBounds.max - sacrificedAttributeBounds.min)

        val enhancedAttribute = attributes.random()
        val enhancedAttributeBounds = attributesBounds.firstOrNull { it.type == enhancedAttribute.type } ?: return
        val enhancedAttributeRange = enhancedAttributeBounds.max - enhancedAttributeBounds.min

        enhancedAttribute.roll += RefinementSettings.refineAttributeValue(enhancedAttributeRange, sacrificedAttributePercentage)

        result.setRolledAttributes(attributes)
    }

    private fun hasMultipleAttributes(item: ItemStack): Boolean {
        val attributes = item.getRolledAttributes() ?: return false
        return attributes.size >= 2
    }
}