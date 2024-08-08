package de.fuballer.mcendgame.component.crafting.roll_randomization

import de.fuballer.mcendgame.component.crafting.AnvilCraftingBaseService
import de.fuballer.mcendgame.component.item.attribute.AttributeUtil
import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.util.extension.AttributeRollExtension.run
import de.fuballer.mcendgame.util.extension.ItemStackExtension.getCustomAttributes
import de.fuballer.mcendgame.util.extension.ItemStackExtension.isRollRandomizationCraftingItem
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setCustomAttributes
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

@Service
class RollRandomizationService : AnvilCraftingBaseService() {
    override fun isBaseValid(base: ItemStack) = AttributeUtil.hasCustomAttributesWithRolls(base)

    override fun isCraftingItemValid(craftingItem: ItemStack) = craftingItem.isRollRandomizationCraftingItem()

    override fun getResultPreview(base: ItemStack) = base

    override fun getResult(base: ItemStack, craftingItem: ItemStack): ItemStack {
        val attributes = base.getCustomAttributes()!!.toMutableList()
        attributes.forEach { attribute ->
            attribute.attributeRolls.forEach { attributeRoll ->
                attributeRoll.run(
                    { it.percentRoll = it.bounds.roll(Random.nextDouble()).percentRoll },
                    { it.indexRoll = it.bounds.roll(Random.nextDouble()).indexRoll },
                    { it.percentRoll = it.bounds.roll(Random.nextDouble()).percentRoll }
                )
            }
        }

        base.setCustomAttributes(attributes)
        return base
    }
}