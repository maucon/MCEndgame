package de.fuballer.mcendgame.main.component.item.custom.tool.item

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesItem
import net.minecraft.world.entity.EquipmentSlotGroup

class Twinfire(
    settings: Properties,
) : UniqueAttributesItem(settings) {
    override fun getCustomAttributes() = listOf(
        RollableCustomAttribute(CustomAttributeTypes.SPELL_DAMAGE, 0, DoubleBounds(1.0, 2.0)),
        RollableCustomAttribute(CustomAttributeTypes.MORE_DAMAGE_DUAL_WIELD, 0, DoubleBounds(0.08, 0.12)),
        RollableCustomAttribute(CustomAttributeTypes.DODGE, 0, DoubleBounds(0.1, 0.15)),
        RollableCustomAttribute(CustomAttributeTypes.ATTRIBUTES_APPLY_IN_BOTH_HANDS, 0),
    )

    override fun getAttributeModifierSlot() = EquipmentSlotGroup.HAND
}