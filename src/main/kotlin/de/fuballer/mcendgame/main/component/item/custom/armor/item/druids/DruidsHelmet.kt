package de.fuballer.mcendgame.main.component.item.custom.armor.item.druids

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.StringBounds
import de.fuballer.mcendgame.main.component.custom_attribute.effects.companion.wolf_companion.WolfCompanionType
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.custom_attribute.types.VanillaAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesArmorItem
import net.minecraft.component.type.AttributeModifierSlot
import net.minecraft.item.ArmorMaterial
import net.minecraft.registry.entry.RegistryEntry

class DruidsHelmet(
    material: RegistryEntry<ArmorMaterial>,
    type: Type,
    settings: Settings,
) : UniqueAttributesArmorItem(material, type, settings) {
    override fun getCustomAttributes() = listOf(
        RollableCustomAttribute(VanillaAttributeTypes.MAX_HEALTH, 0, DoubleBounds(1.0, 2.0)),
        RollableCustomAttribute(CustomAttributeTypes.WOLF_COMPANION, 0, StringBounds(WolfCompanionType.getNames())),
        RollableCustomAttribute(CustomAttributeTypes.WOLF_COMPANION, 0, StringBounds(WolfCompanionType.getNames())),
    )

    override fun getAttributeModifierSlot() = AttributeModifierSlot.HEAD
}