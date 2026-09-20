package de.fuballer.mcendgame.main.component.item.custom.armor.item.crown_of_the_stag

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.VanillaAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesItem
import net.minecraft.world.entity.EquipmentSlotGroup

class CrownOfTheStag(
    settings: Properties,
) : UniqueAttributesItem(settings) {
    override fun getCustomAttributes() = listOf(
        RollableCustomAttribute(VanillaAttributeTypes.MAX_HEALTH, 0, DoubleBounds(2.0, 3.0)),
    )

    override fun getAttributeModifierSlot() = EquipmentSlotGroup.HEAD
}