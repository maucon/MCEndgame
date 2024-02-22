package de.fuballer.mcendgame.component.artifact.artifacts.wolf_companion

import de.fuballer.mcendgame.component.artifact.ArtifactSettings
import de.fuballer.mcendgame.component.artifact.data.ArtifactTier
import de.fuballer.mcendgame.component.artifact.data.ArtifactType
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.NamespacedKey

object WolfCompanionArtifactType : ArtifactType {
    private const val LORE_FORMAT = "You are accompanied by %s invincible wolfs.\\Your wolfs gains permanent strength %s"
    override val key: NamespacedKey = PluginUtil.createNamespacedKey("wolf_companion")
    override val displayName = "Artifact of Company"

    override fun getValues(tier: ArtifactTier) = when (tier) {
        ArtifactTier.COMMON -> listOf(2.0, 1.0)
        ArtifactTier.UNCOMMON -> listOf(2.0, 3.0)
        ArtifactTier.RARE -> listOf(3.0, 3.0)
        ArtifactTier.LEGENDARY -> listOf(4.0, 5.0)
    }

    override fun getLore(tier: ArtifactTier): List<String> {
        val values = getValues(tier)
        return ArtifactSettings.formatLore(LORE_FORMAT, values)
    }
}