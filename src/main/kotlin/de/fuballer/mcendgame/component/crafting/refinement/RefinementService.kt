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
    override fun isBaseValid(base: ItemStack) =
        Equipment.existsByMaterial(base.type)
                && base.getCustomItemType() == null
                && hasMultipleAttributes(base)

    override fun isCraftingItemValid(craftingItem: ItemStack) = craftingItem.isRefinement()

    override fun getResultPreview(base: ItemStack) = base

    override fun getResult(base: ItemStack, craftingItem: ItemStack): ItemStack {
        val attributes = base.getRolledAttributes()?.toMutableList() ?: return base
        val attributesBounds = ItemUtil.getEquipmentAttributes(base)

        val sacrificedAttribute = attributes.random()
        attributes.remove(sacrificedAttribute)

        val sacrificedAttributeBounds = attributesBounds.firstOrNull { it.type == sacrificedAttribute.type } ?: return base
        val sacrificedAttributePercentage = (sacrificedAttribute.roll - sacrificedAttributeBounds.min) / (sacrificedAttributeBounds.max - sacrificedAttributeBounds.min)

        val enhancedAttribute = attributes.random()
        val enhancedAttributeBounds = attributesBounds.firstOrNull { it.type == enhancedAttribute.type } ?: return base
        val enhancedAttributeRange = enhancedAttributeBounds.max - enhancedAttributeBounds.min

        enhancedAttribute.roll += RefinementSettings.refineAttributeValue(enhancedAttributeRange, sacrificedAttributePercentage)

        base.setRolledAttributes(attributes)
        return base
    }

    private fun hasMultipleAttributes(item: ItemStack): Boolean {
        val attributes = item.getRolledAttributes() ?: return false
        return attributes.size >= 2
    }
}