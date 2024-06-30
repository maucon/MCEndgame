package de.fuballer.mcendgame.component.dungeon.generation.layout_generator.linear

import com.sk89q.worldedit.world.block.BlockTypes
import de.fuballer.mcendgame.component.dungeon.generation.data.Layout
import de.fuballer.mcendgame.component.dungeon.generation.data.PlaceableBlock
import de.fuballer.mcendgame.component.dungeon.generation.data.PlaceableTile
import de.fuballer.mcendgame.component.dungeon.generation.data.SpawnLocation
import de.fuballer.mcendgame.component.dungeon.generation.layout_generator.LayoutGenerator
import de.fuballer.mcendgame.util.VectorUtil
import de.fuballer.mcendgame.util.random.RandomOption
import de.fuballer.mcendgame.util.random.RandomUtil
import org.bukkit.util.Vector
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

private fun calculateComplexityLimit() = 125
private fun calculateBranchComplexityLimit() = 5

private val branchingPoints = listOf(0.33, 0.66) // -> 3 boss rooms
private var complexityLimit = 0
private var branchComplexityLimit = 0

class LinearLayoutGenerator(
    private val startRoomType: RoomType,
    private val bossRoomType: RoomType,
    private val roomTypes: List<RandomOption<RoomType>>,
) : LayoutGenerator {
    private lateinit var random: Random
    private val blockedArea = mutableListOf<Area>()

    override fun generateDungeon(
        random: Random,
        mapTier: Int
    ): Layout {
        this.random = random

        complexityLimit = calculateComplexityLimit()
        branchComplexityLimit = calculateBranchComplexityLimit()

        val startTile = PlaceableTile(startRoomType.schematicData, Vector(0, 0, 0), 0.0)
        val tiles = mutableListOf(startTile)

        val spawnLocations = mutableListOf<SpawnLocation>()
        val bossSpawnLocations = mutableListOf<SpawnLocation>()

        blockedArea.add(Area(Vector(0, 0, 0), startRoomType.size))

        if (!generateNextRoom(tiles, startRoomType.doors[0], 0, true, 0, startRoomType, spawnLocations, bossSpawnLocations)) {
            throw IllegalStateException("No valid layout could be generated")
        }

        return Layout(startRoomType.startLocation!!, tiles, spawnLocations, bossSpawnLocations)
    }

    private fun generateNextRoom(
        tiles: MutableList<PlaceableTile>,
        currentDoor: Door,
        roomComplexitySum: Int,
        isMainPath: Boolean,
        existingBranches: Int,
        lastRoomType: RoomType?,
        spawnLocations: MutableList<SpawnLocation>,
        bossSpawnLocations: MutableList<SpawnLocation>
    ): Boolean {
        if ((isMainPath && roomComplexitySum >= complexityLimit) ||
            (!isMainPath && roomComplexitySum >= branchComplexityLimit)
        ) return generateBossRoom(tiles, currentDoor, spawnLocations, bossSpawnLocations)

        val possibleRoomTypes = getPossibleNextRooms(roomComplexitySum, isMainPath, existingBranches, lastRoomType)

        for (chosenRoomType in possibleRoomTypes) {
            if (generateRoomIfValid(tiles, currentDoor, roomComplexitySum, isMainPath, existingBranches, chosenRoomType, spawnLocations, bossSpawnLocations)) return true
        }

        return false
    }

    private fun generateBossRoom(
        tiles: MutableList<PlaceableTile>,
        currentDoor: Door,
        spawnLocations: MutableList<SpawnLocation>,
        bossSpawnLocations: MutableList<SpawnLocation>
    ) = generateRoomIfValid(tiles, currentDoor, 0, true, 0, bossRoomType, spawnLocations, bossSpawnLocations)

    private fun generateRoomIfValid(
        tiles: MutableList<PlaceableTile>,
        currentDoor: Door,
        roomComplexitySum: Int,
        isMainPath: Boolean,
        existingBranches: Int,
        chosenRoomType: RoomType,
        spawnLocations: MutableList<SpawnLocation>,
        bossSpawnLocations: MutableList<SpawnLocation>
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
            if (!generateRemainingDoors(
                    tiles,
                    remainingDoors,
                    chosenRoomType,
                    isMainPath,
                    existingBranches,
                    roomComplexitySum,
                    offsetRoomOrigin,
                    rotationRad,
                    spawnLocations,
                    bossSpawnLocations
                )
            ) {
                blockedArea.remove(area)
                continue
            }

            val extraBlocks = mutableListOf<PlaceableBlock>()

            if (!isMainPath && roomComplexitySum == 0) { // first room of branch
                val postLocation = VectorUtil.toBlockVector3(chosenDoor.position)
                val skullRotation = chosenDoor.getDirectionInDegree()

                val skull = PlaceableBlock(postLocation.x(), postLocation.y() + 1, postLocation.z(), skullRotation, BlockTypes.WITHER_SKELETON_SKULL!!)
                val post = PlaceableBlock(postLocation.x(), postLocation.y(), postLocation.z(), 0.0, BlockTypes.SPRUCE_FENCE!!)

                extraBlocks.add(skull)
                extraBlocks.add(post)
            }

            val tile = PlaceableTile(chosenRoomType.schematicData, offsetRoomOrigin, rotation, extraBlocks)
            tiles.add(tile)

            addAdjustSpawnLocations(chosenRoomType, offsetRoomOrigin, rotation, spawnLocations, bossSpawnLocations)

            return true
        }

        return false
    }

    private fun getPossibleNextRooms(
        roomComplexitySum: Int,
        isMainPath: Boolean,
        existingBranches: Int,
        lastRoomType: RoomType?,
    ): List<RoomType> {
        val possibleRooms = RandomUtil.shuffle(roomTypes, random).filter { it != lastRoomType }

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
        spawnLocations: MutableList<SpawnLocation>,
        bossSpawnLocations: MutableList<SpawnLocation>
    ): Boolean {
        val branchTiles = mutableListOf<PlaceableTile>()
        val branchSpawnLocations = mutableListOf<SpawnLocation>()
        val branchBossSpawnLocations = mutableListOf<SpawnLocation>()

        val updatedExistingBranches = if (remainingDoors.size > 1) existingBranches + 1 else existingBranches

        for (d in remainingDoors.indices) {
            val nextDoor = remainingDoors[d].getRotated(rotationRad)
            nextDoor.position.add(offsetRoomOrigin)

            val nextIsMainPath = isMainPath && d == remainingDoors.size - 1
            val nextRoomComplexitySum = if (nextIsMainPath != isMainPath) 0 else (roomComplexitySum + chosenRoomType.getComplexity())

            if (!generateNextRoom(
                    if (nextIsMainPath) tiles else branchTiles,
                    nextDoor,
                    nextRoomComplexitySum,
                    nextIsMainPath,
                    updatedExistingBranches,
                    chosenRoomType,
                    branchSpawnLocations,
                    branchBossSpawnLocations
                )
            ) {
                unblockTilesByOrigin(branchTiles)
                return false
            }
        }

        tiles.addAll(branchTiles)
        spawnLocations.addAll(branchSpawnLocations)
        bossSpawnLocations.addAll(branchBossSpawnLocations)

        return true
    }

    private fun unblockTilesByOrigin(
        tiles: List<PlaceableTile>,
    ) {
        for (tile in tiles)
            blockedArea.removeAll { it.contains(tile.position) }
    }

    private fun calculateRoomOffsetAfterRotation(
        currentDoor: Door,
        nextDoor: Door,
        rotationRad: Double
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

    private fun addAdjustSpawnLocations(
        chosenRoom: RoomType,
        offsetRoomOrigin: Vector,
        rotation: Double,
        spawnLocations: MutableList<SpawnLocation>,
        bossSpawnLocations: MutableList<SpawnLocation>,
    ) {
        val quarterRotations = (rotation / 90).toInt()
        val rotationRad = Math.toRadians(rotation)

        chosenRoom.spawnLocations.onEach {
            val newLocation = rotateSpawnLocation(it, rotationRad, offsetRoomOrigin, quarterRotations)

            spawnLocations.add(SpawnLocation(newLocation))
        }

        chosenRoom.bossSpawnLocations.onEach {
            val newLocation = rotateSpawnLocation(it, rotationRad, offsetRoomOrigin, quarterRotations)
            val newRotation = ((it.rotation + rotation) % 360) * -1

            bossSpawnLocations.add(SpawnLocation(newLocation, newRotation))
        }
    }

    private fun rotateSpawnLocation(
        spawnLocation: SpawnLocation,
        rotationRad: Double,
        offsetRoomOrigin: Vector,
        quarterRotations: Int
    ): Vector {
        val newLocation = spawnLocation.location.clone()
        newLocation.rotateAroundY(rotationRad)
        newLocation.add(offsetRoomOrigin)

        when (quarterRotations) {
            1 -> newLocation.add(Vector(0, 0, 1))
            2 -> newLocation.add(Vector(1, 0, 1))
            3 -> newLocation.add(Vector(1, 0, 0))
        }
        return newLocation
    }
}