package de.fuballer.mcendgame.main.component.item.custom.armor.item.gilded_tempest

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.custom_attribute.types.VanillaAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesItem
import de.fuballer.mcendgame.main.component.item.custom.armor.interfaces.HidePlayerModelPartArmor
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.player.PlayerModelPart

class GildedTempest(
    settings: Properties,
) : UniqueAttributesItem(settings), HidePlayerModelPartArmor {
    override fun getCustomAttributes() = listOf(
        RollableCustomAttribute(CustomAttributeTypes.INCREASED_MOVEMENT_SPEED_MODIFIERS_AFFECT_DAMAGE, 0, DoubleBounds(0.3, 0.5)),
        RollableCustomAttribute(CustomAttributeTypes.MORE_MOVEMENT_SPEED_MODIFIERS_AFFECT_DAMAGE, 0, DoubleBounds(0.1, 0.15)),
        RollableCustomAttribute(VanillaAttributeTypes.INCREASED_MOVEMENT_SPEED, 0, DoubleBounds(0.1, 0.15)),
    )

    override fun getAttributeModifierSlot() = EquipmentSlotGroup.LEGS

    override val hiddenPlayerModelParts = listOf(
        PlayerModelPart.LEFT_PANTS_LEG,
        PlayerModelPart.RIGHT_PANTS_LEG,
    )
}