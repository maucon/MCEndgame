package de.fuballer.mcendgame.main.component.entity.custom.attack.data.particle

import net.minecraft.core.particles.ParticleOptions
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.phys.Vec3

data class ParticleData(
    private val particle: (ServerLevel, Entity) -> ParticleOptions,
    private val offset: (Entity) -> Vec3,
    private val count: Int,
    private val dist: (Entity) -> Vec3,
    private val speed: Double,
    private val applyScale: Boolean = true,
) {
    fun apply(
        level: ServerLevel,
        entity: Entity,
    ) {
        var adjustedOffset = offset(entity)
        if (applyScale && entity is LivingEntity) {
            adjustedOffset = adjustedOffset.scale(entity.scale.toDouble())
        }
        val pos = entity.position().add(adjustedOffset)
        val d = dist(entity)
        level.sendParticles(
            particle(level, entity),
            pos.x,
            pos.y,
            pos.z,
            count,
            d.x,
            d.y,
            d.z,
            speed,
        )
    }
}