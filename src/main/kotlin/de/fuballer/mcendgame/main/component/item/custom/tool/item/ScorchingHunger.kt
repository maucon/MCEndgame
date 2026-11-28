package de.fuballer.mcendgame.main.component.item.custom.tool.item

import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesItem
import net.minecraft.world.entity.EquipmentSlotGroup

class ScorchingHunger(
    settings: Properties,
) : UniqueAttributesItem(settings) {
    override fun getCustomAttributes() = listOf(
        RollableCustomAttribute(CustomAttributeTypes.ABSORB_STAT_FROM_SLAIN_BOSS, 0),
        RollableCustomAttribute(CustomAttributeTypes.ABSORB_STAT_FROM_SLAIN_BOSS, 0),
        RollableCustomAttribute(CustomAttributeTypes.ABSORB_STAT_FROM_SLAIN_BOSS, 0),
    )

    override fun getAttributeModifierSlot() = EquipmentSlotGroup.MAINHAND
}