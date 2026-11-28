package de.fuballer.mcendgame.client.component.particle

import de.fuballer.mcendgame.main.component.particle.DirectionalFlameSweepParticleEffect
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.util.RandomSource
import net.minecraft.world.phys.Vec3

class DirectionalFlameSweepParticle(
    level: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    size: Double,
    direction: Vec3,
    sprites: SpriteSet,
) : DirectionalSweepParticle(level, x, y, z, size, direction, sprites) {
    init {
        val col = 1f
        rCol = col
        gCol = col
        bCol = col
    }

    class Factory(
        private val sprites: SpriteSet,
    ) : ParticleProvider<DirectionalFlameSweepParticleEffect> {
        override fun createParticle(
            effect: DirectionalFlameSweepParticleEffect,
            clientWorld: ClientLevel,
            x: Double,
            y: Double,
            z: Double,
            xAux: Double,
            yAux: Double,
            zAux: Double,
            random: RandomSource,
        ): Particle {
            return DirectionalFlameSweepParticle(
                clientWorld,
                x, y, z,
                effect.size,
                Vec3(effect.xDir, effect.yDir, effect.zDir),
                sprites,
            )
        }
    }
}