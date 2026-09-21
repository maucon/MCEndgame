package de.fuballer.mcendgame.main.component.item.custom.armor.item.skin_of_the_rhino

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.custom_attribute.types.VanillaAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesItem
import net.minecraft.world.entity.EquipmentSlotGroup

class SkinOfTheRhino(
    settings: Properties,
) : UniqueAttributesItem(settings) {
    override fun getCustomAttributes() = listOf(
        RollableCustomAttribute(VanillaAttributeTypes.ARMOR_TOUGHNESS, 0, DoubleBounds(3.0, 4.0)),
        RollableCustomAttribute(VanillaAttributeTypes.MAX_HEALTH, 0, DoubleBounds(1.5, 2.5)),
        RollableCustomAttribute(CustomAttributeTypes.FLAT_DAMAGE_TAKEN, 0, DoubleBounds(-0.8, -0.6)),
    )

    override fun getAttributeModifierSlot() = EquipmentSlotGroup.CHEST
}