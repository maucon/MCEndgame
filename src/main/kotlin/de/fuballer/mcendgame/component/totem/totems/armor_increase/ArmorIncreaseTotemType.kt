package de.fuballer.mcendgame.component.totem.totems.armor_increase

import de.fuballer.mcendgame.component.totem.TotemSettings
import de.fuballer.mcendgame.component.totem.data.TotemTier
import de.fuballer.mcendgame.component.totem.data.TotemType
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.NamespacedKey

object ArmorIncreaseTotemType : TotemType {
    private const val LORE_FORMAT = "You gain %s%% increased armor"
    override val key: NamespacedKey = PluginUtil.createNamespacedKey("armor_increase")
    override val displayName = "Totem of Fortress"

    override fun getValues(tier: TotemTier) = when (tier) {
        TotemTier.COMMON -> listOf(0.10)
        TotemTier.UNCOMMON -> listOf(0.15)
        TotemTier.RARE -> listOf(0.2)
        TotemTier.LEGENDARY -> listOf(0.30)
    }

    override fun getLore(tier: TotemTier): List<String> {
        val (increasedArmor) = getValues(tier)
        val values = listOf(increasedArmor * 100)

        return TotemSettings.formatLore(LORE_FORMAT, values)
    }
}