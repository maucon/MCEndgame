package de.fuballer.mcendgame.component.dungeon.generation

import de.fuballer.mcendgame.component.dungeon.generation.data.LayoutTile
import java.awt.Point
import java.util.*
import kotlin.math.abs

class DungeonLayoutGenerator {
    private var layoutTiles: Array<Array<LayoutTile>> = arrayOf()
    private val unchosenTiles: MutableList<Point> = mutableListOf()

    private var bossRoomPosition = Point(0, 0)
    private var startRoomPosition = Point(0, 0)

    private val random = Random()

    fun getLayout() = layoutTiles
    fun getBossRoomPos() = bossRoomPosition
    fun getStartRoomPos() = startRoomPosition

    fun generateDungeon(
        layoutWidth: Int,
        layoutHeight: Int,
        junctionProbability: Double,
        maxAdjacentTilePathLengthDifference: Int
    ) {
        setUpTiles(layoutWidth, layoutHeight)

        chooseRandomTiles(junctionProbability)

        bossRoomPosition = Point(DungeonGenerationSettings.DUNGEON_BOSS_ROOM_X_TILE, 0)
        connectEverythingToPoint(bossRoomPosition, layoutWidth * layoutHeight)

        startRoomPosition = fixMaxAdjacentTilePathLengthDifference(bossRoomPosition, maxAdjacentTilePathLengthDifference, canConnectToStart = true, guaranteeDeadEnd = true)!!
        fixMaxAdjacentTilePathLengthDifference(startRoomPosition, maxAdjacentTilePathLengthDifference, canConnectToStart = false, guaranteeDeadEnd = false)

        layoutTiles[bossRoomPosition.x][bossRoomPosition.y].up = true
    }

    private fun setUpTiles(
        layoutWidth: Int,
        layoutHeight: Int
    ) {
        layoutTiles = Array(layoutWidth) { Array(layoutHeight) { LayoutTile(up = true, right = true, down = true, left = true) } }

        for (x in 0 until layoutWidth) {
            for (y in 0 until layoutHeight) {
                layoutTiles[x][y] = LayoutTile(y > 0, x < layoutWidth - 1, y < layoutHeight - 1, x > 0)
                unchosenTiles.add(Point(x, y))
            }
        }
    }

    private fun chooseRandomTiles(junctionProbability: Double) {
        while (unchosenTiles.isNotEmpty())
            chooseRandomTile(junctionProbability)
    }

    private fun chooseRandomTile(junctionProbability: Double) {
        val chosenTilePos = unchosenTiles[random.nextInt(unchosenTiles.size)]
        unchosenTiles.remove(chosenTilePos)

        updateChosenTile(chosenTilePos, junctionProbability)
    }

    private fun updateChosenTile(
        chosenTilePos: Point,
        junctionProbability: Double
    ) {
        val chosenTile = layoutTiles[chosenTilePos.x][chosenTilePos.y]

        val possibleWays = chosenTile.getWays()
        if (possibleWays.isEmpty()) return

        val guaranteedWay = possibleWays[random.nextInt(possibleWays.size)]

        if (guaranteedWay != "up" && chosenTile.up && random.nextDouble() > junctionProbability) {
            chosenTile.up = false
            layoutTiles[chosenTilePos.x][chosenTilePos.y - 1].down = false
        }
        if (guaranteedWay != "right" && chosenTile.right && random.nextDouble() > junctionProbability) {
            chosenTile.right = false
            layoutTiles[chosenTilePos.x + 1][chosenTilePos.y].left = false
        }
        if (guaranteedWay != "down" && chosenTile.down && random.nextDouble() > junctionProbability) {
            chosenTile.down = false
            layoutTiles[chosenTilePos.x][chosenTilePos.y + 1].up = false
        }
        if (guaranteedWay != "left" && chosenTile.left && random.nextDouble() > junctionProbability) {
            chosenTile.left = false
            layoutTiles[chosenTilePos.x - 1][chosenTilePos.y].right = false
        }
    }

