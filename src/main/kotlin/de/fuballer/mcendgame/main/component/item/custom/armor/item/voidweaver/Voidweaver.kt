package de.fuballer.mcendgame.main.component.item.custom.armor.item.voidweaver

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.IntBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesItem
import de.fuballer.mcendgame.main.component.item.custom.armor.interfaces.HidePlayerModelPartArmor
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.player.PlayerModelPart

class Voidweaver(
    settings: Properties,
) : UniqueAttributesItem(settings), HidePlayerModelPartArmor {
    override fun getCustomAttributes() = listOf(
        RollableCustomAttribute(CustomAttributeTypes.LINK_NEARBY_ENEMIES, 0, IntBounds(4, 5)),
        RollableCustomAttribute(CustomAttributeTypes.DAMAGE_LINKED_ENEMIES, 0, DoubleBounds(0.4, 0.6)),
        RollableCustomAttribute(CustomAttributeTypes.HEAL_ON_LINKED_ENEMY_KILLED, 0, DoubleBounds(0.8, 1.2)),
        RollableCustomAttribute(CustomAttributeTypes.SPELL_DAMAGE, 0, DoubleBounds(2.0, 3.0)),
        RollableCustomAttribute(CustomAttributeTypes.WARD, 0, DoubleBounds(2.0, 3.0)),
    )

    override fun getAttributeModifierSlot() = EquipmentSlotGroup.CHEST

    override val hiddenPlayerModelParts = listOf(
        PlayerModelPart.LEFT_SLEEVE,
        PlayerModelPart.RIGHT_SLEEVE,
        PlayerModelPart.JACKET,
    )
}