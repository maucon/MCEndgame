package de.fuballer.mcendgame.component.artifact.artifacts.armor

import de.fuballer.mcendgame.component.artifact.ArtifactSettings
import de.fuballer.mcendgame.component.artifact.data.ArtifactTier
import de.fuballer.mcendgame.component.artifact.data.ArtifactType
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.NamespacedKey

object ArmorArtifactType : ArtifactType {
    private const val LORE_FORMAT = "You gain %s additional armor"
    override val key: NamespacedKey = PluginUtil.createNamespacedKey("armor")
    override val displayName = "Artifact of Bastion"

    override fun getValues(tier: ArtifactTier) = when (tier) {
        ArtifactTier.COMMON -> listOf(3.0)
        ArtifactTier.UNCOMMON -> listOf(4.5)
        ArtifactTier.RARE -> listOf(6.0)
        ArtifactTier.LEGENDARY -> listOf(8.0)
    }

    override fun getLore(tier: ArtifactTier): List<String> {
        val values = getValues(tier)
        return ArtifactSettings.formatLore(LORE_FORMAT, values)
    }
}