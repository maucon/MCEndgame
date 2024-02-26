package de.fuballer.mcendgame.component.totem.totems.projectile_damage

import de.fuballer.mcendgame.component.totem.TotemSettings
import de.fuballer.mcendgame.component.totem.data.TotemTier
import de.fuballer.mcendgame.component.totem.data.TotemType
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.NamespacedKey

object ProjectileDamageTotemType : TotemType {
    private const val LORE_FORMAT = "Your projectiles deal %s%% increased damage."
    override val key: NamespacedKey = PluginUtil.createNamespacedKey("projectile_damage")
    override val displayName = "Totem of Impact"

    override fun getValues(tier: TotemTier) = when (tier) {
        TotemTier.COMMON -> listOf(15.0)
        TotemTier.UNCOMMON -> listOf(30.0)
        TotemTier.RARE -> listOf(45.0)
        TotemTier.LEGENDARY -> listOf(75.0)
    }

    override fun getLore(tier: TotemTier): List<String> {
        val values = getValues(tier)
        return TotemSettings.formatLore(LORE_FORMAT, values)
    }
}