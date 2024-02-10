package de.fuballer.mcendgame.component.artifact.artifacts.slow_when_hit

import de.fuballer.mcendgame.component.artifact.ArtifactSettings
import de.fuballer.mcendgame.component.artifact.data.ArtifactTier
import de.fuballer.mcendgame.component.artifact.data.ArtifactType
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.NamespacedKey

object SlowWhenHitArtifactType : ArtifactType {
    private const val LORE_FORMAT = "Affect nearby enemies with slowness %s\\for %s seconds on hit"
    override val key: NamespacedKey = PluginUtil.createNamespacedKey("slow_on_hit")
    override val displayName = "Artifact of Hinder"

    override fun getValues(tier: ArtifactTier) = when (tier) {
        ArtifactTier.COMMON -> listOf(1.0, 2.0)
        ArtifactTier.UNCOMMON -> listOf(1.0, 3.5)
        ArtifactTier.RARE -> listOf(1.0, 5.0)
        ArtifactTier.LEGENDARY -> listOf(2.0, 5.0)
    }

    override fun getLore(tier: ArtifactTier): List<String> {
        val values = getValues(tier)
        return ArtifactSettings.formatLore(LORE_FORMAT, values)
    }
}