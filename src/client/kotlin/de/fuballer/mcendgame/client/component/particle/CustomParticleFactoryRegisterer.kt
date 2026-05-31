package de.fuballer.mcendgame.client.component.particle

import de.fuballer.mcendgame.main.component.particle.CustomParticleTypes
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.initializer.Initializer
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry

@Injectable
class CustomParticleFactoryRegisterer {
    @Initializer
    fun init() {
        ParticleFactoryRegistry.getInstance().register(CustomParticleTypes.FLAME_PILLAR, FlamePillarParticle::Factory)
        ParticleFactoryRegistry.getInstance().register(CustomParticleTypes.SMOKE_PILLAR, SmokePillarParticle::Factory)
        ParticleFactoryRegistry.getInstance().register(CustomParticleTypes.HORIZONTAL_FLAME_BREATH, HorizontalFlameBreathParticle::Factory)
        ParticleFactoryRegistry.getInstance().register(CustomParticleTypes.MOVE_TO_TARGET_FLAME, MoveToTargetFlameParticle::Factory)
    }
}