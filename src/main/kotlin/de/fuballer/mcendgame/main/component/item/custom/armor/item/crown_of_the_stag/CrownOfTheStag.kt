package de.fuballer.mcendgame.main.component.item.custom.armor.item.crown_of_the_stag

import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesItem
import net.minecraft.world.entity.EquipmentSlotGroup

private val BLESSING_ATTRIBUTE_TYPES = listOf(
    CustomAttributeTypes.BLESSING_OF_THE_BEAR,
    CustomAttributeTypes.BLESSING_OF_THE_EAGLE,
    CustomAttributeTypes.BLESSING_OF_THE_MAMMOTH,
    CustomAttributeTypes.BLESSING_OF_THE_RHINO,
    CustomAttributeTypes.BLESSING_OF_THE_SERPENT,
    CustomAttributeTypes.BLESSING_OF_THE_STAG,
    CustomAttributeTypes.BLESSING_OF_THE_WOLF,
)

class CrownOfTheStag(
    settings: Properties,
) : UniqueAttributesItem(settings) {
    override fun getCustomAttributes() = listOf(
        getRandomBlessingAttribute(),
        getRandomBlessingAttribute(),
        getRandomBlessingAttribute(),
    )

    private fun getRandomBlessingAttribute() = RollableCustomAttribute(BLESSING_ATTRIBUTE_TYPES.random(), 0)

    override fun getAttributeModifierSlot() = EquipmentSlotGroup.HEAD
}