    private fun connectEverythingToPoint(
        startPoint: Point,
        minConnectedTiles: Int
    ) {
        val connectedTilesPos: MutableList<Point> = mutableListOf()
        val possibleConnectionPoints = findNewConnectedTiles(startPoint, connectedTilesPos)

        while (connectedTilesPos.size < minConnectedTiles) {
            val newConnectedPoint = addConnection(possibleConnectionPoints, connectedTilesPos)
            possibleConnectionPoints.addAll(findNewConnectedTiles(newConnectedPoint, connectedTilesPos))
        }
    }

    private fun findNewConnectedTiles(
        startPoint: Point,
        connectedTilesPos: MutableList<Point>
    ): MutableList<Point> {
        val foundTiles: MutableList<Point> = mutableListOf(startPoint)
        connectedTilesPos.add(startPoint)

        var heads: List<Point> = listOf(startPoint)
        while (heads.isNotEmpty()) {
            val newHeads: MutableList<Point> = mutableListOf()

            for (head in heads) {

                for (adjConTile in getAdjacentConnectedTiles(head)) {
                    if (connectedTilesPos.contains(adjConTile)) continue

                    foundTiles.add(adjConTile)
                    connectedTilesPos.add(adjConTile)
                    newHeads.add(adjConTile)
                }
            }

            heads = newHeads
        }

        return foundTiles
    }

    private fun getAdjacentConnectedTiles(tilePos: Point): List<Point> {
        val tile = layoutTiles[tilePos.x][tilePos.y]

        val foundTiles: MutableList<Point> = mutableListOf()

        if (tile.up) foundTiles.add(Point(tilePos.x, tilePos.y - 1))
        if (tile.right) foundTiles.add(Point(tilePos.x + 1, tilePos.y))
        if (tile.down) foundTiles.add(Point(tilePos.x, tilePos.y + 1))
        if (tile.left) foundTiles.add(Point(tilePos.x - 1, tilePos.y))

        return foundTiles
    }

    private fun addConnection(
        possibleConnectionPoints: MutableList<Point>,
        connectedTilesPos: List<Point>
    ): Point {
        while (true) {
            val posToTest = possibleConnectionPoints[random.nextInt(possibleConnectionPoints.size)]

            val validNewConnectionDirections: MutableList<String> = mutableListOf()

            for (direction in layoutTiles[posToTest.x][posToTest.y].getBlockedWays()) {
                val adjacentPos = getAdjacentTilePosInDirection(posToTest, direction)
                if (adjacentPos.x < 0 || adjacentPos.x >= layoutTiles.size || adjacentPos.y < 0 || adjacentPos.y >= layoutTiles[0].size)
                    continue

                if (!connectedTilesPos.contains(adjacentPos))
                    validNewConnectionDirections.add(direction)
            }

            if (validNewConnectionDirections.size <= 1) {
                possibleConnectionPoints.remove(posToTest)

                if (validNewConnectionDirections.isEmpty())
                    continue
            }

            val chosenDirection = validNewConnectionDirections[random.nextInt(validNewConnectionDirections.size)]
            connect(posToTest, chosenDirection, false)
            return getAdjacentTilePosInDirection(posToTest, chosenDirection)
        }
    }

    private fun getAdjacentTilePosInDirection(
        tilePos: Point,
        direction: String
    ) =
        when (direction) {
            "up" -> Point(tilePos.x, tilePos.y - 1)
            "right" -> Point(tilePos.x + 1, tilePos.y)
            "down" -> Point(tilePos.x, tilePos.y + 1)
            "left" -> Point(tilePos.x - 1, tilePos.y)
            else -> Point(tilePos.x, tilePos.y)
        }

    private fun connect(
        tilePos: Point,
        direction: String,
        disconnect: Boolean
    ) {
        when (direction) {
            "up" -> {
                layoutTiles[tilePos.x][tilePos.y].up = !disconnect
                layoutTiles[tilePos.x][tilePos.y - 1].down = !disconnect
            }

            "right" -> {
                layoutTiles[tilePos.x][tilePos.y].right = !disconnect
                layoutTiles[tilePos.x + 1][tilePos.y].left = !disconnect
            }

            "down" -> {
                layoutTiles[tilePos.x][tilePos.y].down = !disconnect
                layoutTiles[tilePos.x][tilePos.y + 1].up = !disconnect
            }

            "left" -> {
                layoutTiles[tilePos.x][tilePos.y].left = !disconnect
                layoutTiles[tilePos.x - 1][tilePos.y].right = !disconnect
            }
        }
    }

