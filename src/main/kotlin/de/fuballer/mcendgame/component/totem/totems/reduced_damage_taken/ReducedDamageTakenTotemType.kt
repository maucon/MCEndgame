package de.fuballer.mcendgame.component.totem.totems.reduced_damage_taken

import de.fuballer.mcendgame.component.item.attribute.CustomAttributeTypes
import de.fuballer.mcendgame.component.item.attribute.data.AttributeRoll
import de.fuballer.mcendgame.component.item.attribute.data.DoubleBounds
import de.fuballer.mcendgame.component.item.attribute.data.DoubleRoll
import de.fuballer.mcendgame.component.totem.data.AttributeTotemType
import de.fuballer.mcendgame.component.totem.data.TotemTier
import de.fuballer.mcendgame.util.PluginUtil

object ReducedDamageTakenTotemType : AttributeTotemType {
    override val key = PluginUtil.createNamespacedKey("reduced_damage_taken")
    override val displayName = "Totem of Fortress"
    override val attributeType = CustomAttributeTypes.REDUCED_DAMAGE_TAKEN

    override fun getAttributeRollsByTier(tier: TotemTier): List<AttributeRoll<*>> = when (tier) {
        TotemTier.COMMON -> listOf(DoubleRoll(DoubleBounds(0.05), 1.0))
        TotemTier.UNCOMMON -> listOf(DoubleRoll(DoubleBounds(0.1), 1.0))
        TotemTier.RARE -> listOf(DoubleRoll(DoubleBounds(0.15), 1.0))
        TotemTier.LEGENDARY -> listOf(DoubleRoll(DoubleBounds(0.22), 1.0))
    }
}