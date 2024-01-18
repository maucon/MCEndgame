package de.fuballer.mcendgame.util

import de.fuballer.mcendgame.component.artifact.ArtifactSettings
import de.fuballer.mcendgame.component.artifact.data.Artifact
import de.fuballer.mcendgame.domain.artifact.ArtifactType
import de.fuballer.mcendgame.domain.persistent_data.TypeKeys
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

        PersistentDataUtil.setValue(meta, TypeKeys.ARTIFACT, artifact)
        PersistentDataUtil.setValue(meta, TypeKeys.UNMODIFIABLE, true)

        item.itemMeta = meta
        return item
    }

    fun isArtifact(item: ItemStack): Boolean {
        val itemMeta = item.itemMeta ?: return false
        return PersistentDataUtil.getValue(itemMeta, TypeKeys.ARTIFACT) != null
    }

    fun fromItem(item: ItemStack): Artifact? {
        val itemMeta = item.itemMeta ?: return null
        return PersistentDataUtil.getValue(itemMeta, TypeKeys.ARTIFACT)
    }

    fun getHighestTier(player: Player, artifactType: ArtifactType): Int? {
        val artifacts = PersistentDataUtil.getValue(player, TypeKeys.ARTIFACTS) ?: return null

        return artifacts
            .filter { it.type == artifactType }
            .maxByOrNull { it.tier }?.tier
    }

    private fun getLoreWithValues(loreFormat: String, values: List<Double>): List<String> {
        val formattedValues = values.map { format.format(it) }
        val lore = String.format(loreFormat, *formattedValues.toTypedArray())

        return lore.split("\\")
            .map { "${ArtifactSettings.LORE_COLOR}$it" }
    }
}