package de.fuballer.mcendgame.main.component.entity.custom.entities.bandit.behaviour

import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation
import net.minecraft.world.level.Level
import net.minecraft.world.level.pathfinder.Node
import net.minecraft.world.level.pathfinder.Path
import net.minecraft.world.phys.Vec3
import kotlin.math.abs
import kotlin.math.min

class BanditPathNavigation(
    mob: Mob,
    level: Level,
) : GroundPathNavigation(mob, level) {
    companion object {
        private const val LOOK_AHEAD_NODES = 6
        private const val WAYPOINT_DISTANCE = 1.0
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
        if (verticalDifference > 0.0 || verticalDifference < 5.0) return false

        val targetPosition = path.getEntityPosAtNode(mob, index)
        return isClearForMovementBetween(mob, mobPosition, targetPosition, false)
    }

    private fun isAtNode(node: Node): Boolean {
        val nodePosition = Vec3.atBottomCenterOf(node.asBlockPos())

        val dx = abs(mob.x - nodePosition.x + 0.5)
        val dy = abs(mob.y - nodePosition.y)
        val dz = abs(mob.z - nodePosition.z + 0.5)
        return dx <= WAYPOINT_DISTANCE && dz <= WAYPOINT_DISTANCE && dy <= maxVerticalDistanceToWaypoint
    }
}