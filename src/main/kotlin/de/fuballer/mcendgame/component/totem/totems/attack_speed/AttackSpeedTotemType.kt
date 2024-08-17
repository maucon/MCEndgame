package de.fuballer.mcendgame.component.totem.totems.attack_speed

import de.fuballer.mcendgame.component.item.attribute.VanillaAttributeTypes
import de.fuballer.mcendgame.component.item.attribute.data.AttributeRoll
import de.fuballer.mcendgame.component.item.attribute.data.DoubleBounds
import de.fuballer.mcendgame.component.item.attribute.data.DoubleRoll
import de.fuballer.mcendgame.component.totem.data.AttributeTotemType
import de.fuballer.mcendgame.component.totem.data.TotemTier
import de.fuballer.mcendgame.util.PluginUtil

object AttackSpeedTotemType : AttributeTotemType {
    override val key = PluginUtil.createNamespacedKey("attack_speed")
    override val displayName = "Totem of Frenzy"
    override val attributeType = VanillaAttributeTypes.ATTACK_SPEED

    override fun getAttributeRollsByTier(tier: TotemTier): List<AttributeRoll<*>> = when (tier) {
        TotemTier.COMMON -> listOf(DoubleRoll(DoubleBounds(0.1), 1.0))
        TotemTier.UNCOMMON -> listOf(DoubleRoll(DoubleBounds(0.17), 1.0))
        TotemTier.RARE -> listOf(DoubleRoll(DoubleBounds(0.25), 1.0))
        TotemTier.LEGENDARY -> listOf(DoubleRoll(DoubleBounds(0.4), 1.0))
    }
}