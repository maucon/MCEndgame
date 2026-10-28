package de.fuballer.mcendgame.main.component.entity.custom.entities.magma_spike


import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.Mth
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.phys.Vec3
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

object MagmaSpikesUtil {
    fun castMagmaSpikesLine(
        caster: LivingEntity,
        target: Entity,
        origin: Vec3,
        offset: Float,
        delay: Int,
        minScale: Float,
        maxScale: Float,
        finalScale: Float,
        finalDelay: Int,
        level: ServerLevel,
    ) {
        val minY = min(target.y, origin.y)
        val maxY = max(target.y, origin.y) + 1.0
        val angleToTarget = Mth.atan2(target.z - origin.z, target.x - origin.x).toFloat()

        val distanceToTarget = origin.distanceTo(target.position())
        val spikesCount = (distanceToTarget / offset).toInt() + 1
        for (i in 1..spikesCount) {
            val reach = offset * i
            val spikeIndexPercentage = i / spikesCount.toFloat()
            val isFinal = i == spikesCount
            createMagmaSpikeEntity(
                caster,
                origin.x + Mth.cos(angleToTarget.toDouble()) * reach,
                origin.z + Mth.sin(angleToTarget.toDouble()) * reach,
                minY,
                maxY,
                yRotRad = angleToTarget - Mth.HALF_PI,
                xzRotRad = Math.toRadians(45.0 - (45.0 * spikeIndexPercentage)).toFloat(),
                scale = if (isFinal) finalScale else minScale + ((maxScale - minScale) * spikeIndexPercentage),
                delayTicks = (delay * spikeIndexPercentage).toInt() + if (isFinal) finalDelay else 0,
                isFinal,
                level,
            )
        }
    }

    fun castMagmaSpikesSlam(
        caster: LivingEntity,
        ringDelay: Int,
        minScale: Float,
        maxScale: Float,
        ringSpikeCount: List<Int>,
        ringRadius: Float,
        level: ServerLevel,
    ) {
        ringSpikeCount.forEachIndexed { index, count ->
            val ringPercentage = (index + 1).toFloat() / ringSpikeCount.size
            val randomAngle = Random.nextFloat() * Mth.TWO_PI

            for (i in 0 until count) {
                val angle = randomAngle + Mth.TWO_PI.toDouble() * i / count
                val reach = ringRadius * (index + 1)

                createMagmaSpikeEntity(
                    caster,
                    caster.x + Mth.cos(angle) * reach,
                    caster.z + Mth.sin(angle) * reach,
                    caster.y,
                    caster.y + 1.0,
                    yRotRad = (angle - Mth.HALF_PI).toFloat(),
                    xzRotRad = Math.toRadians(45.0 - (45.0 * ringPercentage)).toFloat(),
                    scale = minScale + ((maxScale - minScale) * ringPercentage),
                    delayTicks = (ringDelay * ringPercentage).toInt(),
                    false,
                    level,
                )
            }
        }
    }

    private fun createMagmaSpikeEntity(
        caster: LivingEntity,
        x: Double,
        z: Double,
        minY: Double,
        maxY: Double,
        yRotRad: Float,
        xzRotRad: Float,
        scale: Float,
        delayTicks: Int,
        large: Boolean,
        level: ServerLevel,
    ) {
        var pos = BlockPos.containing(x, maxY, z)
        var success = false
        var topOffset = 0.0
        do {
            val below = pos.below()
            val belowState: BlockState = level.getBlockState(below)
            if (!belowState.isFaceSturdy(level, below, Direction.UP)) {
                pos = pos.below()
                continue
            }

            if (!level.isEmptyBlock(pos)) {
                val blockState: BlockState = level.getBlockState(pos)
                val shape = blockState.getCollisionShape(level, pos)
                if (!shape.isEmpty) topOffset = shape.max(Direction.Axis.Y)
            }

            success = true
            break
        } while (pos.y >= Mth.floor(minY) - 1)

        if (success) {
            level.addFreshEntity(MagmaSpikeEntity(level, x, pos.y + topOffset, z, yRotRad, xzRotRad, scale, delayTicks, caster, large))
            level.gameEvent(GameEvent.ENTITY_PLACE, Vec3(x, pos.y + topOffset, z), GameEvent.Context.of(caster))
        }
    }
}