package de.fuballer.mcendgame.component.totem.totems.armor_toughness

import de.fuballer.mcendgame.component.totem.TotemSettings
import de.fuballer.mcendgame.component.totem.data.TotemTier
import de.fuballer.mcendgame.component.totem.data.TotemType
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.NamespacedKey

object ArmorToughnessTotemType : TotemType {
    private const val LORE_FORMAT = "You gain %s additional armor toughness"
    override val key: NamespacedKey = PluginUtil.createNamespacedKey("armor_toughness")
    override val displayName = "Totem of Vanguard"

    override fun getValues(tier: TotemTier) = when (tier) {
        TotemTier.COMMON -> listOf(3.0)
        TotemTier.UNCOMMON -> listOf(5.0)
        TotemTier.RARE -> listOf(8.0)
        TotemTier.LEGENDARY -> listOf(12.0)
    }

    override fun getLore(tier: TotemTier): List<String> {
        val values = getValues(tier)
        return TotemSettings.formatLore(LORE_FORMAT, values)
    }
}