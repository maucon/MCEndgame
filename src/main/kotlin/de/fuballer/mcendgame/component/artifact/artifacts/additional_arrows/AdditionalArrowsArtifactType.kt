package de.fuballer.mcendgame.component.artifact.artifacts.additional_arrows

import de.fuballer.mcendgame.component.artifact.data.ArtifactTier
import de.fuballer.mcendgame.component.artifact.data.ArtifactType
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.NamespacedKey

object AdditionalArrowsArtifactType : ArtifactType {
    override val key: NamespacedKey = PluginUtil.createNamespacedKey("additional_arrows")
    override val displayName = "Artifact of Barrage"
    override val displayLoreFormat = "You shoot %s additional arrows\\dealing %s%% of the damage"

    override fun getValues(tier: ArtifactTier) = when (tier) {
        ArtifactTier.COMMON -> listOf(2.0, 30.0)
        ArtifactTier.UNCOMMON -> listOf(2.0, 60.0)
        ArtifactTier.RARE -> listOf(2.0, 100.0)
        ArtifactTier.LEGENDARY -> listOf(4.0, 100.0)
    }
}