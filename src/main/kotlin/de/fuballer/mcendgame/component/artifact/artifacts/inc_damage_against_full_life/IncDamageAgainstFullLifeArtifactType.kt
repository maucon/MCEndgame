package de.fuballer.mcendgame.component.artifact.artifacts.inc_damage_against_full_life

import de.fuballer.mcendgame.component.artifact.data.ArtifactTier
import de.fuballer.mcendgame.component.artifact.data.ArtifactType
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.NamespacedKey

object IncDamageAgainstFullLifeArtifactType : ArtifactType {
    override val key: NamespacedKey = PluginUtil.createNamespacedKey("inc_damage_against_full_life")
    override val displayName = "Artifact of Commencement"
    override val displayLoreFormat = "You deal %s%% increased damage\\against enemies with full health"

    override fun getValues(tier: ArtifactTier) = when (tier) {
        ArtifactTier.COMMON -> listOf(50.0)
        ArtifactTier.UNCOMMON -> listOf(75.0)
        ArtifactTier.RARE -> listOf(100.0)
        ArtifactTier.LEGENDARY -> listOf(150.0)
    }
}