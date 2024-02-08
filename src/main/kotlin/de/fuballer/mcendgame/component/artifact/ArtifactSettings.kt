package de.fuballer.mcendgame.component.artifact

import de.fuballer.mcendgame.component.artifact.data.ArtifactTier
import de.fuballer.mcendgame.component.artifact.data.ArtifactType
import de.fuballer.mcendgame.util.random.RandomOption
import de.fuballer.mcendgame.util.random.SortableRandomOption
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryType

object ArtifactSettings {
    const val COMMAND_NAME = "dungeon-artifacts"
    const val GIVE_ARTIFACT_COMMAND_NAME = "dungeon-artifact-give"

    val ARTIFACT_BASE_TYPE = Material.TOTEM_OF_UNDYING

    val ARTIFACTS_WINDOW_TYPE = InventoryType.HOPPER
    val ARTIFACTS_WINDOW_SIZE = ARTIFACTS_WINDOW_TYPE.defaultSize
    const val ARTIFACTS_WINDOW_TITLE = "Artifacts"
    val CANNOT_CHANGE_ARTIFACTS_MESSAGE = "${ChatColor.RED}You cannot change artifacts whilst inside a dungeon."

    val LORE_COLOR = "${ChatColor.GRAY}${ChatColor.ITALIC}"

    const val ARTIFACT_DROP_CHANCE = 0.00133

    val ARTIFACT_TIERS = listOf(
        SortableRandomOption(1000, 0, ArtifactTier.COMMON),
        SortableRandomOption(200, 1, ArtifactTier.UNCOMMON),
        SortableRandomOption(25, 2, ArtifactTier.RARE),
        SortableRandomOption(3, 3, ArtifactTier.LEGENDARY)
    )

    val ARTIFACT_TYPES = ArtifactType.REGISTRY
        .map { RandomOption(1, it.value) }
}
