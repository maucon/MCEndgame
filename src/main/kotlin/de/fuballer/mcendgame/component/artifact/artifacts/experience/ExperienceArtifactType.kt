package de.fuballer.mcendgame.component.artifact.artifacts.experience

import de.fuballer.mcendgame.component.artifact.ArtifactSettings
import de.fuballer.mcendgame.component.artifact.data.ArtifactTier
import de.fuballer.mcendgame.component.artifact.data.ArtifactType
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.NamespacedKey

object ExperienceArtifactType : ArtifactType {
    private const val LORE_FORMAT = "You gain %s%% increased experience"
    override val key: NamespacedKey = PluginUtil.createNamespacedKey("experience")
    override val displayName = "Artifact of Catalyst"

    override fun getValues(tier: ArtifactTier) = when (tier) {
        ArtifactTier.COMMON -> listOf(0.4)
        ArtifactTier.UNCOMMON -> listOf(0.8)
        ArtifactTier.RARE -> listOf(1.2)
        ArtifactTier.LEGENDARY -> listOf(2.0)
    }

    override fun getLore(tier: ArtifactTier): List<String> {
        val (experience) = getValues(tier)
        val values = listOf(experience * 100)

        return ArtifactSettings.formatLore(LORE_FORMAT, values)
    }
}