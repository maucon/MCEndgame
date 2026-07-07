package de.fuballer.mcendgame.main.component.item.custom.armor.item.druids

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.IntBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.custom_attribute.types.VanillaAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesArmorItem
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesItem
import net.minecraft.component.type.AttributeModifierSlot
import net.minecraft.item.ArmorItem.Type
import net.minecraft.item.ArmorMaterial
import net.minecraft.item.Item.Settings
import net.minecraft.registry.entry.RegistryEntry

class DruidsChestplate(
    material: RegistryEntry<ArmorMaterial>,
    type: Type,
    settings: Settings,
) : UniqueAttributesArmorItem(material, type, settings) {
    override fun getCustomAttributes() = listOf(
        RollableCustomAttribute(VanillaAttributeTypes.MAX_HEALTH, 0, DoubleBounds(1.0, 3.0)),
        RollableCustomAttribute(CustomAttributeTypes.FURY_ON_KILL, 0, IntBounds(0, 0)),
        RollableCustomAttribute(CustomAttributeTypes.RESILIENCE_ON_DAMAGE_TAKEN, 0, IntBounds(0, 0)),
    )

    override fun getAttributeModifierSlot() = AttributeModifierSlot.CHEST
}