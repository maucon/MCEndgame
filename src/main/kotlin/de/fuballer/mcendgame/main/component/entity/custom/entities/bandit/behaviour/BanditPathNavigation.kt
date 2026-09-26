package de.fuballer.mcendgame.main.component.entity.custom.entities.bandit.behaviour

import net.minecraft.core.BlockPos
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation
import net.minecraft.world.level.Level
import net.minecraft.world.level.pathfinder.Node
import net.minecraft.world.level.pathfinder.Path
import net.minecraft.world.phys.Vec3
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.min

class BanditPathNavigation(
    mob: Mob,
    level: Level,
) : GroundPathNavigation(mob, level) {
    companion object {
        private const val LOOK_AHEAD_NODES = 10
        private const val AIRBORNE_VERTICAL_TOLERANCE = 2.0F
    }

    override fun canUpdatePath(): Boolean = true

    override fun getMaxVerticalDistanceToWaypoint(): Float {
        return if (mob.onGround()) super.getMaxVerticalDistanceToWaypoint()
        else AIRBORNE_VERTICAL_TOLERANCE
    }

    override fun followThePath() {
        val path = path ?: return
        if (path.isDone) return

        val mobPosition = tempMobPos
        if (!path.isDone && isAtNode(path.nextNode)) path.advance()
        if (path.isDone) {
            doStuckDetection(mobPosition)
            return
        }

        path.nextNodeIndex = findFurthestReachableNode(path, mobPosition)
        doStuckDetection(mobPosition)
    }

    private fun findFurthestReachableNode(
        path: Path,
        mobPosition: Vec3,
    ): Int {
        val lastIndex = min(path.nextNodeIndex + LOOK_AHEAD_NODES, path.nodeCount - 1)
        var reachableIndex = path.nextNodeIndex + 1
        while (reachableIndex <= lastIndex && canSkipToNode(path, mobPosition, reachableIndex)) reachableIndex++
        return reachableIndex - 1
    }

    private fun canSkipToNode(
        path: Path,
        mobPosition: Vec3,
        index: Int,
    ): Boolean {
        val baseNode = path.nextNode
        val targetNode = path.getNode(index)

        val verticalDifference = targetNode.y - baseNode.y
        if (verticalDifference > 0.0 || verticalDifference < -5.0) return false

        val targetPosition = path.getEntityPosAtNode(mob, index)
        if (!isClearForMovementBetween(mob, mobPosition, targetPosition, false)) return false
        if (!hasGroundAlongPath(mobPosition, targetPosition)) return false

        return true
    }

    private fun hasGroundAlongPath(
        start: Vec3,
        end: Vec3,
    ): Boolean {
        val distance = start.distanceTo(end)
        val steps = ceil(distance).toInt()

        for (step in 0..steps) {
            val percentage = step.toDouble() / steps.coerceAtLeast(1)
            val pos = start.lerp(end, percentage)

            val blockPos = BlockPos.containing(pos)
            val below = blockPos.below()

            if (!mob.level().getBlockState(below).isSolidRender) return false
        }

        return true
    }

    private fun isAtNode(node: Node): Boolean {
        val nodePosition = Vec3.atBottomCenterOf(node.asBlockPos())

        val dx = abs(mob.x - nodePosition.x)
        val dy = abs(mob.y - nodePosition.y)
        val dz = abs(mob.z - nodePosition.z)

        maxDistanceToWaypoint = if (mob.bbWidth > 0.75f) mob.bbWidth / 2.0f else 0.75f - mob.bbWidth / 2.0f
        return dx <= maxDistanceToWaypoint && dz <= maxDistanceToWaypoint && dy <= maxVerticalDistanceToWaypoint
    }
}