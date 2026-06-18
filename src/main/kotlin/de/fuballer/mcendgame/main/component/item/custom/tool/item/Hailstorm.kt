package de.fuballer.mcendgame.main.component.item.custom.tool.item

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.IntBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesBowItem
import net.minecraft.world.entity.EquipmentSlotGroup

class Hailstorm(
    settings: Properties,
) : UniqueAttributesBowItem(settings) {
    override fun getCustomAttributes() = listOf(
        RollableCustomAttribute(CustomAttributeTypes.ADDITIONAL_PROJECTILES, 0, IntBounds(2, 2)),
        RollableCustomAttribute(CustomAttributeTypes.ELEMENTAL_DAMAGE, 0, DoubleBounds(1.0, 2.0)),
    )

    override fun getAttributeModifierSlot() = EquipmentSlotGroup.MAINHAND
}