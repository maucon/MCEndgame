package de.fuballer.mcendgame.component.crafting.transfiguration

import de.fuballer.mcendgame.component.crafting.AnvilCraftingBaseService
import de.fuballer.mcendgame.component.item.attribute.AttributeUtil
import de.fuballer.mcendgame.component.item.attribute.data.RollType
import de.fuballer.mcendgame.component.item.attribute.data.SingleValueAttribute
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.ItemStackExtension.getCustomAttributes
import de.fuballer.mcendgame.util.extension.ItemStackExtension.isTransfiguration
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setCustomAttributes
import org.bukkit.inventory.ItemStack
import java.util.*

@Component
class TransfigurationService : AnvilCraftingBaseService() {
    override fun isBaseValid(base: ItemStack) = hasMultipleRollableAttributes(base)

    override fun isCraftingItemValid(craftingItem: ItemStack) = craftingItem.isTransfiguration()

    override fun getResultPreview(base: ItemStack) = base

    override fun getResult(base: ItemStack, craftingItem: ItemStack): ItemStack {
        val attributes = base.getCustomAttributes()!!.shuffled()
        val percentRolls = attributes
            .filter { it.rollType == RollType.SINGLE }
            .map { (it as SingleValueAttribute).percentRoll }
            .toMutableList()

        Collections.rotate(percentRolls, 1)

        attributes.onEach {
            if (it.rollType != RollType.SINGLE) return@onEach

            val attribute = it as SingleValueAttribute
            attribute.percentRoll = percentRolls.removeAt(0)
        }

        base.setCustomAttributes(attributes)
        return base
    }

    private fun hasMultipleRollableAttributes(item: ItemStack): Boolean {
        val attributes = AttributeUtil.getSingleValueAttributes(item)
        return attributes.size >= 2
    }
}