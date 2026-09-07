package de.fuballer.mcendgame.main.component.entity.custom.entities.bandit.behaviour

import de.fuballer.mcendgame.main.component.entity.custom.entities.bandit.BanditEntity
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.tags.BlockTags
import net.minecraft.util.Mth
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.control.MoveControl
import net.minecraft.world.level.pathfinder.PathType
import kotlin.math.max

class BanditMoveControl(
    banditEntity: BanditEntity
) : MoveControl<BanditEntity>(banditEntity) {
    var isBlocking = false
    var isPullingBow = false

    override fun tick() {
        when (operation) {
            Operation.STRAFE -> tickStrafe()
            Operation.MOVE_TO -> tickMoveTo()
            Operation.JUMPING -> tickMoveTo(true)
            else -> mob.setZza(0.0f)
        }
    }

    private fun tickStrafe() {
        val speed = getModifiedSpeed()
        var xa = strafeForwards
        var za = strafeRight
        var dist = Mth.sqrt(xa * xa + za * za)
        if (dist < 1.0f) {
            dist = 1.0f
        }

        mob.target?.also { tickRotate(it.x - mob.x, it.z - mob.z) }

        dist = speed / dist
        xa *= dist
        za *= dist
        val sin = Mth.sin((mob.yRot * (Math.PI / 180.0).toFloat()).toDouble())
        val cos = Mth.cos((mob.yRot * (Math.PI / 180.0).toFloat()).toDouble())
        val dx = xa * cos - za * sin
        val dz = za * cos + xa * sin
        if (!isWalkable(dx, dz)) {
            strafeForwards = 1.0f
            strafeRight = 0.0f
        }

        mob.setSpeed(speed)
        mob.setZza(strafeForwards)
        mob.setXxa(strafeRight)
        operation = Operation.WAIT
    }

    private fun tickMoveTo(jumping: Boolean = false) {
        operation = Operation.WAIT
        val xd = wantedX - mob.x
        val zd = wantedZ - mob.z
        val yd = wantedY - mob.y
        val dd = xd * xd + yd * yd + zd * zd
        if (dd < 2.5000003E-7f) {
            mob.setZza(0.0f)
            return
        }

        tickRotate(xd, zd)
        mob.setSpeed(getModifiedSpeed())

        if (jumping) {
            if (mob.onGround() || mob.isInLiquid && mob.isAffectedByFluids) operation = Operation.WAIT
            return
        }

        val pos = mob.blockPosition()
        val blockState = mob.level().getBlockState(pos)
        val shape = blockState.getCollisionShape(mob.level(), pos)
        if (yd > mob.maxUpStep() && xd * xd + zd * zd < max(1.0f, mob.bbWidth)
            || !shape.isEmpty && mob.y < shape.max(Direction.Axis.Y) + pos.y && !blockState.`is`(BlockTags.DOORS) && !blockState.`is`(BlockTags.FENCES)
        ) {
            mob.getJumpControl().jump()
            operation = Operation.JUMPING
        }
    }

    private fun tickRotate(
        xd: Double,
        zd: Double
    ) {
        val yRotD = (Mth.atan2(zd, xd) * 180.0f / Math.PI.toFloat()).toFloat() - 90.0f
        mob.yRot = rotlerp(mob.yRot, yRotD, 90.0f)
    }

    private fun isWalkable(dx: Float, dz: Float): Boolean {
        val pathNavigation = mob.getNavigation()
        val nodeEvaluator = pathNavigation.getNodeEvaluator()
        val pathType = nodeEvaluator.getPathType(mob, BlockPos.containing(mob.x + dx, mob.blockY.toDouble(), mob.z + dz))
        return (pathType == PathType.WALKABLE)
    }

    fun jump() {
        mob.getJumpControl().jump()
        operation = Operation.JUMPING
    }

    override fun strafe(
        forwards: Float,
        right: Float,
    ) {
        operation = Operation.STRAFE
        strafeForwards = forwards
        strafeRight = right
        speedModifier = 0.5
    }

    private fun getModifiedSpeed(): Float {
        var speed = mob.getAttributeValue(Attributes.MOVEMENT_SPEED)
        speed *= speedModifier
        if (isBlocking || isPullingBow) speed *= 0.3
        return speed.toFloat()
    }
}