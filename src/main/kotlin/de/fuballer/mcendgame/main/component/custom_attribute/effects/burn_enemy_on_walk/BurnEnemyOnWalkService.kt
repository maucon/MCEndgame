package de.fuballer.mcendgame.main.component.custom_attribute.effects.burn_enemy_on_walk

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asDoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asIntRoll
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getAllCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.damage.dealing.DamageDealingExtension.dealSpellDamage
import de.fuballer.mcendgame.main.component.particle.MoveToTargetFlameParticleEffect
import de.fuballer.mcendgame.main.functional.scheduler.Scheduler
import de.fuballer.mcendgame.main.util.extension.EntityExtension.isEnemy
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.initializer.Initializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.FluidTags
import net.minecraft.world.entity.LivingEntity
import java.util.*
import kotlin.math.max

@Injectable
class BurnEnemyOnWalkService(
    private val scheduler: Scheduler,
) {
    private val walkData: MutableMap<UUID, DistanceWalkedData> = mutableMapOf()

    @Initializer
    fun register() {
        ServerTickEvents.END_LEVEL_TICK.register { world ->
            updateEntities(world)

            val time = world.gameTime
            if (time % 20 == 0L) cleanOldData(time)
        }
    }

    private fun updateEntities(world: ServerLevel) {
        world.allEntities.forEach { entity ->
            if (entity !is LivingEntity) return@forEach
            val attributes = entity.getAllCustomAttributes()[CustomAttributeTypes.BURN_ENEMY_ON_WALK] ?: return@forEach
            updateAttributes(entity, world, attributes)
        }
    }

    private fun updateAttributes(
        entity: LivingEntity,
        world: ServerLevel,
        attributes: List<CustomAttribute>,
    ) {
        attributes.forEach { updateAttribute(entity, world, it) }
    }

    private fun updateAttribute(
        entity: LivingEntity,
        world: ServerLevel,
        attribute: CustomAttribute,
    ) {
        val currentPos = entity.position()
        val currentTime = world.gameTime

        val id = attribute.id
        if (!walkData.contains(id)) {
            walkData[id] = DistanceWalkedData(0.0, currentPos, currentTime)
            return
        }
        val data = walkData[id]!!

        data.lastUpdatedWorldTime = currentTime

        val movedDistance = currentPos.subtract(data.previousPos).length()
        data.previousPos = currentPos
        if (movedDistance <= 0.0) return
        if (movedDistance > 5.0) return // ignore teleports

        if (!entity.onGround()) return
        if (entity.isPassenger) return
        if (entity.isSwimming) return
        if (entity.isFallFlying) return
        if (entity.isEyeInFluid(FluidTags.WATER)) return
        if (entity.isInWater) return
        if (entity.onClimbable()) return

        val distanceSum = data.distance + movedDistance
        var distanceTrigger = attribute.rolls[0].asDoubleRoll().getValue()
        distanceTrigger = max(distanceTrigger, 0.01) // infinite trigger & mod 0 protection
        val triggerTimes = (distanceSum / distanceTrigger).toInt()
        data.distance = distanceSum % distanceTrigger
        if (triggerTimes == 0) return

        val range = attribute.rolls[1].asIntRoll().getValue()
        val damagePercent = attribute.rolls[2].asDoubleRoll().getValue()
        repeat(triggerTimes) { burnEnemy(entity, world, range, damagePercent) }
    }

    private fun burnEnemy(
        entity: LivingEntity,
        world: ServerLevel,
        range: Int,
        spellPercent: Double,
    ) {
        val enemiesInRange = world.getEntitiesOfClass(
            LivingEntity::class.java,
            entity.boundingBox.inflate(range.toDouble())
        ) { it != entity && it.isAlive && it.isEnemy(entity) }

        val target = enemiesInRange.randomOrNull() ?: return
        val distance = target.distanceTo(entity)
        val sparkTravelTime = BurnEnemyOnWalkSettings.getSparkTravelTime(distance)

        world.sendParticles(
            MoveToTargetFlameParticleEffect(target.uuid, sparkTravelTime),
            entity.x,
            entity.y,
            entity.z,
            1,
            0.0,
            0.0,
            0.0,
            1.0,
        )

        scheduler.delayed(sparkTravelTime) {
            if (target.isDeadOrDying) return@delayed
            if (!target.dealSpellDamage(spellPercent, entity)) return@delayed
            target.igniteForSeconds(BurnEnemyOnWalkSettings.BURN_DURATION)
        }
    }

    private fun cleanOldData(currentTime: Long) {
        walkData.entries.removeIf {
            currentTime - it.value.lastUpdatedWorldTime > 200
        }
    }
}