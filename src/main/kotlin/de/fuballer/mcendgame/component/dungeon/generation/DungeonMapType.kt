package de.fuballer.mcendgame.component.dungeon.generation

import de.fuballer.mcendgame.component.dungeon.generation.layout_generator.LayoutGenerator
import de.fuballer.mcendgame.component.dungeon.generation.layout_generator.linear.Door
import de.fuballer.mcendgame.component.dungeon.generation.layout_generator.linear.LinearLayoutGenerator
import de.fuballer.mcendgame.component.dungeon.generation.layout_generator.linear.RoomType
import org.bukkit.util.Vector

private var startRoom = RoomType( // FIXME
    "start_room",
    Vector(13, 7, 14),
    0,
    listOf(Door(Vector(13, 0, 7), Vector(1, 0, 0))),
    listOf()
)

private var rooms = listOf( // FIXME
    RoomType(
        "short_connection", Vector(3, 7, 8), 1,
        listOf(Door(Vector(0, 0, 4), Vector(-1, 0, 0)), Door(Vector(3, 0, 4), Vector(1, 0, 0))),
        listOf(Vector(0, 0, 4), Vector(3, 0, 4))
    ),
    RoomType(
        "staircase", Vector(14, 19, 16), 6,
        listOf(Door(Vector(0, 11, 5), Vector(-1, 0, 0)), Door(Vector(9, 0, 16), Vector(0, 0, 1)), Door(Vector(14, 11, 12), Vector(1, 0, 0))),
        listOf(Vector(0, 11, 5), Vector(9, 0, 16), Vector(14, 11, 12))
    ),
    RoomType(
        "arena", Vector(22, 11, 24), 10,
        listOf(Door(Vector(0, 3, 12), Vector(-1, 0, 0)), Door(Vector(22, 3, 12), Vector(1, 0, 0))),
        listOf(Vector(0, 3, 12), Vector(22, 3, 12))
    )
)

enum class DungeonMapType(
    val schematicFolder: String,
    val layoutGenerator: LayoutGenerator
) {
    STRONGHOLD(
        "test",
        LinearLayoutGenerator(startRoom, rooms)
    ),
}