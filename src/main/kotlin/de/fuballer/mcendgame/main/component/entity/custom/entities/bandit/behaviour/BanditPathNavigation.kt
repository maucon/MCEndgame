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
        while (!path.isDone && isAtNode(path.nextNode)) path.advance()

        if (path.isDone) {
            doStuckDetection(mobPosition)
            return
        }

        val currentIndex = path.nextNodeIndex
        val furthestReachableIndex = findFurthestReachableNode(path, currentIndex, mobPosition)
        if (furthestReachableIndex > currentIndex) path.nextNodeIndex = furthestReachableIndex

        doStuckDetection(mobPosition)
    }

    private fun findFurthestReachableNode(
        path: Path,
        currentIndex: Int,
        mobPosition: Vec3,
    ): Int {
        val lastIndex = min(currentIndex + LOOK_AHEAD_NODES, path.nodeCount - 1)
        var reachableIndex = currentIndex + 1
        while (reachableIndex <= lastIndex && canSkipToNode(path, mobPosition, reachableIndex)) reachableIndex++
        return reachableIndex - 1
    }

    private fun canSkipToNode(
        path: Path,
        mobPosition: Vec3,
        index: Int,
    ): Boolean {
        val node = path.getNode(index)

        val verticalDifference = node.y - mobPosition.y
        if (abs(verticalDifference) > 1.0) return false

        val target = path.getEntityPosAtNode(mob, index)
        return canMoveDirectly(mobPosition, target)
    }

    private fun isAtNode(node: Node): Boolean {
        val nodePosition = Vec3.atBottomCenterOf(node.asBlockPos())

        val dx = abs(mob.x - nodePosition.x)
        val dz = abs(mob.z - nodePosition.z)
        val dy = abs(mob.y - nodePosition.y)

        return dx <= WAYPOINT_DISTANCE && dz <= WAYPOINT_DISTANCE && dy <= maxVerticalDistanceToWaypoint
    }
}