package de.fuballer.mcendgame.main.component.entity.custom.attack.fire_geysers

import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.damage.custom_type.CustomDamageTypes
import de.fuballer.mcendgame.main.component.damage.dealing.DamageDealingExtension.dealDamage
import de.fuballer.mcendgame.main.component.particle.CustomParticleTypes
import de.fuballer.mcendgame.main.functional.scheduler.Scheduler
import de.fuballer.mcendgame.main.util.extension.EntityExtension.setAndSyncVelocity
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.particle.BlockStateParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.world.World
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
        val world = event.attacker.entityWorld as? ServerWorld ?: return

        val positions = chosePositions(world, attacker.blockPos, event.radius, event.geyserProbability, event.geyserCountLimit)

        createParticles(world, positions, event.delay, event.indicatorDuration, event.pillarDuration)
        playSound(world, positions, event.delay, event.indicatorDuration, event.pillarDuration)
        dealDamage(world, positions, event.delay, event.indicatorDuration, event.pillarDuration, attacker, event.burstDamageConversion, event.durationDamageConversion)
    }

    private fun chosePositions(
        world: World,
        startPos: BlockPos,
        radius: Int,
        probability: Double,
        countLimit: Int
    ): List<BlockPos> {
        val possiblePositions = getPossiblePositions(world, startPos, radius)
        val count = min((possiblePositions.size * probability).toInt(), countLimit)
        return possiblePositions.shuffled().take(count)
    }

    private fun getPossiblePositions(
        world: World,
        startPos: BlockPos,
        radius: Int,
    ): List<BlockPos> {
        val squaredRadius = radius * radius

        val possiblePositions = mutableListOf<BlockPos>()

        var heads = listOf(startPos)
        val checkedPositions = mutableSetOf(startPos)
        while (heads.isNotEmpty()) {
            val newHeads = mutableListOf<BlockPos>()

            for (head in heads) {
                if (head.getSquaredDistance(startPos) > squaredRadius) continue

                val below = head.down()
                val belowState = world.getBlockState(below)
                if (belowState.isSolidBlock(world, below)) {
                    possiblePositions.add(head)
                }

                val neighbors = listOf(
                    head.north(),
                    head.south(),
                    head.east(),
                    head.west(),
                    head.up(),
                    head.down()
                )

                for (neighbor in neighbors) {
                    if (neighbor in checkedPositions) continue
                    checkedPositions.add(neighbor)

                    val state = world.getBlockState(neighbor)
                    if (!state.isSolidBlock(world, neighbor)) newHeads.add(neighbor)
                }
            }

            heads = newHeads
        }

        return possiblePositions
    }

    private fun createParticles(
        world: ServerWorld,
        positions: List<BlockPos>,
        delay: Int,
        indicatorDuration: Int,
        pillarDuration: Int,
    ) {
        scheduler.repeatingForDuration(delay, 1, indicatorDuration) {
            positions.forEach {
                val centerPos = it.toCenterPos().subtract(0.0, 0.2, 0.0)
                world.spawnParticles(
                    BlockStateParticleEffect(ParticleTypes.BLOCK, world.getBlockState(it.down())),
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
                val centerPos = it.toCenterPos().subtract(0.0, 0.2, 0.0)
                world.spawnParticles(
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
                val centerPos = it.toCenterPos().subtract(0.0, 0.2, 0.0)
                world.spawnParticles(
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
                val centerPos = it.toCenterPos().subtract(0.0, 0.2, 0.0)
                world.spawnParticles(
                    BlockStateParticleEffect(ParticleTypes.BLOCK, world.getBlockState(it.down())),
                    centerPos.x,
                    centerPos.y,
                    centerPos.z,
                    1,
                    0.1,
                    0.1,
                    0.1,
                    0.3
                )
                world.spawnParticles(
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
                world.spawnParticles(
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
        world: ServerWorld,
        positions: List<BlockPos>,
        delay: Int,
        indicatorDuration: Int,
        pillarDuration: Int,
    ) {
        scheduler.delayed(max(1, delay + indicatorDuration - 60)) {
            positions.forEach {
                if (Random.nextDouble() > 0.15) return@forEach
                world.playSound(null, it, SoundEvents.BLOCK_LAVA_AMBIENT, SoundCategory.HOSTILE, 0.5F, 0.8F + 0.3F * Random.nextFloat())
            }
        }

        scheduler.repeatingForDuration(delay, 2, indicatorDuration) { ticks ->
            val volume = max(0.2F, ticks / indicatorDuration.toFloat())
            positions.forEach {
                if (Random.nextDouble() > 0.15) return@forEach
                world.playSound(null, it, SoundEvents.BLOCK_STONE_HIT, SoundCategory.HOSTILE, volume, 0.5F)
            }
        }

        scheduler.delayed(delay + indicatorDuration) {
            positions.forEach {
                if (Random.nextDouble() < 0.5)
                    world.playSound(null, it, SoundEvents.ENTITY_GENERIC_EXPLODE.value(), SoundCategory.HOSTILE, 0.3F, 0.8F + 0.4F * Random.nextFloat())
                if (Random.nextDouble() < 0.3)
                    world.playSound(null, it, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.HOSTILE, 0.3F, 1F)
            }
        }

        scheduler.repeatingForDuration(delay + indicatorDuration, 2, pillarDuration) {
            positions.forEach {
                if (Random.nextDouble() > 0.15) return@forEach
                world.playSound(null, it, SoundEvents.ENTITY_BLAZE_BURN, SoundCategory.HOSTILE, 0.4F + 0.1F * Random.nextFloat(), 0.8F + 0.3F * Random.nextFloat())
            }
        }
    }

    private fun dealDamage(
        world: ServerWorld,
        positions: List<BlockPos>,
        delay: Int,
        indicatorDuration: Int,
        pillarDuration: Int,
        attacker: Entity,
        burstDamageConversion: Double,
        durationDamageConversion: Double,
    ) {
        val attackDamage = if (attacker is LivingEntity) attacker.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) else 1.0
        val burstElementalDamage = attackDamage * burstDamageConversion
        val durationDamage = attackDamage * durationDamageConversion

        scheduler.delayed(delay + indicatorDuration) {
            val targets = getTargets(world, positions, attacker)
            targets.forEach {
                it.dealDamage(
                    attacker,
                    listOf(
                        NO_AD_ATTRIBUTE,
                        CustomAttribute(CustomAttributeTypes.ELEMENTAL_DAMAGE, roll = DoubleRoll(DoubleBounds(burstElementalDamage))),
                    ),
                    CustomDamageTypes.SPELL
                )
                it.setOnFireForTicks(80)
                it.setAndSyncVelocity(it.velocity.add(0.0, 1.0, 0.0))
            }
        }

        scheduler.repeatingForDuration(delay + indicatorDuration, 2, pillarDuration) {
            val targets = getTargets(world, positions, attacker)
            targets.forEach {
                it.dealDamage(
                    attacker,
                    listOf(
                        NO_AD_ATTRIBUTE,
                        CustomAttribute(CustomAttributeTypes.ELEMENTAL_DAMAGE, roll = DoubleRoll(DoubleBounds(durationDamage))),
                    ),
                    CustomDamageTypes.SPELL
                )
                it.setOnFireForTicks(80)
            }
        }
    }

    private fun getTargets(
        world: ServerWorld,
        positions: List<BlockPos>,
        attacker: Entity,
    ): Set<LivingEntity> {
        val targets = mutableSetOf<LivingEntity>()

        positions.forEach {
            val box = Box(it.x - 0.2, it.y - 1.0, it.z - 0.2, it.x + 1.2, it.y + 4.0, it.z + 1.2)
            targets.addAll(world.getEntitiesByClass(LivingEntity::class.java, box) { target -> target != attacker })
        }

        return targets
    }
}