package de.fuballer.mcendgame.main.component.item.custom.armor.item.wither_rose

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.custom_attribute.types.VanillaAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesItem
import de.fuballer.mcendgame.main.component.item.custom.armor.interfaces.HidePlayerModelPartArmor
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.player.PlayerModelPart

class WitherRoseLeggings(
    settings: Properties,
) : UniqueAttributesItem(settings), HidePlayerModelPartArmor {
    override val hiddenPlayerModelParts = listOf(
        PlayerModelPart.LEFT_PANTS_LEG,
        PlayerModelPart.RIGHT_PANTS_LEG
    )

    override fun getCustomAttributes() = listOf(
        RollableCustomAttribute(VanillaAttributeTypes.MAX_HEALTH, 0, DoubleBounds(2.0, 3.0)),
        RollableCustomAttribute(CustomAttributeTypes.MORE_DAMAGE_TAKEN_PER_MAX_HEALTH_ABOVE_TWENTY, 0, DoubleBounds(-0.01, -0.01)),
    )

    override fun getAttributeModifierSlot() = EquipmentSlotGroup.LEGS
}