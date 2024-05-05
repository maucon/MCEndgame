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

private fun calculateComplexityLimit(mapTier: Int) = 150 + 0 * mapTier
private fun calculateBranchComplexityLimit(mapTier: Int) = 20 + 0 * mapTier

private val branchingPoints = listOf(0.33, 0.66)
private var complexityLimit = 0
private var branchComplexityLimit = 0

class LinearLayoutGenerator(
    private val startRoomType: RoomType,
    private val bossRoomType: RoomType,
    private val roomTypes: List<RoomType>,
) : LayoutGenerator {
    private lateinit var random: Random

    private val spawnLocations = mutableListOf<SpawnLocation>()
    private val blockedArea = mutableListOf<Area>()

    override fun generateDungeon(
        random: Random,
        mapTier: Int
    ): Layout {
        this.random = random

        complexityLimit = calculateComplexityLimit(mapTier)
        branchComplexityLimit = calculateBranchComplexityLimit(mapTier)

        val startTile = PlaceableTile(startRoomType.schematicData, Vector(0, 0, 0), 0.0)
        val tiles = mutableListOf(startTile)

        blockedArea.add(Area(Vector(0, 0, 0), startRoomType.size))

        if (!generateNextRoom(tiles, startRoomType.doors[0], 0, true, 0)) {
            throw IllegalStateException("No valid layout could be generated")
        }

        return Layout(tiles, spawnLocations)
    }

    private fun generateNextRoom(
        tiles: MutableList<PlaceableTile>,
        currentDoor: Door,
        roomComplexitySum: Int,
        isMainPath: Boolean,
        existingBranches: Int,
    ): Boolean {
        if ((isMainPath && roomComplexitySum >= complexityLimit) ||
            (!isMainPath && roomComplexitySum >= branchComplexityLimit)
        ) return generateBossRoom(tiles, currentDoor)

        val possibleRoomTypes = getPossibleNextRooms(roomComplexitySum, isMainPath, existingBranches)

        for (chosenRoomType in possibleRoomTypes) {
            if (generateRoomIfValid(tiles, currentDoor, roomComplexitySum, isMainPath, existingBranches, chosenRoomType)) return true
        }

        return false
    }

    private fun generateBossRoom(
        tiles: MutableList<PlaceableTile>,
        currentDoor: Door,
    ) = generateRoomIfValid(tiles, currentDoor, 0, true, 0, bossRoomType)

    private fun generateRoomIfValid(
        tiles: MutableList<PlaceableTile>,
        currentDoor: Door,
        roomComplexitySum: Int,
        isMainPath: Boolean,
        existingBranches: Int,
        chosenRoomType: RoomType,
    ): Boolean {
        val possibleDoors = chosenRoomType.doors.shuffled(random)
        for (chosenDoor in possibleDoors) {

            val rotation = calculateRotation(currentDoor, chosenDoor)
            val rotationRad = Math.toRadians(rotation)

            val offsetRoomOrigin = calculateRoomOffsetAfterRotation(currentDoor, chosenDoor, rotationRad)

            val rotatedSize = VectorUtil.getRoundedVector(chosenRoomType.size.clone().rotateAroundY(rotationRad))
            val area = rotatedRoomToArea(offsetRoomOrigin, rotatedSize)
            if (areaIsBlocked(area)) continue

            blockedArea.add(area)

            val remainingDoors = possibleDoors.toMutableList()
            remainingDoors.remove(chosenDoor)
            if (!generateRemainingDoors(tiles, remainingDoors, chosenRoomType, isMainPath, existingBranches, roomComplexitySum, offsetRoomOrigin, rotationRad)) {
                blockedArea.remove(area)
                continue
            }

            val tile = PlaceableTile(chosenRoomType.schematicData, offsetRoomOrigin, rotation)
            tiles.add(tile)

            val tileSpawnLocations = adjustSpawnLocations(chosenRoomType, rotation, offsetRoomOrigin)
            spawnLocations.addAll(tileSpawnLocations)

            return true
        }

        return false
    }

    private fun getPossibleNextRooms(
        roomComplexitySum: Int,
        isMainPath: Boolean,
        existingBranches: Int,
    ): List<RoomType> {
        val possibleRooms = roomTypes.shuffled(random)

        if (isMainPath
            && existingBranches < branchingPoints.size
            && roomComplexitySum.toDouble() / complexityLimit > branchingPoints[existingBranches]
        ) return possibleRooms.filter { !it.isLinear() }

        return possibleRooms.filter { it.isLinear() }
    }

    private fun generateRemainingDoors(
        tiles: MutableList<PlaceableTile>,
        remainingDoors: List<Door>,
        chosenRoomType: RoomType,
        isMainPath: Boolean,
        existingBranches: Int,
        roomComplexitySum: Int,
        offsetRoomOrigin: Vector,
        rotationRad: Double,
    ): Boolean {
        val branchTiles = mutableListOf<PlaceableTile>()

        val updatedExistingBranches = if (remainingDoors.size > 1) existingBranches + 1 else existingBranches

        for (d in remainingDoors.indices) {
            val nextDoor = remainingDoors[d].getRotated(rotationRad)
            nextDoor.position.add(offsetRoomOrigin)

            val nextIsMainPath = isMainPath && d == remainingDoors.size - 1
            val nextRoomComplexitySum = if (nextIsMainPath != isMainPath) 0 else (roomComplexitySum + chosenRoomType.complexity)


            if (!generateNextRoom(if (nextIsMainPath) tiles else branchTiles, nextDoor, nextRoomComplexitySum, nextIsMainPath, updatedExistingBranches)) {
                unblockTilesByOrigin(branchTiles)
                return false
            }
        }

        tiles.addAll(branchTiles)
        return true
    }

    private fun unblockTilesByOrigin(
        tiles: MutableList<PlaceableTile>,
    ) {
        for (tile in tiles)
            blockedArea.removeAll { it.contains(tile.position) }
    }

    private fun calculateRoomOffsetAfterRotation(
        currentDoor: Door,
        nextDoor: Door,
        rotationRad: Double,
    ): Vector {
        val rotatedChosenDoor = nextDoor.getRotated(rotationRad)
        val rotatedChosenDoorOffset = rotatedChosenDoor.position

        val currentDoorAdjacent = currentDoor.getAdjacentPosition()
        return currentDoorAdjacent.clone().subtract(rotatedChosenDoorOffset)
    }

    private fun calculateRotation(
        currentDoor: Door,
        nextDoor: Door
    ): Double {
        val rotationHelp = nextDoor.direction.clone()
        var rotation = 0.0
        while (abs(Math.toDegrees(rotationHelp.angle(currentDoor.direction).toDouble()) - 180) > 1) { // 1 degree inaccuracy (number doesn't really matter)
            rotationHelp.rotateAroundY(Math.toRadians(90.0))
            rotation += 90
        }

        return rotation
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
    ) = chosenRoom.spawnLocations.toMutableList()
        .map {
            val newLocation = it.location.clone()
            newLocation.rotateAroundY(Math.toRadians(neededRotation))
            newLocation.add(offsetRoomOrigin)

            SpawnLocation(newLocation, it.type)
        }

}