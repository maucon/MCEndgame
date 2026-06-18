package de.fuballer.mcendgame.main.component.item.custom.armor.item.moonshadow

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.IntBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.custom_attribute.types.VanillaAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesItem
import net.minecraft.world.entity.EquipmentSlotGroup

class Moonshadow(
    settings: Properties,
) : UniqueAttributesItem(settings) {
    override fun getCustomAttributes() = listOf(
        RollableCustomAttribute(CustomAttributeTypes.DODGE, 0, DoubleBounds(0.1, 0.2)),
        RollableCustomAttribute(CustomAttributeTypes.ADDITIONAL_PROJECTILES, 0, IntBounds(2, 2)),
        RollableCustomAttribute(VanillaAttributeTypes.INCREASED_MOVEMENT_SPEED, 0, DoubleBounds(0.05, 0.15)),
    )

    override fun getAttributeModifierSlot() = EquipmentSlotGroup.FEET
}