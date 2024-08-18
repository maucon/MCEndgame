package de.fuballer.mcendgame.component.totem.totems.dodge

import de.fuballer.mcendgame.component.item.attribute.CustomAttributeTypes
import de.fuballer.mcendgame.component.item.attribute.data.AttributeRoll
import de.fuballer.mcendgame.component.item.attribute.data.DoubleBounds
import de.fuballer.mcendgame.component.item.attribute.data.DoubleRoll
import de.fuballer.mcendgame.component.totem.data.AttributeTotemType
import de.fuballer.mcendgame.component.totem.data.TotemTier
import de.fuballer.mcendgame.util.PluginUtil

object DodgeTotemType : AttributeTotemType {
    override val key = PluginUtil.createNamespacedKey("dodge")
    override val displayName = "Totem of Grace"
    override val attributeType = CustomAttributeTypes.DODGE_CHANCE

    override fun getAttributeRollsByTier(tier: TotemTier): List<AttributeRoll<*>> = when (tier) {
        TotemTier.COMMON -> listOf(DoubleRoll(DoubleBounds(0.1), 1.0))
        TotemTier.UNCOMMON -> listOf(DoubleRoll(DoubleBounds(0.2), 1.0))
        TotemTier.RARE -> listOf(DoubleRoll(DoubleBounds(0.3), 1.0))
        TotemTier.LEGENDARY -> listOf(DoubleRoll(DoubleBounds(0.45), 1.0))
    }
}