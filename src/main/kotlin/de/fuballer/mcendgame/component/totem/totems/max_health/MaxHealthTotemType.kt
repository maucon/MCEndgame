package de.fuballer.mcendgame.component.totem.totems.max_health

import de.fuballer.mcendgame.component.item.attribute.VanillaAttributeTypes
import de.fuballer.mcendgame.component.item.attribute.data.AttributeRoll
import de.fuballer.mcendgame.component.item.attribute.data.DoubleBounds
import de.fuballer.mcendgame.component.item.attribute.data.DoubleRoll
import de.fuballer.mcendgame.component.totem.data.AttributeTotemType
import de.fuballer.mcendgame.component.totem.data.TotemTier
import de.fuballer.mcendgame.util.PluginUtil

object MaxHealthTotemType : AttributeTotemType {
    override val key = PluginUtil.createNamespacedKey("max_health")
    override val displayName = "Totem of Thickness" // name chosen by xX20Erik01Xx
    override val attributeType = VanillaAttributeTypes.MAX_HEALTH

    override fun getAttributeRollsByTier(tier: TotemTier): List<AttributeRoll<*>> = when (tier) {
        TotemTier.COMMON -> listOf(DoubleRoll(DoubleBounds(2.0), 1.0))
        TotemTier.UNCOMMON -> listOf(DoubleRoll(DoubleBounds(3.5), 1.0))
        TotemTier.RARE -> listOf(DoubleRoll(DoubleBounds(5.0), 1.0))
        TotemTier.LEGENDARY -> listOf(DoubleRoll(DoubleBounds(7.0), 1.0))
    }
}