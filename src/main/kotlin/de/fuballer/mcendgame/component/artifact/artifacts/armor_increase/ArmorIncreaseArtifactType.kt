package de.fuballer.mcendgame.component.artifact.artifacts.armor_increase

import de.fuballer.mcendgame.component.artifact.ArtifactSettings
import de.fuballer.mcendgame.component.artifact.data.ArtifactTier
import de.fuballer.mcendgame.component.artifact.data.ArtifactType
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.NamespacedKey

object ArmorIncreaseArtifactType : ArtifactType {
    private const val LORE_FORMAT = "You gain %s%% increased armor"
    override val key: NamespacedKey = PluginUtil.createNamespacedKey("armor_increase")
    override val displayName = "Artifact of Fortress"

    override fun getValues(tier: ArtifactTier) = when (tier) {
        ArtifactTier.COMMON -> listOf(0.05)
        ArtifactTier.UNCOMMON -> listOf(0.075)
        ArtifactTier.RARE -> listOf(0.1)
        ArtifactTier.LEGENDARY -> listOf(0.15)
    }

    override fun getLore(tier: ArtifactTier): List<String> {
        val (increasedArmor) = getValues(tier)
        val values = listOf(increasedArmor * 100)

        return ArtifactSettings.formatLore(LORE_FORMAT, values)
    }
}