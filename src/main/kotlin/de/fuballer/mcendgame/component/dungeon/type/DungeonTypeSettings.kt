package de.fuballer.mcendgame.component.dungeon.type

import de.fuballer.mcendgame.util.TextComponent
import de.fuballer.mcendgame.util.random.RandomOption
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.entity.Player

object DungeonTypeSettings {
    const val COMMAND_NAME = "dungeon-type"
    val PLAYER_NO_DUNGEON_TYPE = TextComponent.error("Player does not have a dungeon type")

    fun getPrefix(player: Player) =
        Component.textOfChildren(
            TextComponent.create(player.name, NamedTextColor.AQUA).decorate(TextDecoration.ITALIC),
            TextComponent.create("'s next dungeon type: [", NamedTextColor.AQUA)
        )

    val SUFFIX = TextComponent.create("]", NamedTextColor.AQUA)

    val DUNGEON_TYPE_WEIGHTS = listOf(
        RandomOption(1, DungeonType.STRONGHOLD)
    )
}
