package de.fuballer.mcendgame.main.component.item.custom.tool.item

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.custom_attribute.types.VanillaAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesItem
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesSwordItem
import de.fuballer.mcendgame.main.component.item.custom.tool.BloodHarvestToolMaterial
import de.fuballer.mcendgame.main.component.item.custom.tool.TwinfireToolMaterial
import net.minecraft.component.type.AttributeModifierSlot

class Twinfire(
    settings: Settings,
) : UniqueAttributesSwordItem(TwinfireToolMaterial, settings) {
    override fun getCustomAttributes() = listOf(
        getFlatDamageAttribute(),
        RollableCustomAttribute(CustomAttributeTypes.DODGE, 0, DoubleBounds(0.05, 0.08)),
        RollableCustomAttribute(CustomAttributeTypes.TWINFIRE_DUAL_WIELD_MORE_DAMAGE, 0, DoubleBounds(0.05, 0.1)),
    )

    private fun getFlatDamageAttribute() = listOf(
        RollableCustomAttribute(VanillaAttributeTypes.ATTACK_DAMAGE, 0, DoubleBounds(1.0, 2.0)),
        RollableCustomAttribute(CustomAttributeTypes.ELEMENTAL_DAMAGE, 0, DoubleBounds(1.0, 2.0)),
    ).random()

    override fun getAttributeModifierSlot() = AttributeModifierSlot.HAND
}