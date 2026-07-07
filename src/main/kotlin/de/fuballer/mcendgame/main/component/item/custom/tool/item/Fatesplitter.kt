package de.fuballer.mcendgame.main.component.item.custom.tool.item

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.custom_attribute.types.VanillaAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesItem
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesSwordItem
import de.fuballer.mcendgame.main.component.item.custom.tool.BloodHarvestToolMaterial
import de.fuballer.mcendgame.main.component.item.custom.tool.FatesplitterToolMaterial
import net.minecraft.component.type.AttributeModifierSlot

class Fatesplitter(
    settings: Settings,
) : UniqueAttributesSwordItem(FatesplitterToolMaterial, settings) {
    override fun getCustomAttributes() = listOf(
        RollableCustomAttribute(VanillaAttributeTypes.ATTACK_DAMAGE, 0, DoubleBounds(2.0, 4.0)),
        RollableCustomAttribute(VanillaAttributeTypes.INCREASED_ATTACK_SPEED, 0, DoubleBounds(-0.3, -0.2)),
        RollableCustomAttribute(CustomAttributeTypes.MORE_DAMAGE_PER_MISSING_HEART, 0, DoubleBounds(0.03, 0.04)),
    )

    override fun getAttributeModifierSlot() = AttributeModifierSlot.MAINHAND
}