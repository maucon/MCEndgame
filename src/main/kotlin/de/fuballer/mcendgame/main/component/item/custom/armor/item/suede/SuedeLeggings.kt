package de.fuballer.mcendgame.main.component.item.custom.armor.item.suede

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.IntBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.custom_attribute.types.VanillaAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesArmorItem
import net.minecraft.component.type.AttributeModifierSlot
import net.minecraft.item.ArmorMaterial
import net.minecraft.registry.entry.RegistryEntry

class SuedeLeggings(
    material: RegistryEntry<ArmorMaterial>,
    type: Type,
    settings: Settings,
) : UniqueAttributesArmorItem(material, type, settings) {
    override fun getCustomAttributes() = listOf(
        RollableCustomAttribute(CustomAttributeTypes.DODGE, 0, DoubleBounds(0.1, 0.15)),
        RollableCustomAttribute(VanillaAttributeTypes.MAX_HEALTH, 0, DoubleBounds(-4.0, -2.0)),
        RollableCustomAttribute(CustomAttributeTypes.MAGIC_FIND, 0, IntBounds(10, 15)),
        RollableCustomAttribute(CustomAttributeTypes.MAGIC_FIND_PER_MAX_HEART, 0, IntBounds(-1, -1)),
    )

    override fun getAttributeModifierSlot() = AttributeModifierSlot.LEGS
}