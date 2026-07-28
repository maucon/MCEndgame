package de.fuballer.mcendgame.main.util

import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level

object FindBlockPosUtil {
    fun findEmptyAboveSolid(
        level: Level,
        startPos: BlockPos,
        steps: Int,
    ): List<BlockPos> {
        val squaredRadius = steps * steps

        val possiblePositions = mutableListOf<BlockPos>()

        var heads = listOf(startPos)
        val checkedPositions = mutableSetOf(startPos)
        while (heads.isNotEmpty()) {
            val newHeads = mutableListOf<BlockPos>()

            for (head in heads) {
                if (head.distSqr(startPos) > squaredRadius) continue

                val below = head.below()
                val belowState = level.getBlockState(below)
                if (belowState.isRedstoneConductor(level, below)) {
                    possiblePositions.add(head)
                }

                val neighbors = listOf(
                    head.north(),
                    head.south(),
                    head.east(),
                    head.west(),
                    head.above(),
                    head.below()
                )

                for (neighbor in neighbors) {
                    if (neighbor in checkedPositions) continue
                    checkedPositions.add(neighbor)

                    val state = level.getBlockState(neighbor)
                    val isEmpty = state.getCollisionShape(level, neighbor).isEmpty
                    if (isEmpty) newHeads.add(neighbor)
                }
            }

            heads = newHeads
        }

        return possiblePositions
    }
}