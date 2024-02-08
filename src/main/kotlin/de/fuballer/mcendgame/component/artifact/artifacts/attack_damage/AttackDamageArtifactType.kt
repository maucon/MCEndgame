package de.fuballer.mcendgame.component.artifact.artifacts.attack_damage

import de.fuballer.mcendgame.component.artifact.data.ArtifactTier
import de.fuballer.mcendgame.component.artifact.data.ArtifactType
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.NamespacedKey

object AttackDamageArtifactType : ArtifactType {
    override val key: NamespacedKey = PluginUtil.createNamespacedKey("attack_damage")
    override val displayName = "Artifact of Force"
    override val displayLoreFormat = "You deal %s additional base damage\\with melee attacks"

    override fun getValues(tier: ArtifactTier) = when (tier) {
        ArtifactTier.COMMON -> listOf(1.0)
        ArtifactTier.UNCOMMON -> listOf(2.0)
        ArtifactTier.RARE -> listOf(3.0)
        ArtifactTier.LEGENDARY -> listOf(5.0)
    }
}