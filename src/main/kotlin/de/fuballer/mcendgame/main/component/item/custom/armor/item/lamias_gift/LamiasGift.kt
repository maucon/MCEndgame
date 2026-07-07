package de.fuballer.mcendgame.main.component.item.custom.armor.item.lamias_gift

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.IntBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesArmorItem
import de.fuballer.mcendgame.main.component.item.custom.armor.interfaces.HideBipedBoneArmor
import de.fuballer.mcendgame.main.component.item.custom.armor.interfaces.HideOtherArmorArmor
import net.minecraft.component.type.AttributeModifierSlot
import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.ArmorMaterial
import net.minecraft.registry.entry.RegistryEntry

class LamiasGift(
    material: RegistryEntry<ArmorMaterial>,
    type: Type,
    settings: Settings,
) : UniqueAttributesArmorItem(material, type, settings), HideBipedBoneArmor, HideOtherArmorArmor {
    override val hiddenBones = listOf(
        HideBipedBoneArmor.BipedBone.LEGS,
    )
    override val hiddenArmor = listOf(
        EquipmentSlot.FEET,
    )

    override fun getAttributeModifierSlot() = AttributeModifierSlot.LEGS

    override fun getCustomAttributes() = listOf(
        RollableCustomAttribute(CustomAttributeTypes.POISON_DAMAGE_IMMUNITY, 0),
        getOffensiveAttribute(),
        getDefensiveAttribute(),
        getMiscAttribute(),
    )

    private fun getOffensiveAttribute() = listOf(
        RollableCustomAttribute(CustomAttributeTypes.INCREASED_DAMAGE_WHILE_POISONED, 0, DoubleBounds(0.04, 0.06)),
        RollableCustomAttribute(CustomAttributeTypes.INCREASED_ATTACK_DAMAGE_WHILE_POISONED, 0, DoubleBounds(0.1, 0.15)),
        RollableCustomAttribute(CustomAttributeTypes.INCREASED_ELEMENTAL_DAMAGE_WHILE_POISONED, 0, DoubleBounds(0.1, 0.2)),
    ).random()

    private fun getDefensiveAttribute() = listOf(
        RollableCustomAttribute(CustomAttributeTypes.MORE_DAMAGE_TAKEN_WHILE_POISONED, 0, DoubleBounds(-0.08, -0.05)),
        RollableCustomAttribute(CustomAttributeTypes.DODGE_WHILE_POISONED, 0, DoubleBounds(0.06, 0.09)),
    ).random()

    private fun getMiscAttribute() = listOf(
        RollableCustomAttribute(CustomAttributeTypes.INCREASED_MOVEMENT_SPEED_WHILE_POISONED, 0, DoubleBounds(.15, .25)),
        RollableCustomAttribute(CustomAttributeTypes.MAGIC_FIND_WHILE_POISONED, 0, IntBounds(3, 5)),
    ).random()
}