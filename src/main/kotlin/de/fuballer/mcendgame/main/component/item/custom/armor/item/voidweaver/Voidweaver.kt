package de.fuballer.mcendgame.main.component.item.custom.armor.item.voidweaver

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.IntBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesArmorItem
import de.fuballer.mcendgame.main.component.item.custom.armor.interfaces.HidePlayerModelPartArmor
import net.minecraft.component.type.AttributeModifierSlot
import net.minecraft.entity.player.PlayerModelPart
import net.minecraft.item.ArmorMaterial
import net.minecraft.registry.entry.RegistryEntry

class Voidweaver(
    material: RegistryEntry<ArmorMaterial>,
    type: Type,
    settings: Settings,
) : UniqueAttributesArmorItem(material, type, settings), HidePlayerModelPartArmor {
    override fun getCustomAttributes() = listOf(
        RollableCustomAttribute(CustomAttributeTypes.LINK_NEARBY_ENEMIES, 0, IntBounds(4, 5)),
        RollableCustomAttribute(CustomAttributeTypes.DAMAGE_LINKED_ENEMIES, 0, DoubleBounds(0.4, 0.6)),
        RollableCustomAttribute(CustomAttributeTypes.HEAL_ON_LINKED_ENEMY_KILLED, 0, DoubleBounds(0.8, 1.2)),
        RollableCustomAttribute(CustomAttributeTypes.ELEMENTAL_DAMAGE, 0, DoubleBounds(2.0, 3.0)),
        RollableCustomAttribute(CustomAttributeTypes.WARD, 0, DoubleBounds(2.0, 3.0)),
    )

    override fun getAttributeModifierSlot() = AttributeModifierSlot.CHEST

    override val hiddenPlayerModelParts = listOf(
        PlayerModelPart.LEFT_SLEEVE,
        PlayerModelPart.RIGHT_SLEEVE,
        PlayerModelPart.JACKET,
    )
}