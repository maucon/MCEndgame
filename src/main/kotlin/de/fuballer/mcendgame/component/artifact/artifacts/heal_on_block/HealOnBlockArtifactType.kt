package de.fuballer.mcendgame.component.artifact.artifacts.heal_on_block

import de.fuballer.mcendgame.component.artifact.data.ArtifactTier
import de.fuballer.mcendgame.component.artifact.data.ArtifactType
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.NamespacedKey

object HealOnBlockArtifactType : ArtifactType {
    override val key: NamespacedKey = PluginUtil.createNamespacedKey("heal_on_block")
    override val displayName = "Artifact of Recoup"
    override val displayLoreFormat = "%s%% chance to heal %s health\\when you block damage with a shield\\(%s seconds cooldown)"

    override fun getValues(tier: ArtifactTier) = when (tier) {
        ArtifactTier.COMMON -> listOf(50.0, 1.0, 1.5)
        ArtifactTier.UNCOMMON -> listOf(75.0, 1.0, 1.5)
        ArtifactTier.RARE -> listOf(100.0, 1.0, 1.5)
        ArtifactTier.LEGENDARY -> listOf(100.0, 2.0, 1.5)
    }
}