    private fun fixMaxAdjacentTilePathLengthDifference(
        startPos: Point,
        maxDifference: Int,
        canConnectToStart: Boolean,
        guaranteeDeadEnd: Boolean
    ): Point? {
        val pathLengthToTile: Array<Array<Int>> = Array(layoutTiles.size) { Array(layoutTiles[0].size) { 0 } }
        pathLengthToTile[startPos.x][startPos.y] = 1
        updatePathLengthFromPos(startPos, pathLengthToTile)

        while (true) {
            var highestDifference = 0
            var chosenTilePos = Point(0, 0)
            var chosenDirection = "right"

            for (x in 0 until layoutTiles.size - 1) {
                for (y in 0 until layoutTiles[0].size - 1) {
                    if (!canConnectToStart && startPos.x == x && startPos.y == y) continue

                    for (direction in listOf("right", "down")) {

                        val adjacentPos = getAdjacentTilePosInDirection(Point(x, y), direction)
                        if (!canConnectToStart && startPos.x == adjacentPos.x && startPos.y == adjacentPos.y) continue

                        val difference = abs(pathLengthToTile[x][y] - pathLengthToTile[adjacentPos.x][adjacentPos.y])
                        if (difference <= highestDifference)
                            continue

                        highestDifference = difference
                        chosenTilePos = Point(x, y)
                        chosenDirection = direction
                    }
                }
            }

            if (highestDifference <= maxDifference) {
                val furthestPoint = getFurthestPoint(pathLengthToTile, false)

                return if (furthestPoint != null || !guaranteeDeadEnd) furthestPoint else getFurthestPoint(pathLengthToTile, true)
            }

            connect(chosenTilePos, chosenDirection, false)
            updatePathLengthFromPos(chosenTilePos, pathLengthToTile)
        }
    }

    private fun updatePathLengthFromPos(
        startPos: Point,
        pathLengthToTile: Array<Array<Int>>
    ) {
        var heads: List<Point> = listOf(startPos)

        while (heads.isNotEmpty()) {
            val newHeads: MutableList<Point> = mutableListOf()

            for (head in heads) {
                val connectedDirections = layoutTiles[head.x][head.y].getWays()

                for (direction in connectedDirections) {
                    val adjacentPos = getAdjacentTilePosInDirection(head, direction)

                    if (pathLengthToTile[adjacentPos.x][adjacentPos.y] == 0) {
                        pathLengthToTile[adjacentPos.x][adjacentPos.y] = pathLengthToTile[head.x][head.y] + 1
                        newHeads.add(adjacentPos)
                        continue
                    }

                    if (abs(pathLengthToTile[adjacentPos.x][adjacentPos.y] - pathLengthToTile[head.x][head.y]) <= 1)
                        continue

                    if (pathLengthToTile[adjacentPos.x][adjacentPos.y] < pathLengthToTile[head.x][head.y]) {
                        pathLengthToTile[head.x][head.y] = pathLengthToTile[adjacentPos.x][adjacentPos.y] + 1
                        newHeads.add(head)
                    } else {
                        pathLengthToTile[adjacentPos.x][adjacentPos.y] = pathLengthToTile[head.x][head.y] + 1
                        newHeads.add(adjacentPos)
                    }
                }
            }

            heads = newHeads
        }
    }

    private fun getFurthestPoint(
        pathLengthToTile: Array<Array<Int>>,
        createDeadEnd: Boolean
    ): Point? {
        var highestDistance = 0
        var furthestPoint: Point? = null

        for (x in layoutTiles.indices) {
            for (y in layoutTiles[0].indices) {

                if (pathLengthToTile[x][y] <= highestDistance) continue
                if (!createDeadEnd && layoutTiles[x][y].getWaysAmount() > 1) continue

                highestDistance = pathLengthToTile[x][y]
                furthestPoint = Point(x, y)
            }
        }

        if (createDeadEnd)
            createDeadEnd(furthestPoint!!)

        return furthestPoint
    }

    private fun createDeadEnd(tilePos: Point) {
        var validPathExisting = false

        for (direction in layoutTiles[tilePos.x][tilePos.y].getWays()) {

            if (!validPathExisting) {
                validPathExisting = true
                continue
            }

            connect(tilePos, direction, true)
        }
    }
}