package de.fuballer.mcendgame.main.component.entity.custom.attack.debris_explosion

import de.fuballer.mcendgame.main.component.entity.custom.attack.damage.AreaAttackDamage
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.ParticleData
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.SoundData
import net.minecraft.world.entity.Mob

data class DebrisExplosionAttackEvent(
    val attacker: Mob,
    val delay: Int,
    val mainExplosionDamage: AreaAttackDamage,
    val mainExplosionParticles: ParticleData,
    val mainExplosionSound: SoundData,
    val debrisExplosionDamage: AreaAttackDamage,
    val debrisExplosionParticles: ParticleData,
    val debrisExplosionSound: SoundData,
    val debrisCreateRadiusRange: Pair<Double, Double>,
    val debrisCreateProbabilityFromDistanceToOrigin: (Double) -> Double,
    val debrisVelocity: () -> Double,
)