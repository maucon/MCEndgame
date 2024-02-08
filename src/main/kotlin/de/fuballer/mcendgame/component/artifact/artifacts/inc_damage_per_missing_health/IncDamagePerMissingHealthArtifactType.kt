package de.fuballer.mcendgame.component.artifact.artifacts.inc_damage_per_missing_health

import de.fuballer.mcendgame.component.artifact.data.ArtifactTier
import de.fuballer.mcendgame.component.artifact.data.ArtifactType
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.NamespacedKey

object IncDamagePerMissingHealthArtifactType : ArtifactType {
    override val key: NamespacedKey = PluginUtil.createNamespacedKey("inc_damage_per_missing_health")
    override val displayName = "Artifact of Berserk"
    override val displayLoreFormat = "You deal %s%% increased damage\\for each missing heart"

    override fun getValues(tier: ArtifactTier) = when (tier) {
        ArtifactTier.COMMON -> listOf(4.0)
        ArtifactTier.UNCOMMON -> listOf(6.0)
        ArtifactTier.RARE -> listOf(8.0)
        ArtifactTier.LEGENDARY -> listOf(12.0)
    }
}