package de.fuballer.mcendgame.main.component.item.custom.crystal.item

import de.fuballer.mcendgame.main.component.block.blocks.crystalforge.CrystalForgeSettings
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.updateCustomAttributes
import de.fuballer.mcendgame.main.component.item.custom.crystal.CrystalItem
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.item.ItemStack
import java.awt.Color

class CalibrationCrystalItem(
    settings: Properties,
) : CrystalItem(settings) {
    override val forgeColor = Color(40, 179, 232)

    override val description: MutableComponent = Component.translatable(DESCRIPTION_BASE_KEY + "calibration")

    override fun canForge(
        stack: ItemStack,
        secondaryOutputSlotFilled: Boolean,
    ): MutableComponent? {
        val cannotForgeReason = super.canForge(stack, secondaryOutputSlotFilled)
        if (cannotForgeReason != null) return cannotForgeReason

        val attributes = stack.getCustomAttributes().filter { it.hasNonZeroRange() }
        if (attributes.isEmpty()) return CrystalForgeSettings.getForgeErrorText("no_attribute_with_range")

        return null
    }

    override fun forge(stack: ItemStack): CrystalForgeOutput {
        val newStack = stack.copy()

        val oldAttributes = stack.getCustomAttributes()
        if (oldAttributes.isEmpty()) return CrystalForgeOutput(newStack)

        val newAttributes = oldAttributes.map { it.getRerolled() }
        newStack.updateCustomAttributes(newAttributes)

        return CrystalForgeOutput(newStack)
    }
}