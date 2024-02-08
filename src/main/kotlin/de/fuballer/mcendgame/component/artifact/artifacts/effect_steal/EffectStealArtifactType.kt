package de.fuballer.mcendgame.component.artifact.artifacts.effect_steal

import de.fuballer.mcendgame.component.artifact.ArtifactSettings
import de.fuballer.mcendgame.component.artifact.data.ArtifactTier
import de.fuballer.mcendgame.component.artifact.data.ArtifactType
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.NamespacedKey

object EffectStealArtifactType : ArtifactType {
    private const val LORE_FORMAT = "You have a %s%% chance to steal one effect\\for %s seconds when killing an enemy.\\The maximum stolen effect level is %s"
    override val key: NamespacedKey = PluginUtil.createNamespacedKey("effect_steal")
    override val displayName = "Artifact of Rampage"

    override fun getValues(tier: ArtifactTier) = when (tier) {
        ArtifactTier.COMMON -> listOf(0.25, 30.0, 1.0)
        ArtifactTier.UNCOMMON -> listOf(0.25, 30.0, 2.0)
        ArtifactTier.RARE -> listOf(0.25, 30.0, 3.0)
        ArtifactTier.LEGENDARY -> listOf(0.25, 30.0, 4.0)
    }

    override fun getLore(tier: ArtifactTier): List<String> {
        val (chance, seconds, maxLevel) = getValues(tier)
        val values = listOf(chance * 100, seconds, maxLevel)

        return ArtifactSettings.formatLore(LORE_FORMAT, values)
    }
}