package de.fuballer.mcendgame.main.component.item.custom.crystal.item

import de.fuballer.mcendgame.main.component.block.blocks.crystalforge.CrystalForgeSettings
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.updateCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.data.AttributeRoll
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesItemInterface
import de.fuballer.mcendgame.main.component.item.custom.crystal.CrystalItem
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.item.ItemStack
import java.awt.Color

private val TIER_BASED_ENHANCE_VALUES = mapOf(
    1 to 0.5,
    2 to 0.35,
    3 to 0.25,
)

private fun getTierBasedEnhanceValue(tier: Int) = TIER_BASED_ENHANCE_VALUES[tier] ?: 0.0

class SacrificeCrystalItem(
    settings: Properties,
) : CrystalItem(settings) {
    override val forgeColor = Color(232, 40, 40)

    override val description: MutableComponent = Component.translatable(DESCRIPTION_BASE_KEY + "sacrifice")

    override fun canForge(
        stack: ItemStack,
        secondaryOutputSlotFilled: Boolean,
    ): MutableComponent? {
        val cannotForgeReason = super.canForge(stack, secondaryOutputSlotFilled)
        if (cannotForgeReason != null) return cannotForgeReason

        if (stack.item is UniqueAttributesItemInterface) return CrystalForgeSettings.getForgeErrorText("cannot_forge_unique")

        val attributes = stack.getCustomAttributes()
        if (attributes.size < 2) return CrystalForgeSettings.getForgeErrorText("not_enough_attributes")
        if (attributes.none { it.canBeEnhanced() }) return CrystalForgeSettings.getForgeErrorText("no_enhanceable_attribute")

        return null
    }

    override fun forge(stack: ItemStack): CrystalForgeOutput {
        val newStack = stack.copy()

        val oldAttributes = stack.getCustomAttributes()
        if (oldAttributes.size < 2) return CrystalForgeOutput(newStack)

        val enhanceableAttributes = oldAttributes.filter { it.canBeEnhanced() }
        val toEnhance = enhanceableAttributes.randomOrNull() ?: return CrystalForgeOutput(newStack)

        val remainingAttributes = oldAttributes.filter { it != toEnhance }
        val sacrifice = remainingAttributes.random()

        val enhancePercentage = getTierBasedEnhanceValue(sacrifice.tier)
        val enhanced = toEnhance.getSingleRollEnhanced(enhancePercentage, AttributeRoll.EnhancementType.SACRIFICE)

        val newAttributes = remainingAttributes.filter { it != sacrifice }.toMutableList()
        newAttributes.add(enhanced)
        newStack.updateCustomAttributes(newAttributes)

        return CrystalForgeOutput(newStack)
    }
}