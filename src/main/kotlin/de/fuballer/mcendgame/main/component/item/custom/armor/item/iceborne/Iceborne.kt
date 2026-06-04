package de.fuballer.mcendgame.main.component.item.custom.armor.item.iceborne

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesItem
import net.minecraft.world.entity.EquipmentSlotGroup

class Iceborne(
    settings: Properties,
) : UniqueAttributesItem(settings) {
    override fun getCustomAttributes() = listOf(
        RollableCustomAttribute(CustomAttributeTypes.STRONGER_HORNS, 0),
        RollableCustomAttribute(CustomAttributeTypes.MORE_HORN_EFFECT_DURATION, 0, DoubleBounds(0.3, 0.5)),
        RollableCustomAttribute(CustomAttributeTypes.MORE_HORN_COOLDOWN, 0, DoubleBounds(-0.3, -0.2)),
    )

    override fun getAttributeModifierSlot() = EquipmentSlotGroup.HEAD
}