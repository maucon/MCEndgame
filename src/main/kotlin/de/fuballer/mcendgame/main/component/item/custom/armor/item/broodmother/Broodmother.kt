package de.fuballer.mcendgame.main.component.item.custom.armor.item.broodmother

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.IntBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesItem
import de.fuballer.mcendgame.main.component.item.custom.armor.interfaces.HidePlayerModelPartArmor
import de.fuballer.mcendgame.main.component.item.custom.armor.interfaces.ItemWithCape
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.player.PlayerModelPart

class Broodmother(
    settings: Properties,
) : UniqueAttributesItem(settings), HidePlayerModelPartArmor, ItemWithCape {
    override fun getCustomAttributes() = listOf(
        RollableCustomAttribute(CustomAttributeTypes.POISON_DAMAGE_IMMUNITY, 0),
        RollableCustomAttribute(CustomAttributeTypes.SPIDERLING_COMPANIONS, 0, IntBounds(2, 3)),
        RollableCustomAttribute(CustomAttributeTypes.COMPANION_ATTACK_DAMAGE, 0, DoubleBounds(2.0, 3.0)),
    )

    override fun getAttributeModifierSlot() = EquipmentSlotGroup.CHEST

    override val hiddenPlayerModelParts = listOf(
        PlayerModelPart.LEFT_SLEEVE,
        PlayerModelPart.RIGHT_SLEEVE,
        PlayerModelPart.JACKET,
        PlayerModelPart.CAPE,
    )
}