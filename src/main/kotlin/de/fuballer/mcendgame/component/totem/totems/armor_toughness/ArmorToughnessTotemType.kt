package de.fuballer.mcendgame.component.totem.totems.armor_toughness

import de.fuballer.mcendgame.component.totem.TotemSettings
import de.fuballer.mcendgame.component.totem.data.TotemTier
import de.fuballer.mcendgame.component.totem.data.TotemType
import de.fuballer.mcendgame.util.PluginUtil
import net.kyori.adventure.text.Component
import org.bukkit.NamespacedKey

object ArmorToughnessTotemType : TotemType {
    private const val LORE_FORMAT = "You gain %s additional Armor Toughness"
    override val key: NamespacedKey = PluginUtil.createNamespacedKey("armor_toughness")
    override val displayName = "Totem of Vanguard"

    override fun getValues(tier: TotemTier) = when (tier) {
        TotemTier.COMMON -> listOf(1.5)
        TotemTier.UNCOMMON -> listOf(2.5)
        TotemTier.RARE -> listOf(3.5)
        TotemTier.LEGENDARY -> listOf(5.0)
    }

    override fun getLore(tier: TotemTier): List<Component> {
        val values = getValues(tier)
        return TotemSettings.formatLore(LORE_FORMAT, values)
    }
}