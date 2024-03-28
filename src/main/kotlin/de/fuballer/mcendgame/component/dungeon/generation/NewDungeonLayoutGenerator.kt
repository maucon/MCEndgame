package de.fuballer.mcendgame.component.dungeon.generation

import de.fuballer.mcendgame.component.dungeon.generation.data.Area
import de.fuballer.mcendgame.component.dungeon.generation.data.Door
import de.fuballer.mcendgame.component.dungeon.generation.data.PlaceableRoom
import de.fuballer.mcendgame.component.dungeon.generation.data.RoomType
import org.bukkit.util.Vector
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

class NewDungeonLayoutGenerator {
    private var layoutRooms: MutableList<PlaceableRoom> = mutableListOf()

    fun getLayout() = layoutRooms

    private var startRoom = RoomType(
        "start_room", Vector(13, 7, 14), 0,
        listOf(Door(Vector(13, 0, 7), Vector(1, 0, 0)))
    )

    private var rooms = listOf(
        RoomType(
            "short_connection", Vector(3, 7, 8), 1,
            listOf(Door(Vector(0, 0, 4), Vector(-1, 0, 0)), Door(Vector(3, 0, 4), Vector(1, 0, 0)))
        ),
        RoomType(
            "staircase", Vector(14, 19, 16), 6,
            listOf(Door(Vector(0, 11, 5), Vector(-1, 0, 0)), Door(Vector(9, 0, 16), Vector(0, 0, 1)), Door(Vector(14, 11, 12), Vector(1, 0, 0)))
        ),
        RoomType(
            "arena", Vector(22, 11, 24), 10,
            listOf(Door(Vector(0, 3, 12), Vector(-1, 0, 0)), Door(Vector(22, 3, 12), Vector(1, 0, 0)))
        )
    )

    fun generateDungeon(
        random: Random,
        roomComplexitySumLimit: Int
    ) {
        layoutRooms.add(PlaceableRoom(startRoom.name, Vector(0, 0, 0), 0.0))

        val blockedArea = mutableListOf<Area>()
        blockedArea.add(Area(Vector(0, 0, 0), startRoom.size))

        if (!generateNextRoom(random, startRoom.doors[0], startRoom.complexity, roomComplexitySumLimit, blockedArea))
            println("No valid layout could be generated.")
    }

    private fun generateNextRoom(
        random: Random,
        currentDoor: Door,
        roomComplexitySum: Int,
        roomComplexitySumLimit: Int,
        blockedArea: MutableList<Area>
    ): Boolean {
        if (roomComplexitySum >= roomComplexitySumLimit) return true

        val doorAdjacentCoordinates = currentDoor.getAdjacentPosition()

        val possibleRooms = rooms.toMutableList().shuffled(random)
        for (chosenRoom in possibleRooms) {

            val possibleDoors = chosenRoom.doors.toMutableList().shuffled(random)
            for (chosenDoor in possibleDoors) {
                val rotationHelp = chosenDoor.direction.clone()
                var neededRotation = 0.0
                while (abs(Math.toDegrees(rotationHelp.angle(currentDoor.direction).toDouble()) - 180) > 1) { // 1 degree inaccuracy (number doesn't really matter)
                    rotationHelp.rotateAroundY(Math.toRadians(90.0))
                    neededRotation += 90
                }
                val neededRotationRadians = Math.toRadians(neededRotation)

                val rotatedChosenDoor = chosenDoor.getRotated(neededRotationRadians)
                val rotatedChosenDoorOffset = rotatedChosenDoor.position

                val offsetRoomOrigin = doorAdjacentCoordinates.clone().subtract(rotatedChosenDoorOffset)

                val area = rotatedRoomToArea(offsetRoomOrigin, VectorUtil.getRoundedVector(chosenRoom.size.clone().rotateAroundY(neededRotationRadians)))
                if (areaIsBlocked(area, blockedArea)) continue

                val updatedBlockedArea = blockedArea.toMutableList()
                updatedBlockedArea.add(area)

                val remainingDoors = possibleDoors.toMutableList()
                remainingDoors.remove(chosenDoor)
                val nextDoor = remainingDoors[0].getRotated(neededRotationRadians)
                nextDoor.position.add(offsetRoomOrigin)

                if (!generateNextRoom(random, nextDoor, roomComplexitySum + chosenRoom.complexity, roomComplexitySumLimit, updatedBlockedArea)) continue

                layoutRooms.add(PlaceableRoom(chosenRoom.name, offsetRoomOrigin, neededRotation))

                return true
            }
        }

        return false
    }

    private fun rotatedRoomToArea(origin: Vector, size: Vector): Area {
        val pos1 = Vector(min(origin.x, origin.x + size.x), origin.y, min(origin.z, origin.z + size.z))
        val pos2 = Vector(max(origin.x, origin.x + size.x), (origin.y + size.y), max(origin.z, origin.z + size.z))

        return Area(pos1, pos2)
    }

    private fun areaIsBlocked(area: Area, blockedArea: MutableList<Area>): Boolean {
        for (blocked in blockedArea) {
            if (area.pos1.x > blocked.pos2.x) continue
            if (area.pos1.y > blocked.pos2.y) continue
            if (area.pos1.z > blocked.pos2.z) continue
            if (area.pos2.x < blocked.pos1.x) continue
            if (area.pos2.y < blocked.pos1.y) continue
            if (area.pos2.z < blocked.pos1.z) continue

            return true
        }

        return false
    }
}