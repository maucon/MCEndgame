package de.fuballer.mcendgame.main.component.entity.custom.attack.debris_explosion

import de.fuballer.mcendgame.main.component.entity.custom.attack.damage.AreaAttackDamage
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.particle.ParticleData
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.sound.SoundData
import de.fuballer.mcendgame.main.component.entity.custom.entities.block_debris.BlockDebrisEntity
import de.fuballer.mcendgame.main.functional.scheduler.Scheduler
import de.fuballer.mcendgame.main.util.extension.Vec3Extension.rotateHorizontalVectorUpwards
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Mob
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3
import kotlin.math.ceil

@Injectable
class DebrisExplosionAttackService(
    private val scheduler: Scheduler,
) {
    @EventSubscriber(sync = true)
    fun on(event: DebrisExplosionAttackEvent) {
        val attacker = event.attacker
        val level = event.attacker.level() as? ServerLevel ?: return

        scheduler.delayed(event.delay) {
            mainExplosion(
                level,
                attacker,
                event.mainExplosionDamage,
                event.mainExplosionParticles,
                event.mainExplosionSound
            )
            createDebris(
                level,
                attacker,
                event.debrisExplosionDamage,
                event.debrisExplosionParticles,
                event.debrisExplosionSound,
                event.debrisCreateRadiusRange,
                event.debrisCreateProbabilityFromDistanceToOrigin,
                event.debrisVelocity,
            )
        }
    }

    private fun mainExplosion(
        level: ServerLevel,
        attacker: Mob,
        damage: AreaAttackDamage,
        particles: ParticleData,
        sound: SoundData,
    ) {
        damage.apply(level, attacker, null)
        particles.apply(level, attacker)
        sound.apply(level, attacker)
    }

    private fun createDebris(
        level: ServerLevel,
        attacker: Mob,
        debrisExplosionDamage: AreaAttackDamage,
        debrisExplosionParticles: ParticleData,
        debrisExplosionSound: SoundData,
        debrisCreateRadiusRange: Pair<Double, Double>,
        debrisCreateProbabilityFromDistanceToOrigin: (Double) -> Double,
        debrisVelocity: () -> Double,
    ) {
        val origin = attacker.position()
        val center = BlockPos.containing(origin.subtract(0.0, 0.1, 0.0))

        val random = level.random

        val (radiusMin, radiusMax) = debrisCreateRadiusRange
        val searchRadiusMinSquared = radiusMin * radiusMin
        val searchRadiusMaxSquared = radiusMax * radiusMax

        val searchRange = ceil(radiusMax).toInt()
        val selected = mutableListOf<Pair<Vec3, BlockState>>()
        for (x in -searchRange..searchRange) {
            for (z in -searchRange..searchRange) {
                if (x * x + z * z < searchRadiusMinSquared) continue
                if (x * x + z * z > searchRadiusMaxSquared) continue

                for (y in 2 downTo -2) {
                    val pos = center.offset(x, y, z)
                    val state = level.getBlockState(pos)

                    if (state.getCollisionShape(level, pos).isEmpty) continue

                    val above = level.getBlockState(pos.above())
                    if (!above.getCollisionShape(level, pos).isEmpty) break

                    val spawnPos = Vec3(pos.x + 0.5, pos.y + 1.0, pos.z + 0.5)
                    val distanceToOrigin = origin.distanceTo(spawnPos)
                    val probability = debrisCreateProbabilityFromDistanceToOrigin(distanceToOrigin)
                    if (random.nextDouble() < probability) selected.add(spawnPos to state)

                    break
                }
            }
        }

        for ((spawnPos, blockState) in selected) {
            val debris = BlockDebrisEntity(level, attacker, blockState, debrisExplosionDamage, debrisExplosionParticles, debrisExplosionSound)
            debris.setPos(spawnPos)

            val horizontalDirection = spawnPos.horizontal().subtract(origin.horizontal())
            val upwardsAngleDeg = 25 + 65 * (1 - spawnPos.distanceTo(origin) / radiusMax)
            val direction = horizontalDirection.rotateHorizontalVectorUpwards(upwardsAngleDeg)
                .yRot(Math.toRadians((random.nextDouble() * 2.0 - 1.0) * 10.0).toFloat())
                .normalize()
            val velocity = direction.scale(debrisVelocity())
            debris.deltaMovement = velocity

            level.addFreshEntity(debris)
        }
    }
}
