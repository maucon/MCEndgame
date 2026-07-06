package de.fuballer.mcendgame.main.component.entity.custom.attack.flame_breath

import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.damage.custom_type.CustomDamageTypes
import de.fuballer.mcendgame.main.component.damage.dealing.DamageDealingExtension.dealDamage
import de.fuballer.mcendgame.main.component.particle.HorizontalFlameBreathParticleEffect
import de.fuballer.mcendgame.main.functional.scheduler.Scheduler
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

private val HORIZONTAL_VECTOR = Vec3d(1.0, 0.0, 1.0)
private val NO_AD_ATTRIBUTE = CustomAttribute(CustomAttributeTypes.NO_ATTACK_DAMAGE)

@Injectable
class FlameBreathAttackService(
    private val scheduler: Scheduler,
) {
    @EventSubscriber(sync = true)
    fun on(event: FlameBreathAttackEvent) {
        val attacker = event.attacker
        val world = event.attacker.entityWorld as? ServerWorld ?: return

        val direction = getDirection(attacker, event.target)
        val horizontalDirection = direction.multiply(HORIZONTAL_VECTOR).normalize()
        val horizontalOffset = horizontalDirection.multiply(attacker.width * event.entityWidthOffsetFactor)
        val verticalOffset = Vec3d(0.0, attacker.height * event.entityHeightOffsetFactor, 0.0)
        val originPoint = attacker.pos.add(horizontalOffset).add(verticalOffset)

        createParticles(world, attacker, originPoint, horizontalDirection, event.delay, event.duration, event.angle)
        playSound(world, originPoint, event.delay, event.duration)
        dealDamage(world, attacker, event.damageConversion, originPoint, horizontalDirection, event.delay, event.duration, event.angle)
    }

    private fun getDirection(
        attacker: Entity,
        target: Entity?,
    ): Vec3d {
        if (target == null) return attacker.rotationVector.multiply(HORIZONTAL_VECTOR).normalize()
        return target.pos.subtract(attacker.pos).multiply(HORIZONTAL_VECTOR).normalize()
    }

    private fun createParticles(
        world: ServerWorld,
        attacker: Entity,
        originPoint: Vec3d,
        direction: Vec3d,
        delay: Int,
        duration: Int,
        angle: Double,
    ) {
        scheduler.repeatingForDuration(delay, 1, duration) {
            if (!attacker.isAlive) return@repeatingForDuration
            world.spawnParticles(
                HorizontalFlameBreathParticleEffect(direction.x, direction.y, direction.z, angle),
                originPoint.x,
                originPoint.y,
                originPoint.z,
                4,
                0.0,
                0.0,
                0.0,
                1.0,
            )
        }
    }

    private fun playSound(
        world: ServerWorld,
        originPoint: Vec3d,
        delay: Int,
        duration: Int,
    ) {
        scheduler.repeatingForDuration(max(0, delay - 14), 5, max(1, duration - 10)) {
            world.playSound(
                null,
                originPoint.x,
                originPoint.y,
                originPoint.z,
                SoundEvents.ENTITY_BREEZE_IDLE_GROUND,
                SoundCategory.HOSTILE,
                0.4F + 0.1F * Random.nextFloat(),
                0.2F + 0.2F * Random.nextFloat()
            )
        }
        scheduler.repeatingForDuration(delay, 3, duration) {
            world.playSound(
                null,
                originPoint.x,
                originPoint.y,
                originPoint.z,
                SoundEvents.ENTITY_BLAZE_BURN,
                SoundCategory.HOSTILE,
                0.4F + 0.1F * Random.nextFloat(),
                0.8F + 0.3F * Random.nextFloat()
            )
        }
    }

    private fun dealDamage(
        world: ServerWorld,
        attacker: Entity,
        damageConversion: Double,
        originPoint: Vec3d,
        direction: Vec3d,
        delay: Int,
        duration: Int,
        angle: Double,
    ) {
        val speed = 0.25

        val halfAngleRad = Math.toRadians(angle / 2.0)
        val cosThreshold = cos(halfAngleRad)

        scheduler.repeatingForDuration(delay, 2, duration + 40) { tick ->
            if (tick == 0) return@repeatingForDuration

            val maxDistance = speed * min(tick, 40) // 40 = particle max age
            val maxDistanceSquared = maxDistance * maxDistance

            var ticksSinceStop = if (tick > duration) (tick - duration) else 0
            if (!attacker.isAlive && attacker is LivingEntity) {
                ticksSinceStop = max(attacker.deathTime, ticksSinceStop)
            }
            val minDistance = speed * ticksSinceStop
            val minDistanceSquared = minDistance * minDistance

            val box = Box(
                originPoint.x - maxDistance,
                originPoint.y - 1.0,
                originPoint.z - maxDistance,
                originPoint.x + maxDistance,
                originPoint.y + 1.0,
                originPoint.z + maxDistance
            )

            val entities = world.getEntitiesByClass(LivingEntity::class.java, box) {
                val squaredDistance = it.squaredDistanceTo(originPoint)
                it != attacker && squaredDistance <= maxDistanceSquared && squaredDistance >= minDistanceSquared
            }

            val attackDamage = if (attacker is LivingEntity) attacker.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) else 1.0
            val elementalDamage = attackDamage * damageConversion
            for (entity in entities) {
                val directionVectorToEntity = entity.pos.subtract(originPoint).multiply(HORIZONTAL_VECTOR).normalize()

                val dotProduct = direction.dotProduct(directionVectorToEntity)
                if (dotProduct >= cosThreshold) {
                    entity.dealDamage(
                        attacker,
                        listOf(
                            NO_AD_ATTRIBUTE,
                            CustomAttribute(CustomAttributeTypes.ELEMENTAL_DAMAGE, roll = DoubleRoll(DoubleBounds(elementalDamage))),
                        ),
                        CustomDamageTypes.SPELL
                    )
                    entity.setOnFireForTicks(80)
                }
            }
        }
    }
}