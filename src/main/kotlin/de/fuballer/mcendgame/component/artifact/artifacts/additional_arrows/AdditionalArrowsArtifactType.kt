package de.fuballer.mcendgame.component.artifact.artifacts.additional_arrows

import de.fuballer.mcendgame.component.artifact.ArtifactSettings
import de.fuballer.mcendgame.component.artifact.data.ArtifactTier
import de.fuballer.mcendgame.component.artifact.data.ArtifactType
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.NamespacedKey

object AdditionalArrowsArtifactType : ArtifactType {
    private const val LORE_FORMAT = "You shoot %s additional arrows\\dealing %s%% of the damage"
    override val key: NamespacedKey = PluginUtil.createNamespacedKey("additional_arrows")
    override val displayName = "Artifact of Barrage"

    override fun getValues(tier: ArtifactTier) = when (tier) {
        ArtifactTier.COMMON -> listOf(2.0, 0.3)
        ArtifactTier.UNCOMMON -> listOf(2.0, 0.6)
        ArtifactTier.RARE -> listOf(2.0, 1.0)
        ArtifactTier.LEGENDARY -> listOf(4.0, 1.0)
    }

    override fun getLore(tier: ArtifactTier): List<String> {
        val (additionalArrows, damage) = getValues(tier)
        val values = listOf(additionalArrows, damage * 100)

        return ArtifactSettings.formatLore(LORE_FORMAT, values)
    }
}