package de.fuballer.mcendgame.main.component.custom_attribute.effects.burn_enemy_on_walk

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asDoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asIntRoll
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getAllCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.damage.dealing.DamageDealingExtension.dealElementalSpellDamage
import de.fuballer.mcendgame.main.component.particle.MoveToTargetFlameParticleEffect
import de.fuballer.mcendgame.main.functional.scheduler.Scheduler
import de.fuballer.mcendgame.main.util.extension.EntityExtension.isEnemy
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.initializer.Initializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.minecraft.entity.LivingEntity
import net.minecraft.registry.tag.FluidTags
import net.minecraft.server.world.ServerWorld
import java.util.*
import kotlin.math.max

@Injectable
class BurnEnemyOnWalkService(
    private val scheduler: Scheduler,
) {
    private val walkData: MutableMap<UUID, DistanceWalkedData> = mutableMapOf()

    @Initializer
    fun register() {
        ServerTickEvents.END_WORLD_TICK.register { world ->
            updateEntities(world)

            val time = world.time
            if (time % 20 == 0L) cleanOldData(time)
        }
    }

    private fun updateEntities(world: ServerWorld) {
        world.iterateEntities().forEach { entity ->
            if (entity !is LivingEntity) return@forEach
            val attributes = entity.getAllCustomAttributes()[CustomAttributeTypes.BURN_ENEMY_ON_WALK] ?: return@forEach
            updateAttributes(entity, world, attributes)
        }
    }

    private fun updateAttributes(
        entity: LivingEntity,
        world: ServerWorld,
        attributes: List<CustomAttribute>,
    ) {
        attributes.forEach { updateAttribute(entity, world, it) }
    }

    private fun updateAttribute(
        entity: LivingEntity,
        world: ServerWorld,
        attribute: CustomAttribute,
    ) {
        val currentPos = entity.entityPos
        val currentTime = world.time

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

        if (!entity.isOnGround) return
        if (entity.hasVehicle()) return
        if (entity.isSwimming) return
        if (entity.isGliding) return
        if (entity.isSubmergedIn(FluidTags.WATER)) return
        if (entity.isTouchingWater) return
        if (entity.isClimbing) return

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
        world: ServerWorld,
        range: Int,
        elementalPercent: Double,
    ) {
        val enemiesInRange = world.getEntitiesByClass(
            LivingEntity::class.java,
            entity.boundingBox.expand(range.toDouble())
        ) { it != entity && it.isAlive && it.isEnemy(entity) }

        val target = enemiesInRange.randomOrNull() ?: return
        val distance = target.distanceTo(entity)
        val sparkTravelTime = BurnEnemyOnWalkSettings.getSparkTravelTime(distance)

        world.spawnParticles(
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
            if (target.isDead) return@delayed
            target.setOnFireFor(BurnEnemyOnWalkSettings.BURN_DURATION)
            target.dealElementalSpellDamage(elementalPercent, entity)
        }
    }

    private fun cleanOldData(currentTime: Long) {
        walkData.entries.removeIf {
            currentTime - it.value.lastUpdatedWorldTime > 200
        }
        println(walkData.size)
    }
}