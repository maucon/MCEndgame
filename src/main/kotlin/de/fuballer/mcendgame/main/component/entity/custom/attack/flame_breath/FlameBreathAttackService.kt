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
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

private val HORIZONTAL_VECTOR = Vec3(1.0, 0.0, 1.0)
private val NO_AD_ATTRIBUTE = CustomAttribute(CustomAttributeTypes.NO_ATTACK_DAMAGE)

@Injectable
class FlameBreathAttackService(
    private val scheduler: Scheduler,
) {
    @EventSubscriber(sync = true)
    fun on(event: FlameBreathAttackEvent) {
        val attacker = event.attacker
        val world = event.attacker.level() as? ServerLevel ?: return

        val direction = getDirection(attacker, event.target)
        val horizontalDirection = direction.multiply(HORIZONTAL_VECTOR).normalize()
        val horizontalOffset = horizontalDirection.scale(attacker.bbWidth * event.entityWidthOffsetFactor)
        val verticalOffset = Vec3(0.0, attacker.bbHeight * event.entityHeightOffsetFactor, 0.0)
        val originPoint = attacker.position().add(horizontalOffset).add(verticalOffset)

        createParticles(world, attacker, originPoint, horizontalDirection, event.delay, event.duration, event.angle)
        playSound(world, originPoint, event.delay, event.duration)
        dealDamage(world, attacker, event.damageConversion, originPoint, horizontalDirection, event.delay, event.duration, event.angle)
    }

    private fun getDirection(
        attacker: Entity,
        target: Entity?,
    ): Vec3 {
        if (target == null) return attacker.lookAngle.multiply(HORIZONTAL_VECTOR).normalize()
        return target.position().subtract(attacker.position()).multiply(HORIZONTAL_VECTOR).normalize()
    }

    private fun createParticles(
        world: ServerLevel,
        attacker: Entity,
        originPoint: Vec3,
        direction: Vec3,
        delay: Int,
        duration: Int,
        angle: Double,
    ) {
        scheduler.repeatingForDuration(delay, 1, duration) {
            if (!attacker.isAlive) return@repeatingForDuration
            world.sendParticles(
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
        world: ServerLevel,
        originPoint: Vec3,
        delay: Int,
        duration: Int,
    ) {
        scheduler.repeatingForDuration(max(0, delay - 14), 5, max(1, duration - 10)) {
            world.playSound(
                null,
                originPoint.x,
                originPoint.y,
                originPoint.z,
                SoundEvents.BREEZE_IDLE_GROUND,
                SoundSource.HOSTILE,
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
                SoundEvents.BLAZE_BURN,
                SoundSource.HOSTILE,
                0.4F + 0.1F * Random.nextFloat(),
                0.8F + 0.3F * Random.nextFloat()
            )
        }
    }

    private fun dealDamage(
        world: ServerLevel,
        attacker: Entity,
        damageConversion: Double,
        originPoint: Vec3,
        direction: Vec3,
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

            val box = AABB(
                originPoint.x - maxDistance,
                originPoint.y - 1.0,
                originPoint.z - maxDistance,
                originPoint.x + maxDistance,
                originPoint.y + 1.0,
                originPoint.z + maxDistance
            )

            val entities = world.getEntitiesOfClass(LivingEntity::class.java, box) {
                val squaredDistance = it.distanceToSqr(originPoint)
                it != attacker && squaredDistance <= maxDistanceSquared && squaredDistance >= minDistanceSquared
            }

            val attackDamage = if (attacker is LivingEntity) attacker.getAttributeValue(Attributes.ATTACK_DAMAGE) else 1.0
            val spellDamage = attackDamage * damageConversion
            for (entity in entities) {
                val directionVectorToEntity = entity.position().subtract(originPoint).multiply(HORIZONTAL_VECTOR).normalize()

                val dotProduct = direction.dot(directionVectorToEntity)
                if (dotProduct >= cosThreshold) {
                    entity.dealDamage(
                        listOf(
                            NO_AD_ATTRIBUTE,
                            CustomAttribute(CustomAttributeTypes.SPELL_DAMAGE, roll = DoubleRoll(DoubleBounds(spellDamage))),
                        ),
                        CustomDamageTypes.SPELL,
                        attacker,
                    )
                    entity.igniteForTicks(80)
                }
            }
        }
    }
}