package de.fuballer.mcendgame.main.component.entity.custom.attack.trigger_condition

import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.isCompanion
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.TamableAnimal
import net.minecraft.world.entity.ai.targeting.TargetingConditions

class CompanionLimitTriggerCondition(
    private val companionLimit: (Int) -> Int,
    private val getTargetCount: (ServerLevel, Mob, LivingEntity?) -> Int,
    private val searchRange: Double,
) : TriggerCondition() {
    override fun doesTrigger(attacker: Mob, target: LivingEntity?): Boolean {
        val level = attacker.level() as? ServerLevel ?: return false
        val targetCount = getTargetCount(level, attacker, target)
        val companionLimit = companionLimit(targetCount)

        val companionCount = level.getNearbyEntities(
            TamableAnimal::class.java,
            TargetingConditions.forNonCombat().selector { entity, _ -> entity.isCompanion() && (entity as TamableAnimal).owner == attacker },
            attacker,
            attacker.boundingBox.inflate(searchRange),
        ).count()

        return companionLimit >= companionCount
    }
}