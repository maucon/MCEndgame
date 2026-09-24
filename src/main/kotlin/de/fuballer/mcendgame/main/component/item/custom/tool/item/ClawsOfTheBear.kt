package de.fuballer.mcendgame.main.component.item.custom.tool.item

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.custom_attribute.types.VanillaAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesItem
import net.minecraft.world.entity.EquipmentSlotGroup

class ClawsOfTheBear(
    settings: Properties,
) : UniqueAttributesItem(settings) {
    override fun getCustomAttributes() = listOf(
        RollableCustomAttribute(VanillaAttributeTypes.ATTACK_DAMAGE, 0, DoubleBounds(1.5, 2.5)),
        RollableCustomAttribute(CustomAttributeTypes.MORE_ATTACK_SPEED_DUAL_WIELD, 0, DoubleBounds(0.12, 0.18)),
        RollableCustomAttribute(CustomAttributeTypes.HEAL_ON_MELEE_HIT, 0, DoubleBounds(0.2, 0.4)),
    )

    override fun getAttributeModifierSlot() = EquipmentSlotGroup.HAND
}