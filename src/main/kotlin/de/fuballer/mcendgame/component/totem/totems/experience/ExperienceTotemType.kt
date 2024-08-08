package de.fuballer.mcendgame.component.totem.totems.experience

import de.fuballer.mcendgame.component.totem.TotemSettings
import de.fuballer.mcendgame.component.totem.data.TotemTier
import de.fuballer.mcendgame.component.totem.data.TotemType
import de.fuballer.mcendgame.util.PluginUtil
import net.kyori.adventure.text.Component
import org.bukkit.NamespacedKey

object ExperienceTotemType : TotemType {
    private const val LORE_FORMAT = "You gain %s%% increased Experience"
    override val key: NamespacedKey = PluginUtil.createNamespacedKey("experience")
    override val displayName = "Totem of Catalyst"

    override fun getValues(tier: TotemTier) = when (tier) {
        TotemTier.COMMON -> listOf(1.0)
        TotemTier.UNCOMMON -> listOf(2.0)
        TotemTier.RARE -> listOf(3.0)
        TotemTier.LEGENDARY -> listOf(5.0)
    }

    override fun getLore(tier: TotemTier): List<Component> {
        val (experience) = getValues(tier)
        val values = listOf(experience * 100)

        return TotemSettings.formatLore(LORE_FORMAT, values)
    }
}