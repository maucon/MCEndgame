package de.fuballer.mcendgame.component.artifact.artifacts.movement_speed

import de.fuballer.mcendgame.component.artifact.ArtifactSettings
import de.fuballer.mcendgame.component.artifact.data.ArtifactTier
import de.fuballer.mcendgame.component.artifact.data.ArtifactType
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.NamespacedKey

object MovementSpeedArtifactType : ArtifactType {
    private const val LORE_FORMAT = "You have %s%% increased base movement speed"
    override val key: NamespacedKey = PluginUtil.createNamespacedKey("movement_speed")
    override val displayName = "Artifact of Swiftness"

    override fun getValues(tier: ArtifactTier) = when (tier) {
        ArtifactTier.COMMON -> listOf(0.05)
        ArtifactTier.UNCOMMON -> listOf(0.1)
        ArtifactTier.RARE -> listOf(0.15)
        ArtifactTier.LEGENDARY -> listOf(0.25)
    }

    override fun getLore(tier: ArtifactTier): List<String> {
        val (increasedSpeed) = getValues(tier)
        val values = listOf(increasedSpeed * 100)

        return ArtifactSettings.formatLore(LORE_FORMAT, values)
    }
}