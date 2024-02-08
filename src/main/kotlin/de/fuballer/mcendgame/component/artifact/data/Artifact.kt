package de.fuballer.mcendgame.component.artifact.data

import de.fuballer.mcendgame.component.artifact.ArtifactSettings
import de.fuballer.mcendgame.technical.extension.ItemStackExtension.setArtifact
import de.fuballer.mcendgame.technical.extension.ItemStackExtension.setUnmodifiable
import org.bukkit.inventory.ItemStack

data class Artifact(
    var type: ArtifactType,
    var tier: ArtifactTier
) {
    fun toItem(): ItemStack {
        val item = ItemStack(ArtifactSettings.ARTIFACT_BASE_TYPE)
        val itemMeta = item.itemMeta!!

        itemMeta.setDisplayName("${tier.color}${type.displayName}")
        itemMeta.lore = type.getLore(tier)

        item.itemMeta = itemMeta

        item.setArtifact(this)
        item.setUnmodifiable()

        return item
    }
}