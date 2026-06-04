package de.fuballer.mcendgame.main.component.item.custom.tool.item

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.IntBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.custom_attribute.types.VanillaAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesMaceItem
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.item.component.ItemAttributeModifiers

class Gravebreaker(
    settings: Properties,
) : UniqueAttributesMaceItem(settings) {
    companion object {
        fun createAttributeModifiers(): ItemAttributeModifiers {
            return ItemAttributeModifiers.builder()
                .add(
                    Attributes.ATTACK_DAMAGE,
                    AttributeModifier(BASE_ATTACK_DAMAGE_ID, 5.0, AttributeModifier.Operation.ADD_VALUE),
                    EquipmentSlotGroup.MAINHAND
                )
                .add(
                    Attributes.ATTACK_SPEED,
                    AttributeModifier(BASE_ATTACK_SPEED_ID, -3.4, AttributeModifier.Operation.ADD_VALUE),
                    EquipmentSlotGroup.MAINHAND
                )
                .build()
        }
    }

    override fun getCustomAttributes() = listOf(
        RollableCustomAttribute(CustomAttributeTypes.MORE_ATTACK_DAMAGE_PER_ARMOR, 0, DoubleBounds(0.03, 0.04)),
        RollableCustomAttribute(CustomAttributeTypes.GAIN_ENEMY_ARMOR_ON_KILL, 0, DoubleBounds(0.08, 0.12), IntBounds(10)),
        RollableCustomAttribute(VanillaAttributeTypes.MORE_ATTACK_SPEED, 0, DoubleBounds(-0.25, -0.2)),
    )

    override fun getAttributeModifierSlot() = EquipmentSlotGroup.MAINHAND
}