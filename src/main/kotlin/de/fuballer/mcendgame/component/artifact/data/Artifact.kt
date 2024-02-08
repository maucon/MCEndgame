package de.fuballer.mcendgame.component.artifact.data

import de.fuballer.mcendgame.component.artifact.ArtifactSettings
import de.fuballer.mcendgame.technical.extension.ItemStackExtension.setArtifact
import de.fuballer.mcendgame.technical.extension.ItemStackExtension.setUnmodifiable
import org.bukkit.inventory.ItemStack
import java.text.DecimalFormat

private val format = DecimalFormat("0.#")

data class Artifact(
    var type: ArtifactType,
    var tier: ArtifactTier
) {
    fun toItem(): ItemStack {
        val item = ItemStack(ArtifactSettings.ARTIFACT_BASE_TYPE)
        val itemMeta = item.itemMeta!!

        itemMeta.setDisplayName("${tier.color}${type.displayName}")

        val artifactValues = type.getValues(tier)
        itemMeta.lore = getLoreWithValues(type.displayLoreFormat, artifactValues)

        item.itemMeta = itemMeta

        item.setArtifact(this)
        item.setUnmodifiable()

        return item
    }

    private fun getLoreWithValues(loreFormat: String, values: List<Double>): List<String> {
        val formattedValues = values.map { format.format(it) }
        val lore = String.format(loreFormat, *formattedValues.toTypedArray())

        return lore.split("\\")
            .map { "${ArtifactSettings.LORE_COLOR}$it" }
    }
}