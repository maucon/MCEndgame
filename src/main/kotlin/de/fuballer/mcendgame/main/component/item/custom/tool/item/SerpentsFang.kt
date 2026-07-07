package de.fuballer.mcendgame.main.component.item.custom.tool.item

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.IntBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesItem
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesSwordItem
import de.fuballer.mcendgame.main.component.item.custom.tool.BloodHarvestToolMaterial
import de.fuballer.mcendgame.main.component.item.custom.tool.SerpentsFangToolMaterial
import net.minecraft.component.type.AttributeModifierSlot

class SerpentsFang(
    settings: Settings,
) : UniqueAttributesSwordItem(SerpentsFangToolMaterial, settings) {
    override fun getCustomAttributes() = listOf(
        RollableCustomAttribute(CustomAttributeTypes.STACKING_MORE_ATTACK_SPEED_ON_MELEE_HIT, 0, listOf(DoubleBounds(0.03, 0.05), IntBounds(4, 6))),
        RollableCustomAttribute(CustomAttributeTypes.MORE_DAMAGE, 0, listOf(DoubleBounds(-0.2, -0.15))),
        RollableCustomAttribute(CustomAttributeTypes.MORE_ATTACK_KNOCKBACK, 0, listOf(DoubleBounds(-0.5, -0.35))),
        RollableCustomAttribute(CustomAttributeTypes.INCREASED_MOVEMENT_SPEED_WHILE_POISONED, 0, listOf(DoubleBounds(0.15, 0.2))),
    )

    override fun getAttributeModifierSlot() = AttributeModifierSlot.MAINHAND
}