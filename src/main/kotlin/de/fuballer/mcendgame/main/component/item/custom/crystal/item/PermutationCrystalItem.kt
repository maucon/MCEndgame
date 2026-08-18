package de.fuballer.mcendgame.main.component.item.custom.crystal.item

import de.fuballer.mcendgame.main.component.block.blocks.crystalforge.CrystalForgeSettings
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.updateCustomAttributes
import de.fuballer.mcendgame.main.component.item.custom.crystal.CrystalItem
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.item.ItemStack
import java.awt.Color

class PermutationCrystalItem(
    settings: Properties,
) : CrystalItem(settings) {
    override val forgeColor = Color(176, 232, 40)

    override val description: MutableComponent = Component.translatable(DESCRIPTION_BASE_KEY + "permutation")

    override fun canForge(
        stack: ItemStack,
        secondaryOutputSlotFilled: Boolean,
    ): MutableComponent? {
        val cannotForgeReason = super.canForge(stack, secondaryOutputSlotFilled)
        if (cannotForgeReason != null) return cannotForgeReason

        val attributes = stack.getCustomAttributes()

        val viableRollCount = attributes.sumOf { it.getNonZeroRangePercentRollCount() }
        if (viableRollCount < 2) return CrystalForgeSettings.getForgeErrorText("not_enough_rolls_to_shuffle")

        val rolls = attributes.flatMap { it.getPercentRolls() }
        if (rolls.distinct().size < 2) return CrystalForgeSettings.getForgeErrorText("all_rolls_equal")

        return null
    }

    override fun forge(stack: ItemStack): CrystalForgeOutput {
        val newStack = stack.copy()

        val oldAttributes = stack.getCustomAttributes()
        if (oldAttributes.isEmpty()) return CrystalForgeOutput(newStack)

        val oldRolls = oldAttributes.flatMap { it.getPercentRolls() }
        if (oldRolls.size < 2) return CrystalForgeOutput(newStack)

        var shuffledRolls: List<Double>
        if (oldRolls.distinct().size > 1) {
            do {
                shuffledRolls = oldRolls.shuffled()
            } while (shuffledRolls == oldRolls)
        } else {
            shuffledRolls = oldRolls.toList()
        }

        var rollsUsed = 0
        val newAttributes = oldAttributes.map {
            val neededRolls = it.getNonZeroRangePercentRollCount()
            if (neededRolls == 0) return@map it

            val newRolls = shuffledRolls.subList(rollsUsed, rollsUsed + neededRolls).toList()
            rollsUsed += neededRolls

            it.getWithRollPercentages(newRolls)
        }

        newStack.updateCustomAttributes(newAttributes)

        return CrystalForgeOutput(newStack)
    }
}