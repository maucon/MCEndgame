package de.fuballer.mcendgame.main.component.item.custom.armor.item.emberchant

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesItem
import net.minecraft.world.entity.EquipmentSlotGroup

class Emberchant(
    settings: Properties,
) : UniqueAttributesItem(settings) {
    override fun getCustomAttributes() = listOf(
        RollableCustomAttribute(CustomAttributeTypes.INCREASED_SPELL_DAMAGE, 0, DoubleBounds(0.1, 0.2)),
        RollableCustomAttribute(CustomAttributeTypes.BURNING_ENEMIES_EXPLODE_WHEN_KILLED, 0, DoubleBounds(0.8, 1.2)),
    )

    override fun getAttributeModifierSlot() = EquipmentSlotGroup.HEAD
}