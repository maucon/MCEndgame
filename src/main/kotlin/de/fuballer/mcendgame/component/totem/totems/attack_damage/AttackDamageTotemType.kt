package de.fuballer.mcendgame.component.totem.totems.attack_damage

import de.fuballer.mcendgame.component.item.attribute.VanillaAttributeTypes
import de.fuballer.mcendgame.component.item.attribute.data.AttributeRoll
import de.fuballer.mcendgame.component.item.attribute.data.DoubleBounds
import de.fuballer.mcendgame.component.item.attribute.data.DoubleRoll
import de.fuballer.mcendgame.component.totem.data.AttributeTotemType
import de.fuballer.mcendgame.component.totem.data.TotemTier
import de.fuballer.mcendgame.util.PluginUtil

object AttackDamageTotemType : AttributeTotemType {
    override val key = PluginUtil.createNamespacedKey("attack_damage")
    override val displayName = "Totem of Force"
    override val attributeType = VanillaAttributeTypes.ATTACK_DAMAGE

    override fun getAttributeRollsByTier(tier: TotemTier): List<AttributeRoll<*>> = when (tier) {
        TotemTier.COMMON -> listOf(DoubleRoll(DoubleBounds(2.0), 1.0))
        TotemTier.UNCOMMON -> listOf(DoubleRoll(DoubleBounds(4.0), 1.0))
        TotemTier.RARE -> listOf(DoubleRoll(DoubleBounds(6.0), 1.0))
        TotemTier.LEGENDARY -> listOf(DoubleRoll(DoubleBounds(10.0), 1.0))
    }
}