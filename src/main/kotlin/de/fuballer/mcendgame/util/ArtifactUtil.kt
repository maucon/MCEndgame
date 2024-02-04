package de.fuballer.mcendgame.util

import de.fuballer.mcendgame.component.artifact.Artifact
import de.fuballer.mcendgame.component.artifact.ArtifactSettings
import de.fuballer.mcendgame.domain.ArtifactType
import de.fuballer.mcendgame.technical.extension.ItemStackExtension.setArtifact
import de.fuballer.mcendgame.technical.extension.ItemStackExtension.setUnmodifiable
import de.fuballer.mcendgame.technical.extension.PlayerExtension.getArtifacts
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.text.DecimalFormat

object ArtifactUtil {
    private val format = DecimalFormat("0.#")

    fun getItem(artifact: Artifact): ItemStack {
        val item = ItemStack(ArtifactSettings.ARTIFACT_BASE_TYPE)
        val meta = item.itemMeta ?: return item

        val tierColor = ArtifactSettings.ARTIFACT_TIER_COLORS[artifact.tier]
        meta.setDisplayName("${tierColor}${artifact.type.displayName}")

        val artifactValues = artifact.type.values[artifact.tier] ?: return item
        meta.lore = getLoreWithValues(artifact.type.displayLore, artifactValues)

        item.itemMeta = meta

        item.setArtifact(artifact)
        item.setUnmodifiable()

        return item
    }

    fun getHighestTier(player: Player, artifactType: ArtifactType) =
        player.getArtifacts()
            ?.filter { it.type == artifactType }
            ?.maxByOrNull { it.tier }
            ?.tier

    private fun getLoreWithValues(loreFormat: String, values: List<Double>): List<String> {
        val formattedValues = values.map { format.format(it) }
        val lore = String.format(loreFormat, *formattedValues.toTypedArray())

        return lore.split("\\")
            .map { "${ArtifactSettings.LORE_COLOR}$it" }
    }
}