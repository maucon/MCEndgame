package de.fuballer.mcendgame.main.component.item.custom.armor.item.wither_rose

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.custom_attribute.types.VanillaAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesArmorItem
import net.minecraft.component.type.AttributeModifierSlot
import net.minecraft.item.ArmorMaterial
import net.minecraft.registry.entry.RegistryEntry

class WitherRoseChestplate(
    material: RegistryEntry<ArmorMaterial>,
    type: Type,
    settings: Settings,
) : UniqueAttributesArmorItem(material, type, settings) {
    override fun getCustomAttributes() = listOf(
        RollableCustomAttribute(CustomAttributeTypes.WITHER_DAMAGE_IMMUNITY, 0),
        RollableCustomAttribute(CustomAttributeTypes.INCREASED_DAMAGE_WHILE_WITHERED, 0, DoubleBounds(0.04, 0.06)),
        RollableCustomAttribute(CustomAttributeTypes.INCREASED_ATTACK_DAMAGE_WHILE_WITHERED, 0, DoubleBounds(0.08, 0.12)),
        RollableCustomAttribute(CustomAttributeTypes.ARMOR_WHILE_WITHERED, 0, DoubleBounds(2.5, 4.0)),
        RollableCustomAttribute(VanillaAttributeTypes.INCREASED_SCALE, 0, DoubleBounds(0.05, 0.15)),
    )

    override fun getAttributeModifierSlot() = AttributeModifierSlot.CHEST
}