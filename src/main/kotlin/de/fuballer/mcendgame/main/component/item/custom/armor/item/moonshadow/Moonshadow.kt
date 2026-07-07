package de.fuballer.mcendgame.main.component.item.custom.armor.item.moonshadow

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.custom_attribute.types.VanillaAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesArmorItem
import net.minecraft.component.type.AttributeModifierSlot
import net.minecraft.item.ArmorMaterial
import net.minecraft.registry.entry.RegistryEntry

class Moonshadow(
    material: RegistryEntry<ArmorMaterial>,
    type: Type,
    settings: Settings,
) : UniqueAttributesArmorItem(material, type, settings) {
    override fun getCustomAttributes() = listOf(
        RollableCustomAttribute(CustomAttributeTypes.DODGE, 0, DoubleBounds(0.1, 0.2)),
        RollableCustomAttribute(VanillaAttributeTypes.INCREASED_MOVEMENT_SPEED, 0, DoubleBounds(0.05, 0.15)),
    )

    override fun getAttributeModifierSlot() = AttributeModifierSlot.FEET
}