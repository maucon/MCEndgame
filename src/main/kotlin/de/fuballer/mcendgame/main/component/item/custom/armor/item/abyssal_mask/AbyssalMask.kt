package de.fuballer.mcendgame.main.component.item.custom.armor.item.abyssal_mask

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.StringBounds
import de.fuballer.mcendgame.main.component.custom_attribute.effects.change_gained_status_effect.ChangeGainedStatusEffectSettings
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesItem
import de.fuballer.mcendgame.main.component.item.custom.armor.interfaces.HidePlayerModelPartArmor
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.player.PlayerModelPart

class AbyssalMask(
    settings: Properties,
) : UniqueAttributesItem(settings), HidePlayerModelPartArmor {
    override fun getCustomAttributes(): List<RollableCustomAttribute> {
        val chosenConversions = ChangeGainedStatusEffectSettings.getStatusEffectPairs(2)
        val attributes = mutableListOf<RollableCustomAttribute>(
            RollableCustomAttribute(CustomAttributeTypes.MORE_DAMAGE_TAKEN, 0, DoubleBounds(-0.06, -0.04))
        )

        chosenConversions.forEach { conversion ->
            attributes.add(
                RollableCustomAttribute(
                    CustomAttributeTypes.CHANGE_GAINED_STATUS_EFFECT,
                    0,
                    StringBounds(conversion.first.displayName),
                    StringBounds(conversion.second.displayName)
                )
            )
            attributes.add(
                RollableCustomAttribute(
                    CustomAttributeTypes.CHANGE_GAINED_STATUS_EFFECT,
                    0,
                    StringBounds(conversion.second.displayName),
                    StringBounds(conversion.first.displayName)
                )
            )
        }

        return attributes
    }

    override fun getAttributeModifierSlot() = EquipmentSlotGroup.HEAD

    override val hiddenPlayerModelParts = listOf(
        PlayerModelPart.HAT,
    )
}