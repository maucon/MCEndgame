package de.fuballer.mcendgame.main.component.entity.custom.entities.magma_spike


import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.Mth
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

object MagmaSpikesUtil {
    fun castMagmaSpikesLine(
        caster: LivingEntity,
        damagePercentage: Double,
        largeDamagePercentage: Double,
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
        val minY = min(target.y, origin.y) - 0.1
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
                damagePercentage,
                largeDamagePercentage,
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
        damagePercentage: Double,
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
                    damagePercentage,
                    damagePercentage,
                    caster.x + Mth.cos(angle) * reach,
                    caster.z + Mth.sin(angle) * reach,
                    caster.y - 1.1,
                    caster.y + 2.0,
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
        damagePercentage: Double,
        largeDamagePercentage: Double,
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

        while (pos.y >= Mth.floor(minY) - 1) {
            val below = pos.below()
            val belowState = level.getBlockState(below)
            val collisionShape = belowState.getCollisionShape(level, below)

            if (!collisionShape.isEmpty) {
                val surfaceHeight = collisionShape.max(Direction.Axis.Y)
                val spawnY = below.y + surfaceHeight

                val scaledHalfWidth = MagmaSpikeEntity.WIDTH / 2 * scale
                val box = AABB(x - scaledHalfWidth, spawnY, z - scaledHalfWidth, x + scaledHalfWidth, spawnY + MagmaSpikeEntity.HEIGHT * scale, z + scaledHalfWidth)
                if (level.noCollision(box)) {
                    topOffset = surfaceHeight
                    pos = below
                    success = true
                    break
                }
            }

            pos = pos.below()
        }

        if (success) {
            val spawnY = pos.y + topOffset
            level.addFreshEntity(MagmaSpikeEntity(level, x, spawnY, z, yRotRad, xzRotRad, scale, delayTicks, caster, damagePercentage, largeDamagePercentage, large))
            level.gameEvent(GameEvent.ENTITY_PLACE, Vec3(x, spawnY, z), GameEvent.Context.of(caster))
        }
    }
}