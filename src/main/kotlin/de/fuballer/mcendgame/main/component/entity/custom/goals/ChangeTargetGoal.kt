package de.fuballer.mcendgame.main.component.entity.custom.goals

import com.google.common.base.Predicate
import de.fuballer.mcendgame.main.util.random.RandomOption
import de.fuballer.mcendgame.main.util.random.RandomUtil
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.mob.MobEntity
import kotlin.math.max
import kotlin.random.Random

class ChangeTargetGoal<T : MobEntity>(
    private val entity: T,
    private val probability: Double,
    private val tryIntervalTicks: Int,
    private val cooldownAfterChange: Int,
    private val targetFilter: Predicate<Entity>,
    private val range: Double = entity.getAttributeValue(EntityAttributes.GENERIC_FOLLOW_RANGE),
) : DisableAbleGoal() {
    private var timer = 0
    private var cooldown = 0

    override fun canStart(): Boolean {
        if (entity.target == null) return false
        return super.canStart()
    }

    override fun start() {
        cooldown = getTickCount(cooldownAfterChange)
        timer = getTickCount(tryIntervalTicks)
    }

    override fun shouldContinue(): Boolean {
        if (entity.target == null) return false
        return super.shouldContinue()
    }

    override fun tick() {
        if (cooldown > 0) {
            cooldown--
            return
        }

        if (--timer > 0) return
        timer = getTickCount(tryIntervalTicks)

        if (Random.nextDouble() > probability) return
        changeTarget()
    }

    private fun changeTarget() {
        val targets = entity.entityWorld.getEntitiesByClass(LivingEntity::class.java, entity.boundingBox.expand(range), targetFilter)
        if (targets.isEmpty()) return

        val weightedOptions = targets.map { target ->
            val weight = max(1, (range - entity.distanceTo(target)).toInt())
            RandomOption(weight, target)
        }
        val chosen = RandomUtil.pickOne(weightedOptions).option
        entity.target = chosen

        cooldown = getTickCount(cooldownAfterChange)
    }
}