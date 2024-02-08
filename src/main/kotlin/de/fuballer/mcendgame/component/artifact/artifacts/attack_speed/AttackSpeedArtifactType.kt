package de.fuballer.mcendgame.component.artifact.artifacts.attack_speed

import de.fuballer.mcendgame.component.artifact.data.ArtifactTier
import de.fuballer.mcendgame.component.artifact.data.ArtifactType
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.NamespacedKey

object AttackSpeedArtifactType : ArtifactType {
    override val key: NamespacedKey = PluginUtil.createNamespacedKey("attack_speed")
    override val displayName = "Artifact of Frenzy"
    override val displayLoreFormat = "You have %s additional attacks per second"

    override fun getValues(tier: ArtifactTier) = when (tier) {
        ArtifactTier.COMMON -> listOf(0.1)
        ArtifactTier.UNCOMMON -> listOf(0.2)
        ArtifactTier.RARE -> listOf(0.3)
        ArtifactTier.LEGENDARY -> listOf(0.5)
    }
}