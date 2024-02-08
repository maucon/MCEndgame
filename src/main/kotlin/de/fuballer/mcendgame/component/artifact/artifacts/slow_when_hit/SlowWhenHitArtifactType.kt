package de.fuballer.mcendgame.component.artifact.artifacts.slow_when_hit

import de.fuballer.mcendgame.component.artifact.data.ArtifactTier
import de.fuballer.mcendgame.component.artifact.data.ArtifactType
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.NamespacedKey

object SlowWhenHitArtifactType : ArtifactType {
    override val key: NamespacedKey = PluginUtil.createNamespacedKey("slow_when_hit")
    override val displayName = "Artifact of Hinder"
    override val displayLoreFormat = "Slows nearby enemies with slowness %s\\for %s seconds when you're hit"

    override fun getValues(tier: ArtifactTier) = when (tier) {
        ArtifactTier.COMMON -> listOf(1.0, 2.0)
        ArtifactTier.UNCOMMON -> listOf(1.0, 3.5)
        ArtifactTier.RARE -> listOf(1.0, 5.0)
        ArtifactTier.LEGENDARY -> listOf(2.0, 5.0)
    }
}