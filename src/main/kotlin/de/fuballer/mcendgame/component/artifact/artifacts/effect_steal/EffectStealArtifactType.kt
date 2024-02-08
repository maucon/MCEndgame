package de.fuballer.mcendgame.component.artifact.artifacts.effect_steal

import de.fuballer.mcendgame.component.artifact.data.ArtifactTier
import de.fuballer.mcendgame.component.artifact.data.ArtifactType
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.NamespacedKey

object EffectStealArtifactType : ArtifactType {
    override val key: NamespacedKey = PluginUtil.createNamespacedKey("effect_steal")
    override val displayName = "Artifact of Rampage"
    override val displayLoreFormat = "You have a %s%% chance to steal one effect\\for %s seconds when killing an enemy.\\The maximum stolen effect level is %s"

    override fun getValues(tier: ArtifactTier) = when (tier) {
        ArtifactTier.COMMON -> listOf(25.0, 30.0, 1.0)
        ArtifactTier.UNCOMMON -> listOf(25.0, 30.0, 2.0)
        ArtifactTier.RARE -> listOf(25.0, 30.0, 3.0)
        ArtifactTier.LEGENDARY -> listOf(25.0, 30.0, 4.0)
    }
}