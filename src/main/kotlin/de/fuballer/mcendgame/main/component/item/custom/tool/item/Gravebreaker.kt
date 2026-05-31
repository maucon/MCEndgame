package de.fuballer.mcendgame.main.component.item.custom.tool.item

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.IntBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.custom_attribute.types.VanillaAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesMaceItem
import net.minecraft.component.type.AttributeModifierSlot
import net.minecraft.component.type.AttributeModifiersComponent
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes

class Gravebreaker(
    settings: Settings,
) : UniqueAttributesMaceItem(settings) {
    companion object {
        fun createAttributeModifiers(): AttributeModifiersComponent {
            return AttributeModifiersComponent.builder()
                .add(
                    EntityAttributes.ATTACK_DAMAGE,
                    EntityAttributeModifier(BASE_ATTACK_DAMAGE_MODIFIER_ID, 5.0, EntityAttributeModifier.Operation.ADD_VALUE),
                    AttributeModifierSlot.MAINHAND
                )
                .add(
                    EntityAttributes.ATTACK_SPEED,
                    EntityAttributeModifier(BASE_ATTACK_SPEED_MODIFIER_ID, -3.4, EntityAttributeModifier.Operation.ADD_VALUE),
                    AttributeModifierSlot.MAINHAND
                )
                .build()
        }
    }

    override fun getCustomAttributes() = listOf(
        RollableCustomAttribute(CustomAttributeTypes.MORE_ATTACK_DAMAGE_PER_ARMOR, 0, DoubleBounds(0.03, 0.04)),
        RollableCustomAttribute(CustomAttributeTypes.GAIN_ENEMY_ARMOR_ON_KILL, 0, DoubleBounds(0.08, 0.12), IntBounds(10)),
        RollableCustomAttribute(VanillaAttributeTypes.MORE_ATTACK_SPEED, 0, DoubleBounds(-0.25, -0.2)),
    )

    override fun getAttributeModifierSlot() = AttributeModifierSlot.MAINHAND
}