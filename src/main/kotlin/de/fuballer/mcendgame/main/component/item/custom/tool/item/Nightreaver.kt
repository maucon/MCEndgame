package de.fuballer.mcendgame.main.component.item.custom.tool.item

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.IntBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesItem
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesSwordItem
import de.fuballer.mcendgame.main.component.item.custom.tool.BloodHarvestToolMaterial
import de.fuballer.mcendgame.main.component.item.custom.tool.NightreaverToolMaterial
import net.minecraft.component.type.AttributeModifierSlot

class Nightreaver(
    settings: Settings,
) : UniqueAttributesSwordItem(NightreaverToolMaterial, settings) {
    override fun getCustomAttributes() = listOf(
        RollableCustomAttribute(CustomAttributeTypes.CRITICAL_DAMAGE_MULTIPLIER, 0, DoubleBounds(0.15, 0.25)),
        RollableCustomAttribute(CustomAttributeTypes.INCREASED_MOVEMENT_SPEED_ON_KILL, 0, DoubleBounds(0.2, 0.3), IntBounds(3, 3)),
        RollableCustomAttribute(CustomAttributeTypes.MORE_DAMAGE_AGAINST_ISOLATED, 0, DoubleBounds(0.1, 0.2)),
        RollableCustomAttribute(CustomAttributeTypes.STEALTH, 0),
    )

    override fun getAttributeModifierSlot() = AttributeModifierSlot.MAINHAND
}