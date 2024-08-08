package de.fuballer.mcendgame.component.totem.totems.attack_speed

import de.fuballer.mcendgame.component.totem.TotemSettings
import de.fuballer.mcendgame.component.totem.data.TotemTier
import de.fuballer.mcendgame.component.totem.data.TotemType
import de.fuballer.mcendgame.util.PluginUtil
import net.kyori.adventure.text.Component
import org.bukkit.NamespacedKey

object AttackSpeedTotemType : TotemType {
    private const val LORE_FORMAT = "You have %s%% increased Attack Speed"
    override val key: NamespacedKey = PluginUtil.createNamespacedKey("attack_speed")
    override val displayName = "Totem of Frenzy"

    override fun getValues(tier: TotemTier) = when (tier) {
        TotemTier.COMMON -> listOf(0.1)
        TotemTier.UNCOMMON -> listOf(0.17)
        TotemTier.RARE -> listOf(0.25)
        TotemTier.LEGENDARY -> listOf(0.4)
    }

    override fun getLore(tier: TotemTier): List<Component> {
        val (increasedAttackSpeed) = getValues(tier)
        val values = listOf(increasedAttackSpeed * 100)

        return TotemSettings.formatLore(LORE_FORMAT, values)
    }
}