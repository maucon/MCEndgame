package de.fuballer.mcendgame.main.component.entity.custom.attack.damage.damage_area

import de.fuballer.mcendgame.main.util.extension.EntityExtension.getDistanceToGround
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

class BoxDamageArea(
    private val forwardRange: Double, // only forward
    private val sideRange: Double, // left & right -> 2* sideRange
    private val heightRange: Double, // up & down -> 2* heightRange
    private val forwardOffset: Double = 0.0, // positive -> forward
    private val sideOffset: Double = 0.0, // positive -> right
    private val heightOffset: Double = 0.0, // positive -> up
    private val offsetToGround: Boolean = false,
) : DamageArea() {
    override fun intersects(
        entity: LivingEntity,
        originEntity: Entity,
        forward: Vec3,
        sideways: Vec3,
        scale: Double
    ): Boolean {
        val bb = entity.boundingBox

        val areaOrigin = if (!offsetToGround) originEntity.position() else originEntity.position().subtract(0.0, originEntity.getDistanceToGround(), 0.0)

        return intersectsVertical(bb, areaOrigin, scale) && intersectsHorizontal(bb, areaOrigin, forward, sideways, scale)
    }

    private fun intersectsVertical(
        boundingBox: AABB,
        areaOrigin: Vec3,
        scale: Double,
    ): Boolean {
        val entityMinHeight = boundingBox.minY - areaOrigin.y
        val entityMaxHeight = boundingBox.maxY - areaOrigin.y

        val areaMinHeight = (-heightRange + heightOffset) * scale
        val areaMaxHeight = (heightRange + heightOffset) * scale

        return entityMinHeight <= areaMaxHeight && entityMaxHeight >= areaMinHeight
    }

    // using Separating Axis Theorem
    private fun intersectsHorizontal(
        boundingBox: AABB,
        areaOrigin: Vec3,
        forward: Vec3,
        sideways: Vec3,
        scale: Double,
    ): Boolean {
        val areaHalfForward = forwardRange * scale / 2.0
        val areaHalfSide = sideRange * scale
        val areaCenter = areaOrigin
            .add(forward.scale((forwardOffset + forwardRange / 2.0) * scale))
            .add(sideways.scale(sideOffset * scale))

        val entityHalfX = (boundingBox.maxX - boundingBox.minX) / 2.0
        val entityHalfZ = (boundingBox.maxZ - boundingBox.minZ) / 2.0
        val entityCenter = Vec3(
            boundingBox.minX + entityHalfX,
            0.0,
            boundingBox.minZ + entityHalfZ,
        )

        val delta = entityCenter.subtract(Vec3(areaCenter.x, 0.0, areaCenter.z))

        val axes = listOf(
            forward,
            sideways,
            Vec3(1.0, 0.0, 0.0),
            Vec3(0.0, 0.0, 1.0),
        )

        for (axis in axes) {
            val axis2D = Vec3(axis.x, 0.0, axis.z).normalize()
            val distance = abs(delta.dot(axis2D))

            val areaRadius = areaHalfForward * abs(forward.dot(axis2D)) + areaHalfSide * abs(sideways.dot(axis2D))
            val entityRadius = entityHalfX * abs(axis2D.x) + entityHalfZ * abs(axis2D.z)

            if (distance > areaRadius + entityRadius) return false
        }

        return true
    }

    override fun getCenter(
        originEntity: Entity,
        scale: Double,
        forward: Vec3,
        sideways: Vec3,
    ): Vec3 {
        var center = originEntity.position()
            .add(forward.scale((forwardOffset + forwardRange / 2) * scale))
            .add(sideways.scale(sideOffset * scale))

        center = if (offsetToGround) center.subtract(0.0, originEntity.getDistanceToGround(), 0.0)
        else center.add(0.0, heightOffset * scale, 0.0)

        return center
    }

    override fun getAxisAlignedBoxApproximation(
        originEntity: Entity,
        scale: Double,
        forward: Vec3,
        sideways: Vec3,
    ): AABB {
        val center = getCenter(originEntity, scale, forward, sideways)
        val maxHorizontalDistance = getMaxHorizontalDistance(scale)
        return AABB(
            center.x - maxHorizontalDistance,
            center.y - heightRange * scale,
            center.z - maxHorizontalDistance,
            center.x + maxHorizontalDistance,
            center.y + heightRange * scale,
            center.z + maxHorizontalDistance,
        )
    }

    private fun getMaxHorizontalDistance(scale: Double): Double {
        val maxForward = max(forwardRange + forwardOffset, -forwardOffset)
        val maxSideways = sideRange + abs(sideOffset)
        val distance = sqrt(maxForward * maxForward + maxSideways * maxSideways)
        return distance * scale
    }

    override fun getRandomContainedXZPos(
        center: Vec3,
        scale: Double,
        forward: Vec3,
        sideways: Vec3,
    ): Vec3 {
        val forwardRandomOffset = getRandomForwardPos(scale)
        val sidewaysRandomOffset = getRandomSidewaysPos(scale)
        val particlePos = center
            .add(forward.scale(forwardRandomOffset))
            .add(sideways.scale(sidewaysRandomOffset))
        return particlePos
    }

    private fun getRandomForwardPos(scale: Double) = (forwardRange / 2 * Random.nextDouble().pow(2) * if (Random.nextBoolean()) 1 else -1) * scale
    private fun getRandomSidewaysPos(scale: Double) = (sideRange * Random.nextDouble().pow(2) * if (Random.nextBoolean()) 1 else -1) * scale

    // for debugging / dev
    override fun renderOutline(
        world: ServerLevel,
        originEntity: Entity,
        forward: Vec3,
        sideways: Vec3,
        scale: Double,
    ) {
        var origin = originEntity.position()
        if (offsetToGround) origin = origin.subtract(0.0, originEntity.getDistanceToGround(), 0.0)

        val minForward = forwardOffset * scale
        val maxForward = (forwardOffset + forwardRange) * scale

        val minSide = (-sideRange + sideOffset) * scale
        val maxSide = (sideRange + sideOffset) * scale

        val minHeight = (-heightRange + heightOffset) * scale
        val maxHeight = (heightRange + heightOffset) * scale

        fun point(f: Double, s: Double, h: Double): Vec3 {
            return origin
                .add(forward.scale(f))
                .add(sideways.scale(s))
                .add(0.0, h, 0.0)
        }

        val corners = arrayOf(
            point(minForward, minSide, minHeight),
            point(minForward, maxSide, minHeight),
            point(maxForward, minSide, minHeight),
            point(maxForward, maxSide, minHeight),
            point(minForward, minSide, maxHeight),
            point(minForward, maxSide, maxHeight),
            point(maxForward, minSide, maxHeight),
            point(maxForward, maxSide, maxHeight),
        )

        val edges = arrayOf(
            0 to 1,
            0 to 2,
            1 to 3,
            2 to 3,

            4 to 5,
            4 to 6,
            5 to 7,
            6 to 7,

            0 to 4,
            1 to 5,
            2 to 6,
            3 to 7,
        )

        for ((a, b) in edges) {
            renderLine(world, corners[a], corners[b])
        }
    }

    private fun renderLine(
        world: ServerLevel,
        from: Vec3,
        to: Vec3,
    ) {
        val particle = ParticleTypes.END_ROD
        val distance = from.distanceTo(to)
        val steps = (distance * 8).toInt().coerceAtLeast(1)
        for (i in 0..steps) {
            val t = i.toDouble() / steps

            val pos = from.lerp(to, t)

            world.sendParticles(
                particle,
                pos.x,
                pos.y,
                pos.z,
                1,
                0.0,
                0.0,
                0.0,
                0.0
            )
        }
    }
}