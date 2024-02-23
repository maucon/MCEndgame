package de.fuballer.mcendgame.component.artifact.artifacts.dodge

import de.fuballer.mcendgame.component.artifact.ArtifactSettings
import de.fuballer.mcendgame.component.artifact.data.ArtifactTier
import de.fuballer.mcendgame.component.artifact.data.ArtifactType
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.NamespacedKey

object DodgeArtifactType : ArtifactType {
    private const val LORE_FORMAT = "You gain %s%% chance to dodge"
    override val key: NamespacedKey = PluginUtil.createNamespacedKey("dodge")
    override val displayName = "Artifact of Grace"

    override fun getValues(tier: ArtifactTier) = when (tier) {
        ArtifactTier.COMMON -> listOf(0.05)
        ArtifactTier.UNCOMMON -> listOf(0.1)
        ArtifactTier.RARE -> listOf(0.15)
        ArtifactTier.LEGENDARY -> listOf(0.2)
    }

    override fun getLore(tier: ArtifactTier): List<String> {
        val (dodge) = getValues(tier)
        val values = listOf(dodge * 100)

        return ArtifactSettings.formatLore(LORE_FORMAT, values)
    }
}