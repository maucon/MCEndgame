package de.fuballer.mcendgame.main.component.item.custom.armor.item.druids

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.StringBounds
import de.fuballer.mcendgame.main.component.custom_attribute.effects.companion.wolf_companion.WolfCompanionType
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.custom_attribute.types.VanillaAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesItem
import net.minecraft.world.entity.EquipmentSlotGroup

class DruidsHelmet(
    settings: Properties,
) : UniqueAttributesItem(settings) {
    override fun getCustomAttributes() = listOf(
        RollableCustomAttribute(VanillaAttributeTypes.MAX_HEALTH, 0, DoubleBounds(1.0, 2.0)),
        RollableCustomAttribute(CustomAttributeTypes.WOLF_COMPANION, 0, StringBounds(WolfCompanionType.getNames())),
        RollableCustomAttribute(CustomAttributeTypes.WOLF_COMPANION, 0, StringBounds(WolfCompanionType.getNames())),
    )

    override fun getAttributeModifierSlot() = EquipmentSlotGroup.HEAD
}