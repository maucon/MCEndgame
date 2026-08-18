package de.fuballer.mcendgame.main.component.entity.custom.entities.beastweaver.beastweaver_vine

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal

class BeastweaverVineNearestAttackableTargetGoal<T : LivingEntity>(
    mob: Mob,
    targetType: Class<T>,
) : NearestAttackableTargetGoal<T>(mob, targetType, 0, true, false, null) {
    override fun getFollowDistance(): Double {
        return super.getFollowDistance() * mob.getAttributeValue(Attributes.SCALE)
    }
}