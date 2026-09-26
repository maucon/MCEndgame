package de.fuballer.mcendgame.main.component.entity.custom.goals

import net.minecraft.world.entity.EntityReference
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.goal.Goal
import net.minecraft.world.entity.ai.navigation.PathNavigation
import net.minecraft.world.level.Level
import net.minecraft.world.level.pathfinder.PathType
import java.util.*

class FollowPartnerGoal<T>(
    private val mob: T,
    private val speedModifier: Double,
    private val startDistance: Double,
    private val stopDistance: Double,
) : Goal() where T : PathfinderMob, T : FollowPartnerGoal.PairedMob {
    private var partner: LivingEntity? = null
    private var navigation: PathNavigation = mob.navigation
    private var timeToRecalcPath = 0
    private var oldWaterCost = 0f

    init {
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK))
    }

    override fun canUse(): Boolean {
        val partner: LivingEntity = mob.getPartner() ?: return false
        if (mob.distanceToSqr(partner) < startDistance * startDistance) return false

        this.partner = partner
        return true
    }

    override fun canContinueToUse(): Boolean {
        if (partner == null) return false
        if (navigation.isDone) return false
        return mob.distanceToSqr(partner!!) > stopDistance * stopDistance
    }

    override fun start() {
        timeToRecalcPath = 0
        oldWaterCost = mob.getPathfindingMalus(PathType.WATER)
        mob.setPathfindingMalus(PathType.WATER, 0.0f)
    }

    override fun stop() {
        partner = null
        navigation.stop()
        mob.setPathfindingMalus(PathType.WATER, oldWaterCost)
    }

    override fun tick() {
        mob.getLookControl().setLookAt(partner!!, 10.0f, mob.maxHeadXRot.toFloat())

        if (--timeToRecalcPath > 0) return

        timeToRecalcPath = adjustedTickDelay(10)
        navigation.moveTo(partner!!, speedModifier)
    }

    interface PairedMob {
        var partnerReference: EntityReference<LivingEntity>?

        fun getPartner() = EntityReference.getLivingEntity(partnerReference, getLevel())

        fun getLevel(): Level
    }
}