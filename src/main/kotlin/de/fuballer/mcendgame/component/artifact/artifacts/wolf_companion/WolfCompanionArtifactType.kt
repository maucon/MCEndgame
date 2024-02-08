package de.fuballer.mcendgame.component.artifact.artifacts.wolf_companion

import de.fuballer.mcendgame.component.artifact.data.ArtifactTier
import de.fuballer.mcendgame.component.artifact.data.ArtifactType
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.NamespacedKey

object WolfCompanionArtifactType : ArtifactType {
    override val key: NamespacedKey = PluginUtil.createNamespacedKey("wolf_companion")
    override val displayName = "Artifact of Company"
    override val displayLoreFormat = "You are accompanied by %s invincible wolf/s.\\Your wolfs gains permanent strength %s"

    override fun getValues(tier: ArtifactTier) = when (tier) {
        ArtifactTier.COMMON -> listOf(1.0, 3.0)
        ArtifactTier.UNCOMMON -> listOf(2.0, 3.0)
        ArtifactTier.RARE -> listOf(3.0, 3.0)
        ArtifactTier.LEGENDARY -> listOf(4.0, 5.0)
    }
}