package de.fuballer.mcendgame.component.crafting.roll_shuffle

import de.fuballer.mcendgame.component.crafting.AnvilCraftingBaseService
import de.fuballer.mcendgame.component.item.attribute.AttributeUtil
import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.util.extension.AttributeRollExtension.extract
import de.fuballer.mcendgame.util.extension.AttributeRollExtension.run
import de.fuballer.mcendgame.util.extension.ItemStackExtension.getCustomAttributes
import de.fuballer.mcendgame.util.extension.ItemStackExtension.isRollShuffleCraftingItem
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setCustomAttributes
import org.bukkit.inventory.ItemStack

@Service
class RollShuffleService : AnvilCraftingBaseService() {
    override fun isBaseValid(base: ItemStack) = AttributeUtil.getNonStringOnlyRollAttributes(base).size >= 2

    override fun isCraftingItemValid(craftingItem: ItemStack) = craftingItem.isRollShuffleCraftingItem()

    override fun getResultPreview(base: ItemStack) = base

    override fun getResult(base: ItemStack, craftingItem: ItemStack): ItemStack {
        val attributes = base.getCustomAttributes()!!.toMutableList()
        val nonStringOnlyRollAttributes = AttributeUtil.getNonStringOnlyRollAttributes(base)

        val originalRolls = nonStringOnlyRollAttributes
            .flatMap { it.attributeRolls }
            .mapNotNull { attributeRoll ->
                attributeRoll.extract(
                    { it.percentRoll },
                    { null },
                    { it.percentRoll }
                )
            }

        var shuffledRolls: List<Double>
        var retryCount = 0

        do {
            shuffledRolls = originalRolls.shuffled()
            retryCount++
        } while (shuffledRolls == originalRolls && retryCount < RollShuffleSettings.SHUFFLE_TRIES)

        var index = 0
        attributes.forEach { attribute ->
            attribute.attributeRolls.forEach { attributeRoll ->
                attributeRoll.run(
                    { it.percentRoll = shuffledRolls[index++] },
                    {}, // do nothing on string roll
                    { it.percentRoll = shuffledRolls[index++] }
                )
            }
        }

        base.setCustomAttributes(attributes)
        return base
    }
}