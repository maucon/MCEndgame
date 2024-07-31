package de.fuballer.mcendgame.component.crafting.roll_sacrifice

import de.fuballer.mcendgame.component.crafting.AnvilCraftingBaseService
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.AttributeRollExtension.run
import de.fuballer.mcendgame.util.extension.ItemStackExtension.getCustomAttributes
import de.fuballer.mcendgame.util.extension.ItemStackExtension.isCustomItemType
import de.fuballer.mcendgame.util.extension.ItemStackExtension.isRollSacrificeCraftingItem
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setCustomAttributes
import org.bukkit.inventory.ItemStack

@Component
class RollSacrificeService : AnvilCraftingBaseService() {
    override fun isBaseValid(base: ItemStack): Boolean {
        val rolledAttributes = base.getCustomAttributes() ?: return false
        val hasMultipleCustomAttribute = rolledAttributes.size >= 2

        return !base.isCustomItemType()
                && hasMultipleCustomAttribute
    }

    override fun isCraftingItemValid(craftingItem: ItemStack) = craftingItem.isRollSacrificeCraftingItem()

    override fun getResultPreview(base: ItemStack) = base

    override fun getResult(base: ItemStack, craftingItem: ItemStack): ItemStack {
        val attributes = base.getCustomAttributes()!!.toMutableList()

        val sacrificedAttribute = attributes.random()
        attributes.remove(sacrificedAttribute)

        val enhancedAttribute = attributes.random()
        enhancedAttribute.attributeRolls
            .forEach { attributesRoll ->
                attributesRoll.run(
                    { it.percentRoll += RollSacrificeSettings.REFINEMENT_ATTRIBUTE_VALUE },
                    {}, // string rolls do not change
                    { it.percentRoll += RollSacrificeSettings.REFINEMENT_ATTRIBUTE_VALUE }
                )
            }

        base.setCustomAttributes(attributes)
        return base
    }
}