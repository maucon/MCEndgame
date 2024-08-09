package de.fuballer.mcendgame.component.dungeon.seed

import de.fuballer.mcendgame.util.TextComponent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.entity.Player

object DungeonSeedSettings {
    const val COMMAND_NAME = "dungeon-seed"

    val PLAYER_NO_SEED = TextComponent.error("Player does not have a dungeon seed")
    val INVALID_SEED = TextComponent.error("Dungeon seed must be of length 1 to 64")

    fun getPrefix(player: Player) =
        Component.textOfChildren(
            TextComponent.create(player.name, NamedTextColor.AQUA).decorate(TextDecoration.ITALIC),
            TextComponent.create("'s next dungeon seed: [", NamedTextColor.AQUA)
        )

    val SUFFIX = TextComponent.create("]", NamedTextColor.AQUA)
}