package de.fuballer.mcendgame.component.artifact.artifacts.taunt

import de.fuballer.mcendgame.component.artifact.ArtifactSettings
import de.fuballer.mcendgame.component.artifact.artifacts.additional_arrows.AdditionalArrowsArtifactType
import de.fuballer.mcendgame.component.artifact.data.ArtifactTier
import de.fuballer.mcendgame.component.artifact.data.ArtifactType
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.NamespacedKey

object TauntArtifactType : ArtifactType {
    private const val LORE_FORMAT = "You have a %s%% chance\\to taunt an enemy on hit"
    override val key: NamespacedKey = PluginUtil.createNamespacedKey("taunt")
    override val displayName = "Artifact of Disturbance"

    override fun getValues(tier: ArtifactTier) = when (tier) {
        ArtifactTier.COMMON -> listOf(0.25)
        ArtifactTier.UNCOMMON -> listOf(0.5)
        ArtifactTier.RARE -> listOf(0.75)
        ArtifactTier.LEGENDARY -> listOf(1.0)
    }

    override fun getLore(tier: ArtifactTier): List<String> {
        val (chance) = AdditionalArrowsArtifactType.getValues(tier)
        val values = listOf(chance * 100)

        return ArtifactSettings.formatLore(LORE_FORMAT, values)
    }
}