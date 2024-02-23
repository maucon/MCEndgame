package de.fuballer.mcendgame.component.artifact.artifacts.armor_toughness

import de.fuballer.mcendgame.component.artifact.ArtifactSettings
import de.fuballer.mcendgame.component.artifact.data.ArtifactTier
import de.fuballer.mcendgame.component.artifact.data.ArtifactType
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.NamespacedKey

object ArmorToughnessArtifactType : ArtifactType {
    private const val LORE_FORMAT = "You gain %s additional armor toughness"
    override val key: NamespacedKey = PluginUtil.createNamespacedKey("armor_toughness")
    override val displayName = "Artifact of Vanguard"

    override fun getValues(tier: ArtifactTier) = when (tier) {
        ArtifactTier.COMMON -> listOf(3.0)
        ArtifactTier.UNCOMMON -> listOf(5.0)
        ArtifactTier.RARE -> listOf(8.0)
        ArtifactTier.LEGENDARY -> listOf(12.0)
    }

    override fun getLore(tier: ArtifactTier): List<String> {
        val values = getValues(tier)
        return ArtifactSettings.formatLore(LORE_FORMAT, values)
    }
}