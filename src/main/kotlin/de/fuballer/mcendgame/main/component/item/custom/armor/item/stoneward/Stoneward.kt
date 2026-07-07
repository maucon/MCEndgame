package de.fuballer.mcendgame.main.component.item.custom.armor.item.stoneward

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.custom_attribute.types.VanillaAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesArmorItem
import net.minecraft.component.type.AttributeModifierSlot
import net.minecraft.item.ArmorMaterial
import net.minecraft.registry.entry.RegistryEntry

class Stoneward(
    material: RegistryEntry<ArmorMaterial>,
    type: Type,
    settings: Settings,
) : UniqueAttributesArmorItem(material, type, settings) {
    override fun getCustomAttributes() = listOf(
        RollableCustomAttribute(CustomAttributeTypes.WARD, 0, DoubleBounds(2.0, 2.8)),
        RollableCustomAttribute(VanillaAttributeTypes.ARMOR, 0, DoubleBounds(1.0, 1.8)),
        RollableCustomAttribute(VanillaAttributeTypes.ARMOR_TOUGHNESS, 0, DoubleBounds(2.5, 4.0)),
        RollableCustomAttribute(VanillaAttributeTypes.INCREASED_SCALE, 0, DoubleBounds(0.05, 0.1)),
        RollableCustomAttribute(VanillaAttributeTypes.MORE_MOVEMENT_SPEED, 0, DoubleBounds(-0.2, -0.2)),
    )

    override fun getAttributeModifierSlot() = AttributeModifierSlot.LEGS
}