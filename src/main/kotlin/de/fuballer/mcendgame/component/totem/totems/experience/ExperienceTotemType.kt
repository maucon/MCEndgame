package de.fuballer.mcendgame.component.totem.totems.experience

import de.fuballer.mcendgame.component.item.attribute.CustomAttributeTypes
import de.fuballer.mcendgame.component.item.attribute.data.AttributeRoll
import de.fuballer.mcendgame.component.item.attribute.data.DoubleBounds
import de.fuballer.mcendgame.component.item.attribute.data.DoubleRoll
import de.fuballer.mcendgame.component.totem.data.AttributeTotemType
import de.fuballer.mcendgame.component.totem.data.TotemTier
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.NamespacedKey

object ExperienceTotemType : AttributeTotemType {
    override val key: NamespacedKey = PluginUtil.createNamespacedKey("experience")
    override val displayName = "Totem of Catalyst"
    override val attributeType = CustomAttributeTypes.INCREASED_EXPERIENCE

    override fun getAttributeRollsByTier(tier: TotemTier): List<AttributeRoll<*>> = when (tier) {
        TotemTier.COMMON -> listOf(DoubleRoll(DoubleBounds(1.0), 1.0))
        TotemTier.UNCOMMON -> listOf(DoubleRoll(DoubleBounds(2.0), 1.0))
        TotemTier.RARE -> listOf(DoubleRoll(DoubleBounds(3.0), 1.0))
        TotemTier.LEGENDARY -> listOf(DoubleRoll(DoubleBounds(5.0), 1.0))
    }
}