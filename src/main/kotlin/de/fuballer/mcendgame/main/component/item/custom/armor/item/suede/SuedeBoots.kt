package de.fuballer.mcendgame.main.component.item.custom.armor.item.suede

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.IntBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.custom_attribute.types.VanillaAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesArmorItem
import de.fuballer.mcendgame.main.component.item.custom.armor.interfaces.HidePlayerModelPartArmor
import net.minecraft.component.type.AttributeModifierSlot
import net.minecraft.entity.player.PlayerModelPart
import net.minecraft.item.ArmorMaterial
import net.minecraft.registry.entry.RegistryEntry

class SuedeBoots(
    material: RegistryEntry<ArmorMaterial>,
    type: Type,
    settings: Settings,
) : UniqueAttributesArmorItem(material, type, settings), HidePlayerModelPartArmor {
    override fun getCustomAttributes() = listOf(
        RollableCustomAttribute(CustomAttributeTypes.DODGE, 0, DoubleBounds(0.1, 0.15)),
        RollableCustomAttribute(CustomAttributeTypes.MAGIC_FIND, 0, IntBounds(3, 5)),
        RollableCustomAttribute(VanillaAttributeTypes.INCREASED_MOVEMENT_SPEED, 0, DoubleBounds(0.1, 0.2)),
        RollableCustomAttribute(CustomAttributeTypes.INCREASED_MOVEMENT_SPEED_AFTER_DODGING, 0, DoubleBounds(0.15, 0.25), IntBounds(3, 5)),
    )

    override fun getAttributeModifierSlot() = AttributeModifierSlot.FEET

    override val hiddenPlayerModelParts = listOf(
        PlayerModelPart.LEFT_PANTS_LEG,
        PlayerModelPart.RIGHT_PANTS_LEG,
    )
}