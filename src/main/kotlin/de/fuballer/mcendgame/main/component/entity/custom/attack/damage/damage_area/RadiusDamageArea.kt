package de.fuballer.mcendgame.main.component.entity.custom.attack.damage.damage_area

import de.fuballer.mcendgame.main.util.extension.EntityExtension.getDistanceToGround
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class RadiusDamageArea(
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
        scale: Double
    ) {
        // TODO only for debug
    }
}