package de.fuballer.mcendgame.main.component.item.custom.tool.item

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.custom_attribute.types.VanillaAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesItem
import net.minecraft.world.entity.EquipmentSlotGroup

class Twinfire(
    settings: Properties,
) : UniqueAttributesItem(settings) {
    override fun getCustomAttributes() = listOf(
        getFlatDamageAttribute(),
        RollableCustomAttribute(CustomAttributeTypes.DODGE, 0, DoubleBounds(0.05, 0.08)),
        RollableCustomAttribute(CustomAttributeTypes.TWINFIRE_DUAL_WIELD_MORE_DAMAGE, 0, DoubleBounds(0.05, 0.1)),
    )

    private fun getFlatDamageAttribute() = listOf(
        RollableCustomAttribute(VanillaAttributeTypes.ATTACK_DAMAGE, 0, DoubleBounds(1.0, 2.0)),
        RollableCustomAttribute(CustomAttributeTypes.SPELL_DAMAGE, 0, DoubleBounds(1.0, 2.0)),
    ).random()

    override fun getAttributeModifierSlot() = EquipmentSlotGroup.HAND
}