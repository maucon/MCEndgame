package de.fuballer.mcendgame.component.artifact.artifacts.max_health

import de.fuballer.mcendgame.component.artifact.data.ArtifactTier
import de.fuballer.mcendgame.component.artifact.data.ArtifactType
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.NamespacedKey

object MaxHealthArtifactType : ArtifactType {
    override val key: NamespacedKey = PluginUtil.createNamespacedKey("max_health")
    override val displayName = "Artifact of Thickness" // name chosen by xX20Erik01Xx
    override val displayLoreFormat = "You have %s more health"

    override fun getValues(tier: ArtifactTier) = when (tier) {
        ArtifactTier.COMMON -> listOf(1.0)
        ArtifactTier.UNCOMMON -> listOf(2.0)
        ArtifactTier.RARE -> listOf(3.0)
        ArtifactTier.LEGENDARY -> listOf(5.0)
    }
}