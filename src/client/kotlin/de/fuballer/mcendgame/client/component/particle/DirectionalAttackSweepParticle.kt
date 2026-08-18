package de.fuballer.mcendgame.client.component.particle

import de.fuballer.mcendgame.main.component.particle.DirectionalAttackSweepParticleEffect
import net.minecraft.client.Camera
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SingleQuadParticle
import net.minecraft.client.particle.SpriteSet
import net.minecraft.client.renderer.state.level.QuadParticleRenderState
import net.minecraft.util.RandomSource
import net.minecraft.world.phys.Vec3
import org.joml.Quaternionf

class DirectionalAttackSweepParticle(
    level: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    size: Double,
    private val direction: Vec3,
    private val sprites: SpriteSet,
) : SingleQuadParticle(level, x, y, z, 0.0, 0.0, 0.0, sprites.first()) {
    init {
        lifetime = 4
        val col = random.nextFloat() * 0.9f + 0.1f
        rCol = col
        gCol = col
        bCol = col
        quadSize = size.toFloat()
        setSpriteFromAge(sprites)
    }

    public override fun getLightCoords(a: Float): Int = 15728880

    override fun tick() {
        xo = x
        yo = y
        zo = z
        if (age++ >= lifetime) remove()
        else setSpriteFromAge(sprites)
    }

    public override fun getLayer(): Layer = Layer.OPAQUE

    override fun extract(
        particleTypeRenderState: QuadParticleRenderState,
        camera: Camera,
        partialTickTime: Float,
    ) {
        val rotation = Quaternionf()

        val rot = direction.rotation()
        val pitch = rot.x
        val yaw = rot.y

        rotation.rotateX(Math.toRadians(-90.0).toFloat())
        rotation.rotateZ(Math.toRadians(180.0 - yaw).toFloat())
        rotation.rotateX(Math.toRadians(-pitch.toDouble()).toFloat())

        extractRotatedQuad(particleTypeRenderState, camera, rotation, partialTickTime)
    }

    class Factory(
        private val sprites: SpriteSet
    ) : ParticleProvider<DirectionalAttackSweepParticleEffect> {
        override fun createParticle(
            effect: DirectionalAttackSweepParticleEffect,
            clientWorld: ClientLevel,
            x: Double,
            y: Double,
            z: Double,
            xAux: Double,
            yAux: Double,
            zAux: Double,
            random: RandomSource,
        ): Particle {
            return DirectionalAttackSweepParticle(
                clientWorld,
                x, y, z,
                effect.size,
                Vec3(effect.xDir, effect.yDir, effect.zDir),
                sprites,
            )
        }
    }
}