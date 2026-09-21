package de.fuballer.mcendgame.main.component.item.custom.armor.item.skin_of_the_rhino

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesItem
import net.minecraft.world.entity.EquipmentSlotGroup

class SkinOfTheRhino(
    settings: Properties,
) : UniqueAttributesItem(settings) {
    override fun getCustomAttributes() = listOf(
        RollableCustomAttribute(CustomAttributeTypes.MORE_DAMAGE_TAKEN, 0, DoubleBounds(-0.15, -0.1)),
    )

    override fun getAttributeModifierSlot() = EquipmentSlotGroup.CHEST
}