package de.fuballer.mcendgame.component.totem.totems.experience

import de.fuballer.mcendgame.component.totem.TotemSettings
import de.fuballer.mcendgame.component.totem.data.TotemTier
import de.fuballer.mcendgame.component.totem.data.TotemType
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.NamespacedKey

object ExperienceTotemType : TotemType {
    private const val LORE_FORMAT = "You gain %s%% increased experience"
    override val key: NamespacedKey = PluginUtil.createNamespacedKey("experience")
    override val displayName = "Totem of Catalyst"

    override fun getValues(tier: TotemTier) = when (tier) {
        TotemTier.COMMON -> listOf(0.4)
        TotemTier.UNCOMMON -> listOf(0.8)
        TotemTier.RARE -> listOf(1.2)
        TotemTier.LEGENDARY -> listOf(2.0)
    }

    override fun getLore(tier: TotemTier): List<String> {
        val (experience) = getValues(tier)
        val values = listOf(experience * 100)

        return TotemSettings.formatLore(LORE_FORMAT, values)
    }
}