package de.fuballer.mcendgame.main.component.item.custom.armor.item.windstrider

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.custom_attribute.types.VanillaAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesArmorItem
import de.fuballer.mcendgame.main.component.item.custom.armor.interfaces.HidePlayerModelPartArmor
import net.minecraft.component.type.AttributeModifierSlot
import net.minecraft.entity.player.PlayerModelPart
import net.minecraft.item.ArmorMaterial
import net.minecraft.registry.entry.RegistryEntry

class Windstrider(
    material: RegistryEntry<ArmorMaterial>,
    type: Type,
    settings: Settings,
) : UniqueAttributesArmorItem(material, type, settings), HidePlayerModelPartArmor {
    override fun getCustomAttributes() = listOf(
        RollableCustomAttribute(CustomAttributeTypes.PROJECTILE_DODGE, 0, DoubleBounds(0.3, 0.4)),
        RollableCustomAttribute(CustomAttributeTypes.DODGED_PROJECTILE_REFLECT, 0),
        RollableCustomAttribute(VanillaAttributeTypes.INCREASED_MOVEMENT_SPEED, 0, DoubleBounds(0.15, 0.25)),
        RollableCustomAttribute(VanillaAttributeTypes.INCREASED_JUMP_STRENGTH, 0, DoubleBounds(0.15, 0.25)),
    )

    override fun getAttributeModifierSlot() = AttributeModifierSlot.LEGS

    override val hiddenPlayerModelParts = listOf(
        PlayerModelPart.LEFT_PANTS_LEG,
        PlayerModelPart.RIGHT_PANTS_LEG,
    )
}