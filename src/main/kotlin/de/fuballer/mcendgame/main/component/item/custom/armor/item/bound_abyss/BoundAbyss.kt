package de.fuballer.mcendgame.main.component.item.custom.armor.item.bound_abyss

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.custom_attribute.types.VanillaAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesArmorItem
import net.minecraft.component.type.AttributeModifierSlot
import net.minecraft.item.ArmorMaterial
import net.minecraft.registry.entry.RegistryEntry

class BoundAbyss(
    material: RegistryEntry<ArmorMaterial>,
    type: Type,
    settings: Settings,
) : UniqueAttributesArmorItem(material, type, settings) {
    override fun getCustomAttributes() = listOf(
        RollableCustomAttribute(CustomAttributeTypes.MORE_DAMAGE_TAKEN_WHILE_HIGH_HEALTH, 0, DoubleBounds(-0.1, -0.05)),
        RollableCustomAttribute(CustomAttributeTypes.INCREASED_DAMAGE_WHILE_LOW_HEALTH, 0, DoubleBounds(.1, .15)),
        RollableCustomAttribute(VanillaAttributeTypes.MAX_HEALTH, 0, DoubleBounds(3.0, 4.0)),
    )

    override fun getAttributeModifierSlot() = AttributeModifierSlot.CHEST
}