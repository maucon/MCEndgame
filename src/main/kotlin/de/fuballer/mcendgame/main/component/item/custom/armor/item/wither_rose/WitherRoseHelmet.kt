package de.fuballer.mcendgame.main.component.item.custom.armor.item.wither_rose

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesItem
import net.minecraft.world.entity.EquipmentSlotGroup

class WitherRoseHelmet(
    settings: Properties,
) : UniqueAttributesItem(settings) {
    override fun getCustomAttributes() = listOf(
        RollableCustomAttribute(CustomAttributeTypes.SHOOT_WITHER_SKULL_WHEN_HIT_BY_PROJECTILE, 0, DoubleBounds(0.3, 0.6)),
        RollableCustomAttribute(CustomAttributeTypes.EXPLODE_WHEN_TAKING_DAMAGE, 0, DoubleBounds(0.25, 0.35)),
    )

    override fun getAttributeModifierSlot() = EquipmentSlotGroup.HEAD
}