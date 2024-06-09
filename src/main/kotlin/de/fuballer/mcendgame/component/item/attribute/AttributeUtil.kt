package de.fuballer.mcendgame.component.item.attribute

import de.fuballer.mcendgame.component.item.attribute.data.BaseAttribute
import de.fuballer.mcendgame.component.item.attribute.data.CustomAttribute
import de.fuballer.mcendgame.component.item.attribute.data.RollType
import de.fuballer.mcendgame.component.item.attribute.data.SingleValueAttribute
import de.fuballer.mcendgame.util.extension.ItemStackExtension.getCustomAttributes
import org.bukkit.inventory.ItemStack

object AttributeUtil {
    fun getRollableCustomAttributes(item: ItemStack): List<CustomAttribute> {
        val attributes = item.getCustomAttributes() ?: return listOf()
        return attributes.filter { it.rollType != RollType.STATIC }
    }

    fun getCorrectSignLore(attribute: CustomAttribute): String {
        val roll = when (attribute.rollType) {
            RollType.STATIC -> 0.0
            RollType.SINGLE -> (attribute as SingleValueAttribute).getAbsoluteRoll()

        }
        val lore = attribute.type.lore(roll)

        return getCorrectSignLoreBase(lore, roll)
    }

    fun getCorrectSignLore(attribute: BaseAttribute): String {
        val lore = attribute.type.lore(attribute.roll)
        return getCorrectSignLoreBase(lore, attribute.roll)
    }

    private fun getCorrectSignLoreBase(
        lore: String,
        roll: Double
    ): String {
        if (roll >= 0) return lore
        if (!lore.startsWith("+")) return lore

        return lore.replaceFirstChar { "" }
    }
}