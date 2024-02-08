package de.fuballer.mcendgame.component.artifact.artifacts.movement_speed

import de.fuballer.mcendgame.component.artifact.data.ArtifactTier
import de.fuballer.mcendgame.component.artifact.data.ArtifactType
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.NamespacedKey

object MovementSpeedArtifactType : ArtifactType {
    override val key: NamespacedKey = PluginUtil.createNamespacedKey("movement_speed")
    override val displayName = "Artifact of Swiftness"
    override val displayLoreFormat = "You have %s%% increased base movement speed"

    override fun getValues(tier: ArtifactTier) = when (tier) {
        ArtifactTier.COMMON -> listOf(5.0)
        ArtifactTier.UNCOMMON -> listOf(10.0)
        ArtifactTier.RARE -> listOf(15.0)
        ArtifactTier.LEGENDARY -> listOf(25.0)
    }
}