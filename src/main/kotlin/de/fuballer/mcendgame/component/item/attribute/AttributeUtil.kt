package de.fuballer.mcendgame.component.item.attribute

import de.fuballer.mcendgame.component.item.attribute.data.CustomAttribute
import de.fuballer.mcendgame.component.item.attribute.data.StringRoll
import de.fuballer.mcendgame.util.extension.ItemStackExtension.getCustomAttributes
import org.bukkit.inventory.ItemStack

object AttributeUtil {
    fun hasCustomAttributesWithRolls(item: ItemStack) =
        getCustomAttributesWithRolls(item).isNotEmpty()

    fun getCustomAttributesWithRolls(item: ItemStack): List<CustomAttribute> {
        val attributes = item.getCustomAttributes() ?: return listOf()
        return attributes
            .filter { it.attributeRolls.isNotEmpty() }
    }

    fun getNonStringOnlyRollAttributes(item: ItemStack): List<CustomAttribute> {
        val rolledAttributes = item.getCustomAttributes() ?: return listOf()
        return rolledAttributes
            .filter { attribute -> attribute.attributeRolls.any { it !is StringRoll } }
    }
}