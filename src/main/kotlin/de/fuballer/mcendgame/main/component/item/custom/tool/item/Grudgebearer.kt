package de.fuballer.mcendgame.main.component.item.custom.tool.item

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.IntBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesShieldItem
import net.minecraft.component.type.AttributeModifierSlot
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents

class Grudgebearer(
    settings: Settings,
) : UniqueAttributesShieldItem(settings) {
    override fun getCustomAttributes() = listOf(
        RollableCustomAttribute(CustomAttributeTypes.MORE_DAMAGE_TAKEN, 0, DoubleBounds(-0.06, -0.04)),
        RollableCustomAttribute(CustomAttributeTypes.SHIELD_DISABLED_ON_BLOCKING_HIT, 0, IntBounds(3)),
        RollableCustomAttribute(CustomAttributeTypes.INCREASED_DAMAGE_WHILE_SHIELD_DISABLED, 0, DoubleBounds(0.2, 0.3)),
    )

    override fun getAttributeModifierSlot() = AttributeModifierSlot.HAND

    override fun getBreakSound(): SoundEvent = SoundEvents.ITEM_SHIELD_BREAK
}