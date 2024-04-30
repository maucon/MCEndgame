package de.fuballer.mcendgame.component.dungeon.generation.layout_generator.linear

import de.fuballer.mcendgame.component.dungeon.generation.data.Layout
import de.fuballer.mcendgame.component.dungeon.generation.data.PlaceableTile
import de.fuballer.mcendgame.component.dungeon.generation.data.VectorUtil
import de.fuballer.mcendgame.component.dungeon.generation.layout_generator.LayoutGenerator
import org.bukkit.util.Vector
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

private fun calculateComplexityLimit(mapTier: Int) = 100 + mapTier

class LinearLayoutGenerator(
    private val startRoom: RoomType,
    private val rooms: List<RoomType>
) : LayoutGenerator {
    private lateinit var random: Random

    private val tiles = mutableListOf<PlaceableTile>()
    private val spawnLocations = mutableListOf<Vector>()

    override fun generateDungeon(
        random: Random,
        mapTier: Int
    ): Layout {
        this.random = random

        val startTile = PlaceableTile(startRoom.name, Vector(0, 0, 0), 0.0)
        tiles.add(startTile)

        val blockedArea = mutableListOf<Area>()
        blockedArea.add(Area(Vector(0, 0, 0), startRoom.size))

        val complexityLimit = calculateComplexityLimit(mapTier)
        if (!generateNextRoom(startRoom.doors[0], startRoom.complexity, complexityLimit, blockedArea)) {
            throw IllegalStateException("No valid layout could be generated")
        }

        return Layout(tiles, spawnLocations)
    }

    private fun generateNextRoom(
        currentDoor: Door,
        roomComplexitySum: Int,
        complexityLimit: Int,
        blockedArea: MutableList<Area>
    ): Boolean {
        if (roomComplexitySum >= complexityLimit) return true

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

                if (!generateNextRoom(nextDoor, roomComplexitySum + chosenRoom.complexity, complexityLimit, updatedBlockedArea)) continue

                val tile = PlaceableTile(chosenRoom.name, offsetRoomOrigin, neededRotation)
                tiles.add(tile)

                val absoluteSpawnLocations = chosenRoom.spawnLocations.toMutableList()
                    .map { it.clone() }
                    .map { it.rotateAroundY(Math.toRadians(neededRotation)) }
                    .map { it.add(offsetRoomOrigin) }

                spawnLocations.addAll(absoluteSpawnLocations)

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