package de.fuballer.mcendgame.component.totem.totems.wolf_companion

import de.fuballer.mcendgame.component.totem.TotemSettings
import de.fuballer.mcendgame.component.totem.data.TotemTier
import de.fuballer.mcendgame.component.totem.data.TotemType
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.NamespacedKey

object WolfCompanionTotemType : TotemType {
    private const val LORE_FORMAT = "You are accompanied by %s invincible wolfs.\\Your wolfs gains permanent Strength %s"
    override val key: NamespacedKey = PluginUtil.createNamespacedKey("wolf_companion")
    override val displayName = "Totem of Company"

    override fun getValues(tier: TotemTier) = when (tier) {
        TotemTier.COMMON -> listOf(2.0, 1.0)
        TotemTier.UNCOMMON -> listOf(2.0, 3.0)
        TotemTier.RARE -> listOf(3.0, 3.0)
        TotemTier.LEGENDARY -> listOf(4.0, 5.0)
    }

    override fun getLore(tier: TotemTier): List<String> {
        val values = getValues(tier)
        return TotemSettings.formatLore(LORE_FORMAT, values)
    }
}