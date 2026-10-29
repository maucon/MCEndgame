package de.fuballer.mcendgame.main.component.entity.custom.attack.damage.damage_area

import de.fuballer.mcendgame.main.util.extension.EntityExtension.getDistanceToGround
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class SphereDamageArea(
    private val radius: Double,
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
        val center = getCenter(originEntity, scale, forward, sideways)
        return entity.distanceToSqr(center) <= (radius * scale).pow(2)
    }

    override fun getCenter(
        originEntity: Entity,
        scale: Double,
        forward: Vec3,
        sideways: Vec3
    ): Vec3 {
        var center = originEntity.position()
            .add(forward.scale(forwardOffset * scale))
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
        val rS = radius * scale
        return AABB(
            center.x - rS,
            center.y - rS,
            center.z - rS,
            center.x + rS,
            center.y + rS,
            center.z + rS,
        )
    }

    override fun getRandomContainedXZPos(
        center: Vec3,
        scale: Double,
        forward: Vec3,
        sideways: Vec3
    ): Vec3 {
        val scaledRadius = radius * scale

        val angle = Math.random() * 2.0 * Math.PI
        val distance = sqrt(Math.random()) * scaledRadius

        val x = center.x + cos(angle) * distance
        val z = center.z + sin(angle) * distance

        return Vec3(x, center.y, z)
    }

    override fun renderOutline(
        world: ServerLevel,
        originEntity: Entity,
        forward: Vec3,
        sideways: Vec3,
        scale: Double,
    ) {
        val center = getCenter(originEntity, scale, forward, sideways)
        val r = radius * scale

        val latitudeCount = 24
        val longitudeCount = 48

        for (i in 0..latitudeCount) {
            val latitude = -Math.PI / 2.0 + Math.PI * i / latitudeCount
            val ringRadius = r * cos(latitude)
            val y = r * sin(latitude)

            val pointsInRing = (longitudeCount * cos(latitude)).toInt().coerceAtLeast(1)

            for (j in 0 until pointsInRing) {
                val longitude = 2.0 * Math.PI * j / pointsInRing

                val x = center.x + ringRadius * cos(longitude)
                val py = center.y + y
                val z = center.z + ringRadius * sin(longitude)

                world.sendParticles(
                    ParticleTypes.END_ROD,
                    x, py, z,
                    1,
                    0.0, 0.0, 0.0,
                    0.0
                )
            }
        }
    }
}