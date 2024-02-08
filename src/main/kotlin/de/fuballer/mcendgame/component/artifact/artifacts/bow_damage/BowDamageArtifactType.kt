package de.fuballer.mcendgame.component.artifact.artifacts.bow_damage

import de.fuballer.mcendgame.component.artifact.data.ArtifactTier
import de.fuballer.mcendgame.component.artifact.data.ArtifactType
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.NamespacedKey

object BowDamageArtifactType : ArtifactType {
    override val key: NamespacedKey = PluginUtil.createNamespacedKey("bow_damage")
    override val displayName = "Artifact of Impact"
    override val displayLoreFormat = "Your arrows deal %s%% increased damage."

    override fun getValues(tier: ArtifactTier) = when (tier) {
        ArtifactTier.COMMON -> listOf(15.0)
        ArtifactTier.UNCOMMON -> listOf(30.0)
        ArtifactTier.RARE -> listOf(45.0)
        ArtifactTier.LEGENDARY -> listOf(75.0)
    }
}