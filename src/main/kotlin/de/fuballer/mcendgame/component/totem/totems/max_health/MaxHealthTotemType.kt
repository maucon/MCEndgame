package de.fuballer.mcendgame.component.totem.totems.max_health

import de.fuballer.mcendgame.component.totem.TotemSettings
import de.fuballer.mcendgame.component.totem.data.TotemTier
import de.fuballer.mcendgame.component.totem.data.TotemType
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.NamespacedKey

object MaxHealthTotemType : TotemType {
    private const val LORE_FORMAT = "You have %s more health"
    override val key: NamespacedKey = PluginUtil.createNamespacedKey("max_health")
    override val displayName = "Totem of Thickness" // name chosen by xX20Erik01Xx

    override fun getValues(tier: TotemTier) = when (tier) {
        TotemTier.COMMON -> listOf(1.0)
        TotemTier.UNCOMMON -> listOf(2.0)
        TotemTier.RARE -> listOf(3.0)
        TotemTier.LEGENDARY -> listOf(5.0)
    }

    override fun getLore(tier: TotemTier): List<String> {
        val values = getValues(tier)
        return TotemSettings.formatLore(LORE_FORMAT, values)
    }
}