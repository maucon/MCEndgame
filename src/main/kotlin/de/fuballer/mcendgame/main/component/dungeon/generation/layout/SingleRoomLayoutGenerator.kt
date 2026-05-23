package de.fuballer.mcendgame.main.component.dungeon.generation.layout

import de.fuballer.mcendgame.main.component.dungeon.generation.data.Layout
import de.fuballer.mcendgame.main.component.dungeon.generation.data.PlaceableRoom
import de.fuballer.mcendgame.main.component.dungeon.generation.data.RoomType
import de.fuballer.mcendgame.main.component.dungeon.generation.data.SpawnPosition
import net.minecraft.util.math.Vec3i
import kotlin.random.Random

class SingleRoomLayoutGenerator(
    private val roomType: RoomType,
) : LayoutGenerator {
    override fun generateDungeon(random: Random, dungeonLevel: Int, bossCount: Int): Layout {
        val room = PlaceableRoom(roomType, Vec3i.ZERO, 0)
        val startPos = requireNotNull(roomType.markerPoints.startPos) { "Start room '${roomType.path}' is missing markerPoints.startPos" }
        val spawnPos = SpawnPosition(startPos, -90.0)
        val dummyPos = roomType.markerPoints.bossPos // using same marker as boss for now

        return Layout(spawnPos, mutableListOf(room), listOf(), dummyPos, listOf(), listOf())
    }
}