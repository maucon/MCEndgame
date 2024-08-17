package de.fuballer.mcendgame.component.dungeon.progress

import de.fuballer.mcendgame.util.TextComponent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

object PlayerDungeonProgressSettings {
    const val DUNGEON_LEVEL_INCREASE_THRESHOLD = 3
    const val COMMAND_NAME = "dungeon-progress"

    const val MAX_DUNGEON_TIER = 300 // only for the set progress command

    val PLAYER_NO_PROGRESS_MESSAGE = TextComponent.error("Player has no dungeon progress")

    fun getDungeonProgressMessage(playerName: String, tier: Int, progress: Int) = Component.textOfChildren(
        TextComponent.create(playerName, NamedTextColor.AQUA).decorate(TextDecoration.ITALIC),
        TextComponent.create(" is Tier: $tier", NamedTextColor.AQUA),
        getProgressMessageComponent(progress)
    )

    private val dungeonCompleteMessage = TextComponent.create("Dungeon completed! ", NamedTextColor.GOLD).decorate(TextDecoration.BOLD)
    val NO_PROGRESS_MESSAGE = dungeonCompleteMessage.append(TextComponent.error("Dungeon tier is too low to grant progress!"))

    fun getProgressMessage(tier: Int, progress: Int) = Component.textOfChildren(
        dungeonCompleteMessage,
        TextComponent.create("You are now Tier: $tier", NamedTextColor.AQUA),
        getProgressMessageComponent(progress)
    )

    fun getRegressMessage(tier: Int, progress: Int) = Component.textOfChildren(
        TextComponent.create("You died! ", NamedTextColor.RED).decorate(TextDecoration.BOLD),
        TextComponent.create("You are now Tier: $tier", NamedTextColor.AQUA),
        getProgressMessageComponent(progress)
    )

    private fun getProgressMessageComponent(progress: Int) = Component.textOfChildren(
        TextComponent.create(",", NamedTextColor.GRAY),
        TextComponent.create(" Progress: $progress/${DUNGEON_LEVEL_INCREASE_THRESHOLD}", NamedTextColor.LIGHT_PURPLE)
    )
}