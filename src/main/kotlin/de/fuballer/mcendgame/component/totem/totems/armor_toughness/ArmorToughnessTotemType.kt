package de.fuballer.mcendgame.component.totem.totems.armor_toughness

import de.fuballer.mcendgame.component.item.attribute.VanillaAttributeTypes
import de.fuballer.mcendgame.component.item.attribute.data.AttributeRoll
import de.fuballer.mcendgame.component.item.attribute.data.DoubleBounds
import de.fuballer.mcendgame.component.item.attribute.data.DoubleRoll
import de.fuballer.mcendgame.component.totem.data.AttributeTotemType
import de.fuballer.mcendgame.component.totem.data.TotemTier
import de.fuballer.mcendgame.util.PluginUtil

object ArmorToughnessTotemType : AttributeTotemType {
    override val key = PluginUtil.createNamespacedKey("armor_toughness")
    override val displayName = "Totem of Vanguard"
    override val attributeType = VanillaAttributeTypes.ARMOR_TOUGHNESS

    override fun getAttributeRollsByTier(tier: TotemTier): List<AttributeRoll<*>> = when (tier) {
        TotemTier.COMMON -> listOf(DoubleRoll(DoubleBounds(1.5), 1.0))
        TotemTier.UNCOMMON -> listOf(DoubleRoll(DoubleBounds(2.5), 1.0))
        TotemTier.RARE -> listOf(DoubleRoll(DoubleBounds(3.5), 1.0))
        TotemTier.LEGENDARY -> listOf(DoubleRoll(DoubleBounds(5.0), 1.0))
    }
}