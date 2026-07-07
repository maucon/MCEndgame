package de.fuballer.mcendgame.main.component.item.custom.armor.item.wither_rose

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesArmorItem
import net.minecraft.component.type.AttributeModifierSlot
import net.minecraft.item.ArmorMaterial
import net.minecraft.registry.entry.RegistryEntry

class WitherRoseHelmet(
    material: RegistryEntry<ArmorMaterial>,
    type: Type,
    settings: Settings,
) : UniqueAttributesArmorItem(material, type, settings) {
    override fun getCustomAttributes() = listOf(
        RollableCustomAttribute(CustomAttributeTypes.SHOOT_WITHER_SKULL_WHEN_HIT_BY_PROJECTILE, 0, DoubleBounds(0.3, 0.6)),
        RollableCustomAttribute(CustomAttributeTypes.EXPLODE_WHEN_TAKING_DAMAGE, 0, DoubleBounds(0.25, 0.35)),
    )

    override fun getAttributeModifierSlot() = AttributeModifierSlot.HEAD
}