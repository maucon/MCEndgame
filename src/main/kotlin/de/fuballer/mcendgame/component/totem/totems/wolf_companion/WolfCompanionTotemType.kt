package de.fuballer.mcendgame.component.totem.totems.wolf_companion

import de.fuballer.mcendgame.component.totem.TotemSettings.LORE_COLOR
import de.fuballer.mcendgame.component.totem.data.TotemTier
import de.fuballer.mcendgame.component.totem.data.TotemType
import de.fuballer.mcendgame.util.PluginUtil
import de.fuballer.mcendgame.util.RomanUtil
import de.fuballer.mcendgame.util.TextComponent
import net.kyori.adventure.text.Component
import org.bukkit.NamespacedKey

object WolfCompanionTotemType : TotemType {
    override val key: NamespacedKey = PluginUtil.createNamespacedKey("wolf_companion")
    override val displayName = "Totem of Company"

    fun getValues(tier: TotemTier) = when (tier) {
        TotemTier.COMMON -> listOf(2, 1)
        TotemTier.UNCOMMON -> listOf(2, 3)
        TotemTier.RARE -> listOf(3, 3)
        TotemTier.LEGENDARY -> listOf(4, 5)
    }

    override fun getLore(tier: TotemTier): List<Component> {
        val values = getValues(tier)
        return listOf(TextComponent.create(" Summon ${values[0]} Wolfs with Strength ${RomanUtil.toRoman(values[1])}", LORE_COLOR))
    }
}