package de.fuballer.mcendgame.client.component.particle

import de.fuballer.mcendgame.main.component.particle.CustomParticleTypes
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.initializer.Initializer
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry

@Injectable
class CustomParticleFactoryRegisterer {
    @Initializer
    fun init() {
        ParticleProviderRegistry.getInstance().register(CustomParticleTypes.FLAME_PILLAR, FlamePillarParticle::Factory)
        ParticleProviderRegistry.getInstance().register(CustomParticleTypes.SMOKE_PILLAR, SmokePillarParticle::Factory)
        ParticleProviderRegistry.getInstance().register(CustomParticleTypes.HORIZONTAL_FLAME_BREATH, HorizontalFlameBreathParticle::Factory)
        ParticleProviderRegistry.getInstance().register(CustomParticleTypes.MOVE_TO_TARGET_FLAME, MoveToTargetFlameParticle::Factory)
        ParticleProviderRegistry.getInstance().register(CustomParticleTypes.DIRECTIONAL_SWEEP_ATTACK, DirectionalAttackSweepParticle::Factory)
    }
}