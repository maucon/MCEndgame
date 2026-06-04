package de.fuballer.mcendgame.main.component.item.custom.tool.item

import de.fuballer.mcendgame.main.component.custom_attribute.data.IntBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesBowItem
import net.minecraft.world.entity.EquipmentSlotGroup

class Windstring(
    settings: Properties,
) : UniqueAttributesBowItem(settings) {
    override fun getCustomAttributes() = listOf(
        RollableCustomAttribute(CustomAttributeTypes.BOW_PULL_TICKS, 0, IntBounds(-6, -4)),
    )

    override fun getAttributeModifierSlot() = EquipmentSlotGroup.MAINHAND
}