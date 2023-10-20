package de.fuballer.mcendgame.component.artifact

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
    val ARTIFACTS_WINDOW_TITLE = "${ChatColor.BLACK}Artifacts"
    val CANNOT_CHANGE_ARTIFACTS_MESSAGE = "${ChatColor.RED}You cannot change artifacts whilst inside a dungeon."

    const val ARTIFACT_LORE_FIRST_LINE = "Effect:"

    val ARTIFACT_TIER_COLORS = mutableMapOf(
        0 to ChatColor.WHITE,
        1 to ChatColor.GREEN,
        2 to ChatColor.BLUE,
        3 to ChatColor.GOLD,
    )

    const val ARTIFACT_DROP_CHANCE = 0.00133

    val ARTIFACT_TIERS = listOf(
        SortableRandomOption(1000, 0, 0),
        SortableRandomOption(200, 1, 1),
        SortableRandomOption(25, 2, 2),
        SortableRandomOption(3, 3, 3)
    )

    val ARTIFACT_TYPES = listOf(
        RandomOption(10, ArtifactType.SLOW_WHEN_HIT),
        RandomOption(10, ArtifactType.HEAL_ON_BLOCK),
        RandomOption(10, ArtifactType.WOLF_COMPANION),
        RandomOption(10, ArtifactType.INC_DMG_PER_MISSING_HEALTH),
        RandomOption(10, ArtifactType.INC_DMG_AGAINST_FULL_LIFE),
        RandomOption(10, ArtifactType.MOVEMENT_SPEED),
        RandomOption(10, ArtifactType.ATTACK_SPEED),
        RandomOption(10, ArtifactType.ATTACK_DAMAGE),
        RandomOption(10, ArtifactType.MAX_HEALTH),
        RandomOption(10, ArtifactType.ADDITIONAL_ARROWS),
        RandomOption(10, ArtifactType.EFFECT_STEAL),
        RandomOption(10, ArtifactType.TAUNT)
    )
}
