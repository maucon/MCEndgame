package de.fuballer.mcendgame.main.component.item.custom.tool.item

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.IntBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.custom_attribute.types.VanillaAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesItem
import net.minecraft.world.entity.EquipmentSlotGroup

class RadiantDawn(
    settings: Properties,
) : UniqueAttributesItem(settings) {
    override fun getCustomAttributes() = listOf(
        RollableCustomAttribute(CustomAttributeTypes.HEAL_NEARBY_ALLIES_ON_MELEE_HIT, 0, IntBounds(10, 10), DoubleBounds(0.8, 1.2)),
        RollableCustomAttribute(CustomAttributeTypes.INCREASED_HEALING_PER_ELEMENTAL_DAMAGE, 0, DoubleBounds(0.03, 0.05)),
        RollableCustomAttribute(CustomAttributeTypes.ELEMENTAL_DAMAGE, 0, DoubleBounds(2.0, 4.0)),
        RollableCustomAttribute(VanillaAttributeTypes.INCREASED_ENTITY_INTERACTION_RANGE, 0, DoubleBounds(0.15, 0.15)),
    )

    override fun getAttributeModifierSlot() = EquipmentSlotGroup.MAINHAND
}