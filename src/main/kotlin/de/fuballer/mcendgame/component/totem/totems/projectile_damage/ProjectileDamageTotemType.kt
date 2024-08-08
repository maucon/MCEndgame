package de.fuballer.mcendgame.component.totem.totems.projectile_damage

import de.fuballer.mcendgame.component.totem.TotemSettings
import de.fuballer.mcendgame.component.totem.data.TotemTier
import de.fuballer.mcendgame.component.totem.data.TotemType
import de.fuballer.mcendgame.util.PluginUtil
import net.kyori.adventure.text.Component
import org.bukkit.NamespacedKey

object ProjectileDamageTotemType : TotemType {
    private const val LORE_FORMAT = "Your Projectiles deal %s%% increased Damage."
    override val key: NamespacedKey = PluginUtil.createNamespacedKey("projectile_damage")
    override val displayName = "Totem of Impact"

    override fun getValues(tier: TotemTier) = when (tier) {
        TotemTier.COMMON -> listOf(0.1)
        TotemTier.UNCOMMON -> listOf(0.2)
        TotemTier.RARE -> listOf(0.3)
        TotemTier.LEGENDARY -> listOf(0.45)
    }

    override fun getLore(tier: TotemTier): List<Component> {
        val (increasedDamage) = getValues(tier)
        val values = listOf(increasedDamage * 100)

        return TotemSettings.formatLore(LORE_FORMAT, values)
    }
}