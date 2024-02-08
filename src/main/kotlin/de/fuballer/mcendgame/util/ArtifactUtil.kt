package de.fuballer.mcendgame.util

import de.fuballer.mcendgame.component.artifact.data.ArtifactType
import de.fuballer.mcendgame.technical.extension.PlayerExtension.getArtifacts
import org.bukkit.entity.Player

object ArtifactUtil { // TODO remove or move
    fun getHighestTier(player: Player, artifactType: ArtifactType) =
        player.getArtifacts()
            ?.filter { it.type == artifactType }
            ?.maxByOrNull { it.tier }
            ?.tier
}