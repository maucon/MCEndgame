package de.fuballer.mcendgame.main.component.item.custom.armor.item.emberreign

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.IntBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.custom_attribute.types.VanillaAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesItem
import net.minecraft.component.type.AttributeModifierSlot

class Emberreign(
    settings: Settings,
) : UniqueAttributesItem(settings) {
    override fun getCustomAttributes() = listOf(
        RollableCustomAttribute(VanillaAttributeTypes.INCREASED_MOVEMENT_SPEED, 0, DoubleBounds(0.1, 0.15)),
        RollableCustomAttribute(CustomAttributeTypes.BURN_ENEMY_ON_WALK, 0, DoubleBounds(2.0, 2.5), IntBounds(5, 5), DoubleBounds(0.3, 0.4)),
        RollableCustomAttribute(CustomAttributeTypes.INCREASED_ELEMENTAL_DAMAGE, 0, DoubleBounds(0.2, 0.3)),
    )

    override fun getAttributeModifierSlot() = AttributeModifierSlot.FEET
}