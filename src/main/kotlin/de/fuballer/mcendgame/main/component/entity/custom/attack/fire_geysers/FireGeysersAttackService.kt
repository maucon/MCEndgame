package de.fuballer.mcendgame.main.component.entity.custom.attack.fire_geysers

import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.damage.custom_type.CustomDamageTypes
import de.fuballer.mcendgame.main.component.damage.dealing.DamageDealingExtension.dealDamage
import de.fuballer.mcendgame.main.component.particle.CustomParticleTypes
import de.fuballer.mcendgame.main.functional.scheduler.Scheduler
import de.fuballer.mcendgame.main.util.BlockPosUtil
import de.fuballer.mcendgame.main.util.extension.EntityExtension.setAndSyncVelocity
import de.fuballer.mcendgame.main.util.extension.Vec3iExtension.toCenter
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.BlockParticleOption
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

private val NO_AD_ATTRIBUTE = CustomAttribute(CustomAttributeTypes.NO_ATTACK_DAMAGE)

@Injectable
class FireGeysersAttackService(
    private val scheduler: Scheduler,
) {
    @EventSubscriber(sync = true)
    fun on(event: FireGeysersAttackEvent) {
        val attacker = event.attacker
        val world = event.attacker.level() as? ServerLevel ?: return

        val positions = chosePositions(world, attacker.blockPosition(), event.radius, event.geyserProbability, event.geyserCountLimit)

        createParticles(world, positions, event.delay, event.indicatorDuration, event.pillarDuration)
        playSound(world, positions, event.delay, event.indicatorDuration, event.pillarDuration)
        dealDamage(world, positions, event.delay, event.indicatorDuration, event.pillarDuration, attacker, event.burstDamageConversion, event.durationDamageConversion)
    }

    private fun chosePositions(
        world: Level,
        startPos: BlockPos,
        radius: Int,
        probability: Double,
        countLimit: Int
    ): List<BlockPos> {
        val possiblePositions = BlockPosUtil.findEmptyAboveSolid(world, startPos, radius)
        val count = min((possiblePositions.size * probability).toInt(), countLimit)
        return possiblePositions.shuffled().take(count)
    }

    private fun createParticles(
        world: ServerLevel,
        positions: List<BlockPos>,
        delay: Int,
        indicatorDuration: Int,
        pillarDuration: Int,
    ) {
        scheduler.repeatingForDuration(delay, 1, indicatorDuration) {
            positions.forEach {
                val centerPos = it.toCenter().subtract(0.0, 0.2, 0.0)
                world.sendParticles(
                    BlockParticleOption(ParticleTypes.BLOCK, world.getBlockState(it.below())),
                    centerPos.x,
                    centerPos.y,
                    centerPos.z,
                    1,
                    0.1,
                    0.1,
                    0.1,
                    0.3
                )
            }
        }

        val halfIndicatorDuration = indicatorDuration / 2
        scheduler.repeatingForDuration(delay + halfIndicatorDuration, 4, halfIndicatorDuration + pillarDuration) {
            positions.forEach {
                val centerPos = it.toCenter().subtract(0.0, 0.2, 0.0)
                world.sendParticles(
                    ParticleTypes.LAVA,
                    centerPos.x,
                    centerPos.y,
                    centerPos.z,
                    1,
                    0.1,
                    0.1,
                    0.1,
                    0.1
                )
            }
        }

        scheduler.delayed(delay + indicatorDuration) {
            positions.forEach {
                val centerPos = it.toCenter().subtract(0.0, 0.2, 0.0)
                world.sendParticles(
                    ParticleTypes.FLAME,
                    centerPos.x,
                    centerPos.y,
                    centerPos.z,
                    7,
                    0.1,
                    0.1,
                    0.1,
                    0.2
                )
            }
        }

        scheduler.repeatingForDuration(delay + indicatorDuration, 2, pillarDuration) {
            positions.forEach {
                val centerPos = it.toCenter().subtract(0.0, 0.2, 0.0)
                world.sendParticles(
                    BlockParticleOption(ParticleTypes.BLOCK, world.getBlockState(it.below())),
                    centerPos.x,
                    centerPos.y,
                    centerPos.z,
                    1,
                    0.1,
                    0.1,
                    0.1,
                    0.3
                )
                world.sendParticles(
                    CustomParticleTypes.FLAME_PILLAR,
                    centerPos.x,
                    centerPos.y,
                    centerPos.z,
                    1,
                    0.0,
                    0.0,
                    0.0,
                    0.0
                )
                world.sendParticles(
                    CustomParticleTypes.SMOKE_PILLAR,
                    centerPos.x,
                    centerPos.y,
                    centerPos.z,
                    2,
                    0.0,
                    0.0,
                    0.0,
                    0.0
                )
            }
        }
    }

    private fun playSound(
        world: ServerLevel,
        positions: List<BlockPos>,
        delay: Int,
        indicatorDuration: Int,
        pillarDuration: Int,
    ) {
        scheduler.delayed(max(1, delay + indicatorDuration - 60)) {
            positions.forEach {
                if (Random.nextDouble() > 0.15) return@forEach
                world.playSound(null, it, SoundEvents.LAVA_AMBIENT, SoundSource.HOSTILE, 0.5F, 0.8F + 0.3F * Random.nextFloat())
            }
        }

        scheduler.repeatingForDuration(delay, 2, indicatorDuration) { ticks ->
            val volume = max(0.2F, ticks / indicatorDuration.toFloat())
            positions.forEach {
                if (Random.nextDouble() > 0.15) return@forEach
                world.playSound(null, it, SoundEvents.STONE_HIT, SoundSource.HOSTILE, volume, 0.5F)
            }
        }

        scheduler.delayed(delay + indicatorDuration) {
            positions.forEach {
                if (Random.nextDouble() < 0.5)
                    world.playSound(null, it, SoundEvents.GENERIC_EXPLODE.value(), SoundSource.HOSTILE, 0.3F, 0.8F + 0.4F * Random.nextFloat())
                if (Random.nextDouble() < 0.3)
                    world.playSound(null, it, SoundEvents.FIRECHARGE_USE, SoundSource.HOSTILE, 0.3F, 1F)
            }
        }

        scheduler.repeatingForDuration(delay + indicatorDuration, 2, pillarDuration) {
            positions.forEach {
                if (Random.nextDouble() > 0.15) return@forEach
                world.playSound(null, it, SoundEvents.BLAZE_BURN, SoundSource.HOSTILE, 0.4F + 0.1F * Random.nextFloat(), 0.8F + 0.3F * Random.nextFloat())
            }
        }
    }

    private fun dealDamage(
        world: ServerLevel,
        positions: List<BlockPos>,
        delay: Int,
        indicatorDuration: Int,
        pillarDuration: Int,
        attacker: Entity,
        burstDamageConversion: Double,
        durationDamageConversion: Double,
    ) {
        val attackDamage = if (attacker is LivingEntity) attacker.getAttributeValue(Attributes.ATTACK_DAMAGE) else 1.0
        val burstSpellDamage = attackDamage * burstDamageConversion
        val durationDamage = attackDamage * durationDamageConversion

        scheduler.delayed(delay + indicatorDuration) {
            val targets = getTargets(world, positions, attacker)
            targets.forEach {
                it.dealDamage(
                    listOf(
                        NO_AD_ATTRIBUTE,
                        CustomAttribute(CustomAttributeTypes.SPELL_DAMAGE, roll = DoubleRoll(DoubleBounds(burstSpellDamage))),
                    ),
                    CustomDamageTypes.SPELL,
                    attacker,
                )
                it.igniteForTicks(80)
                it.setAndSyncVelocity(it.deltaMovement.add(0.0, 1.0, 0.0))
            }
        }

        scheduler.repeatingForDuration(delay + indicatorDuration, 2, pillarDuration) {
            val targets = getTargets(world, positions, attacker)
            targets.forEach {
                it.dealDamage(
                    listOf(
                        NO_AD_ATTRIBUTE,
                        CustomAttribute(CustomAttributeTypes.SPELL_DAMAGE, roll = DoubleRoll(DoubleBounds(durationDamage))),
                    ),
                    CustomDamageTypes.SPELL,
                    attacker,
                )
                it.igniteForTicks(80)
            }
        }
    }

    private fun getTargets(
        world: ServerLevel,
        positions: List<BlockPos>,
        attacker: Entity,
    ): Set<LivingEntity> {
        val targets = mutableSetOf<LivingEntity>()

        positions.forEach {
            val box = AABB(it.x - 0.2, it.y - 1.0, it.z - 0.2, it.x + 1.2, it.y + 4.0, it.z + 1.2)
            targets.addAll(world.getEntitiesOfClass(LivingEntity::class.java, box) { target -> target != attacker })
        }

        return targets
    }
}