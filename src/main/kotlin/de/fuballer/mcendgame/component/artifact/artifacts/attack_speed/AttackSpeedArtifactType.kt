package de.fuballer.mcendgame.component.artifact.artifacts.attack_speed

import de.fuballer.mcendgame.component.artifact.ArtifactSettings
import de.fuballer.mcendgame.component.artifact.artifacts.movement_speed.MovementSpeedArtifactType
import de.fuballer.mcendgame.component.artifact.data.ArtifactTier
import de.fuballer.mcendgame.component.artifact.data.ArtifactType
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.NamespacedKey

object AttackSpeedArtifactType : ArtifactType {
    private const val LORE_FORMAT = "You have %s%% increased attack speed"
    override val key: NamespacedKey = PluginUtil.createNamespacedKey("attack_speed")
    override val displayName = "Artifact of Frenzy"

    override fun getValues(tier: ArtifactTier) = when (tier) {
        ArtifactTier.COMMON -> listOf(0.1)
        ArtifactTier.UNCOMMON -> listOf(0.15)
        ArtifactTier.RARE -> listOf(0.2)
        ArtifactTier.LEGENDARY -> listOf(0.3)
    }

    override fun getLore(tier: ArtifactTier): List<String> {
        val (increasedAttackSpeed) = MovementSpeedArtifactType.getValues(tier)
        val values = listOf(increasedAttackSpeed * 100)

        return ArtifactSettings.formatLore(LORE_FORMAT, values)
    }
}