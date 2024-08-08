package de.fuballer.mcendgame.component.totem.totems.less_damage_taken

import de.fuballer.mcendgame.component.totem.TotemSettings
import de.fuballer.mcendgame.component.totem.data.TotemTier
import de.fuballer.mcendgame.component.totem.data.TotemType
import de.fuballer.mcendgame.util.PluginUtil
import net.kyori.adventure.text.Component
import org.bukkit.NamespacedKey

object LessDamageTakenTotemType : TotemType {
    private const val LORE_FORMAT = "You take %s%% less Damage"
    override val key: NamespacedKey = PluginUtil.createNamespacedKey("less_damage_taken")
    override val displayName = "Totem of Fortress"

    override fun getValues(tier: TotemTier) = when (tier) {
        TotemTier.COMMON -> listOf(0.04)
        TotemTier.UNCOMMON -> listOf(0.08)
        TotemTier.RARE -> listOf(0.12)
        TotemTier.LEGENDARY -> listOf(0.2)
    }

    override fun getLore(tier: TotemTier): List<Component> {
        val (increasedArmor) = getValues(tier)
        val values = listOf(increasedArmor * 100)

        return TotemSettings.formatLore(LORE_FORMAT, values)
    }
}