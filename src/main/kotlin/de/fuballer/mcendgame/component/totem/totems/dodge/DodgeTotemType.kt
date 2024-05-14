package de.fuballer.mcendgame.component.totem.totems.dodge

import de.fuballer.mcendgame.component.totem.TotemSettings
import de.fuballer.mcendgame.component.totem.data.TotemTier
import de.fuballer.mcendgame.component.totem.data.TotemType
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.NamespacedKey

object DodgeTotemType : TotemType {
    private const val LORE_FORMAT = "You gain %s%% chance to dodge hits"
    override val key: NamespacedKey = PluginUtil.createNamespacedKey("dodge")
    override val displayName = "Totem of Grace"

    override fun getValues(tier: TotemTier) = when (tier) {
        TotemTier.COMMON -> listOf(0.05)
        TotemTier.UNCOMMON -> listOf(0.1)
        TotemTier.RARE -> listOf(0.15)
        TotemTier.LEGENDARY -> listOf(0.2)
    }

    override fun getLore(tier: TotemTier): List<String> {
        val (dodge) = getValues(tier)
        val values = listOf(dodge * 100)

        return TotemSettings.formatLore(LORE_FORMAT, values)
    }
}