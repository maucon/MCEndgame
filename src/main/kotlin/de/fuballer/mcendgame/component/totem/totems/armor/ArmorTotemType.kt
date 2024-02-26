package de.fuballer.mcendgame.component.totem.totems.armor

import de.fuballer.mcendgame.component.totem.TotemSettings
import de.fuballer.mcendgame.component.totem.data.TotemTier
import de.fuballer.mcendgame.component.totem.data.TotemType
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.NamespacedKey

object ArmorTotemType : TotemType {
    private const val LORE_FORMAT = "You gain %s additional armor"
    override val key: NamespacedKey = PluginUtil.createNamespacedKey("armor")
    override val displayName = "Totem of Bastion"

    override fun getValues(tier: TotemTier) = when (tier) {
        TotemTier.COMMON -> listOf(3.0)
        TotemTier.UNCOMMON -> listOf(4.5)
        TotemTier.RARE -> listOf(6.0)
        TotemTier.LEGENDARY -> listOf(8.0)
    }

    override fun getLore(tier: TotemTier): List<String> {
        val values = getValues(tier)
        return TotemSettings.formatLore(LORE_FORMAT, values)
    }
}