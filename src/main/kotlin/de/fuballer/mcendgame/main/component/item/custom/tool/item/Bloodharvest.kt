package de.fuballer.mcendgame.main.component.item.custom.tool.item

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.custom_attribute.types.VanillaAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesItem
import net.minecraft.world.entity.EquipmentSlotGroup

class Bloodharvest(
    settings: Properties,
) : UniqueAttributesItem(settings) {
    override fun getCustomAttributes() = listOf(
        RollableCustomAttribute(VanillaAttributeTypes.ATTACK_DAMAGE, 0, DoubleBounds(1.0, 3.0)),
        RollableCustomAttribute(VanillaAttributeTypes.INCREASED_ENTITY_INTERACTION_RANGE, 0, DoubleBounds(0.2, 0.2)),
        RollableCustomAttribute(CustomAttributeTypes.MORE_ATTACK_KNOCKBACK, 0, DoubleBounds(-0.5, -0.5)),
    )

    override fun getAttributeModifierSlot() = EquipmentSlotGroup.MAINHAND
}