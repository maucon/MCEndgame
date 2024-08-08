package de.fuballer.mcendgame.component.totem.totems.movement_speed

import de.fuballer.mcendgame.component.totem.TotemSettings
import de.fuballer.mcendgame.component.totem.data.TotemTier
import de.fuballer.mcendgame.component.totem.data.TotemType
import de.fuballer.mcendgame.util.PluginUtil
import net.kyori.adventure.text.Component
import org.bukkit.NamespacedKey

object MovementSpeedTotemType : TotemType {
    private const val LORE_FORMAT = "You have %s%% increased Movement Speed"
    override val key: NamespacedKey = PluginUtil.createNamespacedKey("movement_speed")
    override val displayName = "Totem of Swiftness"

    override fun getValues(tier: TotemTier) = when (tier) {
        TotemTier.COMMON -> listOf(0.05)
        TotemTier.UNCOMMON -> listOf(0.1)
        TotemTier.RARE -> listOf(0.15)
        TotemTier.LEGENDARY -> listOf(0.25)
    }

    override fun getLore(tier: TotemTier): List<Component> {
        val (increasedSpeed) = getValues(tier)
        val values = listOf(increasedSpeed * 100)

        return TotemSettings.formatLore(LORE_FORMAT, values)
    }
}