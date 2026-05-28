package de.fuballer.mcendgame.main.component.dungeon.generation.layout

import de.fuballer.mcendgame.main.component.dungeon.generation.data.*
import de.fuballer.mcendgame.main.util.extension.Vec3iExtension.clone
import de.fuballer.mcendgame.main.util.extension.Vec3iExtension.rotateY90
import de.fuballer.mcendgame.main.util.extension.Vec3iExtension.stepTowardsZero
import de.fuballer.mcendgame.main.util.random.RandomOption
import de.fuballer.mcendgame.main.util.random.RandomUtil
import net.minecraft.block.Blocks
import net.minecraft.util.math.Vec3i
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

private fun calculateBranchComplexityLimit() = 5
private fun calculateBranchingPoints(count: Int): List<Double> {
    val branchOffset = 1.0 / count
    val branchingPoints = mutableListOf<Double>()
    for (branch in 1..<count) {
        branchingPoints.add(branchOffset * branch)
    }
    return branchingPoints
}

private var branchingPoints: List<Double> = listOf()
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
        dungeonLevel: Int,
        bossCount: Int,
        enemyCount: Int,
    ): Layout {
        this.random = random

        branchingPoints = calculateBranchingPoints(bossCount)
        complexityLimit = enemyCount
        branchComplexityLimit = calculateBranchComplexityLimit()

        val startRoom = PlaceableRoom(startRoomType, Vec3i.ZERO, 0)
        val startPos = requireNotNull(startRoomType.markerPoints.startPos) { "Start room '${startRoomType.path}' is missing markerPoints.startPos" }
        val spawnPos = SpawnPosition(startPos, -90.0)
        val startEncounterLocations = startRoomType.markerPoints.startEncounterPos.map { EncounterLocation(it, spawnPos.pos) }

        val tiles = mutableListOf(startRoom)

        val spawnLocations = mutableListOf<SpawnPosition>()
        val bossSpawnLocations = mutableListOf<SpawnPosition>()
        val encounterLocations = mutableListOf<EncounterLocation>()

        blockedArea.add(Area(Vec3i.ZERO, startRoomType.size.stepTowardsZero()))

        if (!generateNextRoom(
                tiles,
                startRoomType.markerPoints.doors[0].getOffset(Vec3i.ZERO),
                0,
                true,
                0,
                startRoomType,
                spawnLocations,
                bossSpawnLocations,
                encounterLocations,
            )
        ) {
            throw IllegalStateException("No valid layout could be generated")
        }

        return Layout(spawnPos, tiles, spawnLocations, bossSpawnLocations, encounterLocations, startEncounterLocations)
    }

    private fun generateNextRoom(
        tiles: MutableList<PlaceableRoom>,
        currentDoor: Door,
        roomComplexitySum: Int,
        isMainPath: Boolean,
        existingBranches: Int,
        lastRoomType: RoomType?,
        spawnLocations: MutableList<SpawnPosition>,
        bossSpawnLocations: MutableList<SpawnPosition>,
        encounterLocations: MutableList<EncounterLocation>,
    ): Boolean {
        if ((isMainPath && roomComplexitySum >= complexityLimit) ||
            (!isMainPath && roomComplexitySum >= branchComplexityLimit)
        ) return generateBossRoom(tiles, currentDoor, spawnLocations, bossSpawnLocations, encounterLocations)

        val possibleRoomTypes = getPossibleNextRooms(roomComplexitySum, isMainPath, existingBranches, lastRoomType)

        for (chosenRoomType in possibleRoomTypes) {
            if (generateRoomIfValid(
                    tiles,
                    currentDoor,
                    roomComplexitySum,
                    isMainPath,
                    existingBranches,
                    chosenRoomType,
                    spawnLocations,
                    bossSpawnLocations,
                    encounterLocations,
                )
            ) return true
        }

        return false
    }

    private fun generateBossRoom(
        tiles: MutableList<PlaceableRoom>,
        currentDoor: Door,
        spawnLocations: MutableList<SpawnPosition>,
        bossSpawnLocations: MutableList<SpawnPosition>,
        encounterLocations: MutableList<EncounterLocation>,
    ) = generateRoomIfValid(tiles, currentDoor, 0, true, 0, bossRoomType, spawnLocations, bossSpawnLocations, encounterLocations)

    private fun generateRoomIfValid(
        tiles: MutableList<PlaceableRoom>,
        currentDoor: Door,
        roomComplexitySum: Int,
        isMainPath: Boolean,
        existingBranches: Int,
        chosenRoomType: RoomType,
        spawnLocations: MutableList<SpawnPosition>,
        bossSpawnLocations: MutableList<SpawnPosition>,
        encounterLocations: MutableList<EncounterLocation>,
    ): Boolean {
        val possibleDoors = chosenRoomType.markerPoints.doors.shuffled(random)
        for (chosenDoor in possibleDoors) {

            val rotation90 = calculateNeededRotation90(currentDoor, chosenDoor)

            val offsetRoomOrigin = calculateRoomOffsetAfterRotation(currentDoor, chosenDoor, rotation90)

            val rotatedSize = chosenRoomType.size.rotateY90(rotation90)
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
                    rotation90,
                    spawnLocations,
                    bossSpawnLocations,
                    encounterLocations,
                )
            ) {
                blockedArea.remove(area)
                continue
            }

            val extraBlocks = mutableListOf<PlaceableBlock>()
            if (!isMainPath && roomComplexitySum == 0) { // first room of branch
                val pos = chosenDoor.pos
                val post = PlaceableBlock(pos.x, pos.y, pos.z, 0, Blocks.SPRUCE_FENCE)

                val skullRotation = (chosenDoor.getDirectionAsBlockRotation16() + 8) % 16
                val skull = PlaceableBlock(
                    pos.x,
                    pos.y + 1,
                    pos.z,
                    skullRotation,
                    Blocks.WITHER_SKELETON_SKULL
                )

                extraBlocks.add(skull)
                extraBlocks.add(post)
            }

            val tile = PlaceableRoom(
                chosenRoomType,
                offsetRoomOrigin,
                rotation90,
                extraBlocks
            )
            tiles.add(tile)

            addMarkerLocations(chosenRoomType, offsetRoomOrigin, rotation90, currentDoor, spawnLocations, bossSpawnLocations, encounterLocations)

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
        val possibleRooms = RandomUtil.shuffleWeighted(roomTypes, random).filter { it != lastRoomType }

        if (isMainPath
            && existingBranches < branchingPoints.size
            && roomComplexitySum.toDouble() / complexityLimit > branchingPoints[existingBranches]
        ) return possibleRooms.filter { !it.isLinear() }

        return possibleRooms.filter { it.isLinear() }
    }

    private fun generateRemainingDoors(
        tiles: MutableList<PlaceableRoom>,
        remainingDoors: List<Door>,
        chosenRoomType: RoomType,
        isMainPath: Boolean,
        existingBranches: Int,
        roomComplexitySum: Int,
        offsetRoomOrigin: Vec3i,
        rotation90: Int,
        spawnLocations: MutableList<SpawnPosition>,
        bossSpawnLocations: MutableList<SpawnPosition>,
        encounterLocations: MutableList<EncounterLocation>,
    ): Boolean {
        val branchTiles = mutableListOf<PlaceableRoom>()
        val branchSpawnLocations = mutableListOf<SpawnPosition>()
        val branchBossSpawnLocations = mutableListOf<SpawnPosition>()
        val branchEncounterLocations = mutableListOf<EncounterLocation>()

        val updatedExistingBranches = if (remainingDoors.size > 1) existingBranches + 1 else existingBranches

        for (d in remainingDoors.indices) {
            val nextDoor = remainingDoors[d].getRotated90(rotation90).getOffset(offsetRoomOrigin)

            val nextIsMainPath = isMainPath && d == remainingDoors.size - 1
            val nextRoomComplexitySum =
                if (nextIsMainPath != isMainPath) 0 else (roomComplexitySum + chosenRoomType.getComplexity())

            if (!generateNextRoom(
                    if (nextIsMainPath) tiles else branchTiles,
                    nextDoor,
                    nextRoomComplexitySum,
                    nextIsMainPath,
                    updatedExistingBranches,
                    chosenRoomType,
                    branchSpawnLocations,
                    branchBossSpawnLocations,
                    branchEncounterLocations,
                )
            ) {
                unblockTilesByOrigin(branchTiles)
                return false
            }
        }

        tiles.addAll(branchTiles)
        spawnLocations.addAll(branchSpawnLocations)
        bossSpawnLocations.addAll(branchBossSpawnLocations)
        encounterLocations.addAll(branchEncounterLocations)

        return true
    }

    private fun unblockTilesByOrigin(
        tiles: List<PlaceableRoom>,
    ) {
        for (tile in tiles) {
            blockedArea.removeAll { it.contains(tile.position) }
        }
    }

    private fun calculateRoomOffsetAfterRotation(
        currentDoor: Door,
        nextDoor: Door,
        rotation90: Int
    ): Vec3i {
        val rotatedChosenDoor = nextDoor.getRotated90(rotation90)
        val rotatedChosenDoorOffset = rotatedChosenDoor.pos

        val currentDoorAdjacent = currentDoor.getAdjacentPosition()
        val roomOffset = currentDoorAdjacent.clone().subtract(rotatedChosenDoorOffset)

        return roomOffset
    }

    private fun calculateNeededRotation90(
        currentDoor: Door,
        nextDoor: Door
    ): Int {
        val directions = listOf(
            Vec3i(1, 0, 0),  // East
            Vec3i(0, 0, 1),  // South
            Vec3i(-1, 0, 0), // West
            Vec3i(0, 0, -1)  // North
        )

        val currentIndex = directions.indexOf(currentDoor.dir)
        val nextIndex = directions.indexOf(nextDoor.dir)

        if (currentIndex == -1 || nextIndex == -1) {
            throw IllegalArgumentException("Invalid direction vector")
        }

        val rotation90 = (6 - (nextIndex - currentIndex)) % 4

        return rotation90
    }

    private fun rotatedRoomToArea(origin: Vec3i, size: Vec3i): Area {
        val decSize = size.stepTowardsZero()
        val pos1 = Vec3i(min(origin.x, origin.x + decSize.x), origin.y, min(origin.z, origin.z + decSize.z))
        val pos2 =
            Vec3i(max(origin.x, origin.x + decSize.x), (origin.y + decSize.y), max(origin.z, origin.z + decSize.z))

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

    private fun addMarkerLocations(
        chosenRoom: RoomType,
        offsetRoomOrigin: Vec3i,
        rotation90: Int,
        entryDoor: Door,
        spawnLocations: MutableList<SpawnPosition>,
        bossSpawnLocations: MutableList<SpawnPosition>,
        encounterLocations: MutableList<EncounterLocation>,
    ) {
        chosenRoom.markerPoints.monsterPos.onEach {
            val absPos = getAbsolutPos(it.pos, rotation90, offsetRoomOrigin)
            spawnLocations.add(SpawnPosition(absPos))
        }

        chosenRoom.markerPoints.bossPos.onEach {
            val absPos = getAbsolutPos(it.pos, rotation90, offsetRoomOrigin)
            val rotDeg = rotation90 * 90.0
            val newRotation = ((it.rot + rotDeg) % 360)
            bossSpawnLocations.add(SpawnPosition(absPos, newRotation))
        }

        chosenRoom.markerPoints.encounterPos.onEach {
            val absPos = getAbsolutPos(it, rotation90, offsetRoomOrigin)
            encounterLocations.add(EncounterLocation(absPos, entryDoor.getAdjacentPosition()))
        }
    }

    private fun getAbsolutPos(
        relativePos: Vec3i,
        rotation90: Int,
        offset: Vec3i,
    ) = relativePos.rotateY90(rotation90).add(offset)
}