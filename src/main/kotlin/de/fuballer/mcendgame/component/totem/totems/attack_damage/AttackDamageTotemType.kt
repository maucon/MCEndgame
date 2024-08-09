package de.fuballer.mcendgame.component.totem.totems.attack_damage

import de.fuballer.mcendgame.component.totem.TotemSettings
import de.fuballer.mcendgame.component.totem.data.TotemTier
import de.fuballer.mcendgame.component.totem.data.TotemType
import de.fuballer.mcendgame.util.PluginUtil
import net.kyori.adventure.text.Component
import org.bukkit.NamespacedKey

object AttackDamageTotemType : TotemType {
    private const val LORE_FORMAT = "You gain %s additional Attack Damage"
    override val key: NamespacedKey = PluginUtil.createNamespacedKey("attack_damage")
    override val displayName = "Totem of Force"

    override fun getValues(tier: TotemTier) = when (tier) {
        TotemTier.COMMON -> listOf(2.0)
        TotemTier.UNCOMMON -> listOf(4.0)
        TotemTier.RARE -> listOf(6.0)
        TotemTier.LEGENDARY -> listOf(10.0)
    }

    override fun getLore(tier: TotemTier): List<Component> {
        val values = getValues(tier)
        return TotemSettings.formatLore(LORE_FORMAT, values)
    }
}