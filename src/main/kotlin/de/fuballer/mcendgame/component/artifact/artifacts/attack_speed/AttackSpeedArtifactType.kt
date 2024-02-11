package de.fuballer.mcendgame.component.artifact.artifacts.attack_speed

import de.fuballer.mcendgame.component.artifact.ArtifactSettings
import de.fuballer.mcendgame.component.artifact.data.ArtifactTier
import de.fuballer.mcendgame.component.artifact.data.ArtifactType
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.NamespacedKey

object AttackSpeedArtifactType : ArtifactType {
    private const val LORE_FORMAT = "You have %s additional attacks per second"
    override val key: NamespacedKey = PluginUtil.createNamespacedKey("attack_speed")
    override val displayName = "Artifact of Frenzy"

    override fun getValues(tier: ArtifactTier) = when (tier) {
        ArtifactTier.COMMON -> listOf(0.1)
        ArtifactTier.UNCOMMON -> listOf(0.2)
        ArtifactTier.RARE -> listOf(0.3)
        ArtifactTier.LEGENDARY -> listOf(0.5)
    }

    override fun getLore(tier: ArtifactTier): List<String> {
        val values = getValues(tier)
        return ArtifactSettings.formatLore(LORE_FORMAT, values)
    }
}