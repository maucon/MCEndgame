package de.fuballer.mcendgame.component.artifact.artifacts.taunt

import de.fuballer.mcendgame.component.artifact.data.ArtifactTier
import de.fuballer.mcendgame.component.artifact.data.ArtifactType
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.NamespacedKey

object TauntArtifactType : ArtifactType {
    override val key: NamespacedKey = PluginUtil.createNamespacedKey("taunt")
    override val displayName = "Artifact of Disturbance"
    override val displayLoreFormat = "You have a %s%% chance\\to taunt an enemy on hit"

    override fun getValues(tier: ArtifactTier) = when (tier) {
        ArtifactTier.COMMON -> listOf(25.0)
        ArtifactTier.UNCOMMON -> listOf(50.0)
        ArtifactTier.RARE -> listOf(75.0)
        ArtifactTier.LEGENDARY -> listOf(100.0)
    }
}