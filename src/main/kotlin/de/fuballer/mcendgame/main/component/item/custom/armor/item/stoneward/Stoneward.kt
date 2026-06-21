package de.fuballer.mcendgame.main.component.item.custom.armor.item.stoneward

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.custom_attribute.types.VanillaAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesItem
import net.minecraft.world.entity.EquipmentSlotGroup

class Stoneward(
    settings: Properties,
) : UniqueAttributesItem(settings) {
    override fun getCustomAttributes() = listOf(
        RollableCustomAttribute(CustomAttributeTypes.SPELL_RESISTANCE, 0, DoubleBounds(0.2, 0.28)),
        RollableCustomAttribute(VanillaAttributeTypes.ARMOR, 0, DoubleBounds(1.0, 1.8)),
        RollableCustomAttribute(VanillaAttributeTypes.ARMOR_TOUGHNESS, 0, DoubleBounds(2.5, 4.0)),
        RollableCustomAttribute(VanillaAttributeTypes.INCREASED_SCALE, 0, DoubleBounds(0.05, 0.1)),
        RollableCustomAttribute(VanillaAttributeTypes.MORE_MOVEMENT_SPEED, 0, DoubleBounds(-0.2, -0.2)),
    )

    override fun getAttributeModifierSlot() = EquipmentSlotGroup.LEGS
}