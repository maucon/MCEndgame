package de.fuballer.mcendgame.main.component.entity.custom.attack.trigger_condition

import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob

class NoEntityNearTargetTriggerCondition(
    private val type: EntityType<*>,
    private val distance: Double,
) : TriggerCondition() {
    private val squaredDistance = distance * distance

    override fun doesTrigger(attacker: Mob, target: LivingEntity?): Boolean {
        if (target == null) return false

        val nearbyEntities = target.level().getEntitiesOfClass(
            Entity::class.java,
            target.boundingBox.inflate(distance)
        ) { it.type == type && it.distanceToSqr(target) < squaredDistance }

        return nearbyEntities.isEmpty()
    }
}