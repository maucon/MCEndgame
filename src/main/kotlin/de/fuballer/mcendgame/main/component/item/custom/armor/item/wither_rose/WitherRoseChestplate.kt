package de.fuballer.mcendgame.main.component.item.custom.armor.item.wither_rose

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.custom_attribute.types.VanillaAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesItem
import net.minecraft.world.entity.EquipmentSlotGroup

class WitherRoseChestplate(
    settings: Properties,
) : UniqueAttributesItem(settings) {
    override fun getCustomAttributes() = listOf(
        RollableCustomAttribute(CustomAttributeTypes.WITHER_DAMAGE_IMMUNITY, 0),
        RollableCustomAttribute(CustomAttributeTypes.INCREASED_DAMAGE_WHILE_WITHERED, 0, DoubleBounds(0.04, 0.06)),
        RollableCustomAttribute(CustomAttributeTypes.INCREASED_ATTACK_DAMAGE_WHILE_WITHERED, 0, DoubleBounds(0.08, 0.12)),
        RollableCustomAttribute(CustomAttributeTypes.ARMOR_WHILE_WITHERED, 0, DoubleBounds(2.5, 4.0)),
        RollableCustomAttribute(VanillaAttributeTypes.INCREASED_SCALE, 0, DoubleBounds(0.05, 0.15)),
    )

    override fun getAttributeModifierSlot() = EquipmentSlotGroup.CHEST
}