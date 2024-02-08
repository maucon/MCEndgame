package de.fuballer.mcendgame.component.artifact.artifacts.inc_damage_per_missing_health

import de.fuballer.mcendgame.component.artifact.ArtifactSettings
import de.fuballer.mcendgame.component.artifact.artifacts.additional_arrows.AdditionalArrowsArtifactType
import de.fuballer.mcendgame.component.artifact.data.ArtifactTier
import de.fuballer.mcendgame.component.artifact.data.ArtifactType
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.NamespacedKey

object IncDamagePerMissingHealthArtifactType : ArtifactType {
    private const val LORE_FORMAT = "You deal %s%% increased damage\\for each missing heart"
    override val key: NamespacedKey = PluginUtil.createNamespacedKey("inc_damage_per_missing_health")
    override val displayName = "Artifact of Berserk"

    override fun getValues(tier: ArtifactTier) = when (tier) {
        ArtifactTier.COMMON -> listOf(0.04)
        ArtifactTier.UNCOMMON -> listOf(0.06)
        ArtifactTier.RARE -> listOf(0.08)
        ArtifactTier.LEGENDARY -> listOf(0.12)
    }

    override fun getLore(tier: ArtifactTier): List<String> {
        val (increasedDamage) = AdditionalArrowsArtifactType.getValues(tier)
        val values = listOf(increasedDamage * 100)

        return ArtifactSettings.formatLore(LORE_FORMAT, values)
    }
}