package de.fuballer.mcendgame.main.component.entity.custom.attack.debris_explosion

import com.geckolib.animatable.GeoEntity
import de.fuballer.mcendgame.main.component.entity.custom.attack.Attack
import de.fuballer.mcendgame.main.component.entity.custom.attack.damage.AreaAttackDamage
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.AttackAnimationData
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.DelayedAttackData
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.particle.ParticleData
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.sound.SoundData
import de.fuballer.mcendgame.main.component.entity.custom.attack.trigger_condition.TriggerCondition
import de.maucon.mauconframework.event.EventGateway
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob

class DebrisExplosionAttack<T>(
    animationData: AttackAnimationData,
    totalDuration: Int,
    cooldown: Int,
    trigger: TriggerCondition,
    data: List<DelayedAttackData>,
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
    blockMovementDuration: Int = 0,
) : Attack<T>(animationData, totalDuration, cooldown, trigger, data, blockMovementDuration) where T : Mob, T : GeoEntity {

    override fun start(attacker: T, target: LivingEntity?) {
        super.start(attacker, target)

        val event = DebrisExplosionAttackEvent(
            attacker,
            delay,
            mainExplosionDamage,
            mainExplosionParticles,
            mainExplosionSound,
            debrisExplosionDamage,
            debrisExplosionParticles,
            debrisExplosionSound,
            debrisCreateRadiusRange,
            debrisCreateProbabilityFromDistanceToOrigin,
            debrisVelocity,
        )
        EventGateway.publish(event)
    }
}