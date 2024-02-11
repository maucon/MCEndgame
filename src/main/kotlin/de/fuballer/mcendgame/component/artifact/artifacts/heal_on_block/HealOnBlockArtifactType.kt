package de.fuballer.mcendgame.component.artifact.artifacts.heal_on_block

import de.fuballer.mcendgame.component.artifact.ArtifactSettings
import de.fuballer.mcendgame.component.artifact.artifacts.effect_steal.EffectStealArtifactType
import de.fuballer.mcendgame.component.artifact.data.ArtifactTier
import de.fuballer.mcendgame.component.artifact.data.ArtifactType
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.NamespacedKey

object HealOnBlockArtifactType : ArtifactType {
    private const val LORE_FORMAT = "%s%% chance to heal %s health\\when you block damage with a shield\\(%s seconds cooldown)"
    override val key: NamespacedKey = PluginUtil.createNamespacedKey("heal_on_block")
    override val displayName = "Artifact of Recoup"

    override fun getValues(tier: ArtifactTier) = when (tier) {
        ArtifactTier.COMMON -> listOf(0.5, 1.0, 1.5)
        ArtifactTier.UNCOMMON -> listOf(0.75, 1.0, 1.5)
        ArtifactTier.RARE -> listOf(1.0, 1.0, 1.5)
        ArtifactTier.LEGENDARY -> listOf(1.0, 2.0, 1.5)
    }

    override fun getLore(tier: ArtifactTier): List<String> {
        val (chance, health, cooldown) = EffectStealArtifactType.getValues(tier)
        val values = listOf(chance * 100, health, cooldown)

        return ArtifactSettings.formatLore(LORE_FORMAT, values)
    }
}