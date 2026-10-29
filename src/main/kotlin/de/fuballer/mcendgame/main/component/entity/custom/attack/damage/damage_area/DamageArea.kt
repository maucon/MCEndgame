package de.fuballer.mcendgame.main.component.entity.custom.attack.damage.damage_area

import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3

abstract class DamageArea {
    abstract fun intersects(
        entity: LivingEntity,
        originEntity: Entity,
        forward: Vec3,
        sideways: Vec3,
        scale: Double
    ): Boolean

    abstract fun getCenter(
        originEntity: Entity,
        scale: Double,
        forward: Vec3,
        sideways: Vec3,
    ): Vec3

    abstract fun getAxisAlignedBoxApproximation(
        originEntity: Entity,
        scale: Double,
        forward: Vec3,
        sideways: Vec3,
    ): AABB

    fun getRandomContainedXZPositions(
        count: Int,
        at: Entity,
        scale: Double,
        forward: Vec3,
        sideways: Vec3,
    ): List<Vec3> {
        val center = getCenter(at, scale, forward, sideways)
        val positions = mutableListOf<Vec3>()
        repeat(count) { positions.add(getRandomContainedXZPos(center, scale, forward, sideways)) }
        return positions
    }

    abstract fun getRandomContainedXZPos(
        center: Vec3,
        scale: Double,
        forward: Vec3,
        sideways: Vec3,
    ): Vec3

    // for debugging / dev
    abstract fun renderOutline(
        world: ServerLevel,
        originEntity: Entity,
        forward: Vec3,
        sideways: Vec3,
        scale: Double,
    )
}