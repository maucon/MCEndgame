package de.fuballer.mcendgame.main.component.entity.custom.attack.data

import net.minecraft.core.particles.ParticleOptions
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.phys.Vec3

class DelayedParticleData(
    private val particle: (ServerLevel, Mob) -> ParticleOptions,
    private val offset: (Mob) -> Vec3,
    private val count: Int,
    private val dist: (Mob) -> Vec3,
    private val speed: Double,
    delay: Int = 0,
) : DelayedAttackData(delay) {
    override fun apply(
        world: ServerLevel,
        entity: Mob,
        target: LivingEntity?,
    ) {
        val scaledOffset = offset(entity).scale(entity.scale.toDouble())
        val pos = entity.position().add(scaledOffset)
        val d = dist(entity)
        world.sendParticles(
            particle(world, entity),
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