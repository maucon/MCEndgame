package de.fuballer.mcendgame.component.dungeon.generation.layout_generator.linear

import de.fuballer.mcendgame.component.dungeon.generation.data.Layout
import de.fuballer.mcendgame.component.dungeon.generation.data.PlaceableTile
import de.fuballer.mcendgame.component.dungeon.generation.data.SpawnLocation
import de.fuballer.mcendgame.component.dungeon.generation.layout_generator.LayoutGenerator
import de.fuballer.mcendgame.util.VectorUtil
import org.bukkit.util.Vector
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

private fun calculateComplexityLimit(mapTier: Int) = 100 + 10 * mapTier
private fun calculateSidePathComplexityLimit(mapTier: Int) = 15 + 3 * mapTier

class LinearLayoutGenerator(
    private val startRoom: RoomType,
    private val rooms: List<RoomType>
) : LayoutGenerator {
    private lateinit var random: Random

    private val tiles = mutableListOf<PlaceableTile>()
    private val spawnLocations = mutableListOf<SpawnLocation>()
    private val blockedArea = mutableListOf<Area>()

    override fun generateDungeon(
        random: Random,
        mapTier: Int
    ): Layout {
        this.random = random

        val startTile = PlaceableTile(startRoom.schematicData, Vector(0, 0, 0), 0.0)
        tiles.add(startTile)

        blockedArea.add(Area(Vector(0, 0, 0), startRoom.size))

        val complexityLimit = calculateComplexityLimit(mapTier)
        val sidePathComplexityLimit = calculateSidePathComplexityLimit(mapTier)
        if (!generateNextRoom(startRoom.doors[0], startRoom.complexity, complexityLimit, sidePathComplexityLimit, true)) {
            throw IllegalStateException("No valid layout could be generated")
        }

        return Layout(tiles, spawnLocations)
    }

    private fun generateNextRoom(
        currentDoor: Door,
        roomComplexitySum: Int,
        complexityLimit: Int,
        sidePathComplexityLimit: Int,
        isMainPath: Boolean
    ): Boolean {
        if (isMainPath && roomComplexitySum >= complexityLimit) return true
        if (!isMainPath && roomComplexitySum >= sidePathComplexityLimit) return true

        var possibleRooms = rooms.shuffled(random)
        if (!isMainPath) {
            possibleRooms = possibleRooms.filter { it.doors.size <= 2 }
        }

        val doorAdjacentCoordinates = currentDoor.getAdjacentPosition()
        for (chosenRoom in possibleRooms) {
            val possibleDoors = chosenRoom.doors.shuffled(random)

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
                if (areaIsBlocked(area)) continue

                blockedArea.add(area)

                val remainingDoors = possibleDoors.toMutableList()
                remainingDoors.remove(chosenDoor)

                var canGenerateAllPaths = true
                for (d in 0..<remainingDoors.size) {
                    val nextDoor = remainingDoors[d].getRotated(neededRotationRadians)
                    nextDoor.position.add(offsetRoomOrigin)

                    val nextIsMainPath = isMainPath && d == remainingDoors.size - 1
                    val nextRoomComplexitySum = if (nextIsMainPath != isMainPath) 0 else (roomComplexitySum + chosenRoom.complexity)
                    if (!generateNextRoom(nextDoor, nextRoomComplexitySum, complexityLimit, sidePathComplexityLimit, nextIsMainPath)) {
                        canGenerateAllPaths = false
                        break
                    }
                }
                if (!canGenerateAllPaths) {
                    blockedArea.remove(area)
                    continue
                }

                val tile = PlaceableTile(chosenRoom.schematicData, offsetRoomOrigin, neededRotation)
                tiles.add(tile)

                val tileSpawnLocations = adjustSpawnLocations(chosenRoom, neededRotation, offsetRoomOrigin)
                spawnLocations.addAll(tileSpawnLocations)

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

    private fun areaIsBlocked(area: Area): Boolean {
        if (area.pos1.y < -64 || area.pos2.y > 320) return true

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

    private fun adjustSpawnLocations(
        chosenRoom: RoomType,
        neededRotation: Double,
        offsetRoomOrigin: Vector
    ): List<SpawnLocation> {
        val absoluteSpawnLocations = chosenRoom.spawnLocations.toMutableList()
            .map {
                val newLocation = it.location.clone()
                newLocation.rotateAroundY(Math.toRadians(neededRotation))
                newLocation.add(offsetRoomOrigin)

                SpawnLocation(newLocation, it.type)
            }
        return absoluteSpawnLocations
    }
}