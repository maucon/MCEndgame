package de.fuballer.mcendgame.main.component.entity.custom.goals

import com.google.common.base.Predicate
import de.fuballer.mcendgame.main.util.random.RandomOption
import de.fuballer.mcendgame.main.util.random.RandomUtil
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.ai.attributes.Attributes
import kotlin.math.max
import kotlin.random.Random

class ChangeTargetGoal<T : Mob>(
    private val entity: T,
    private val probability: Double,
    private val tryIntervalTicks: Int,
    private val cooldownAfterChange: Int,
    private val targetFilter: Predicate<Entity>,
    private val range: Double = entity.getAttributeValue(Attributes.FOLLOW_RANGE),
) : DisableAbleGoal() {
    private var timer = 0
    private var cooldown = 0

    override fun canUse(): Boolean {
        if (entity.target == null) return false
        return super.canUse()
    }

    override fun start() {
        cooldown = adjustedTickDelay(cooldownAfterChange)
        timer = adjustedTickDelay(tryIntervalTicks)
    }

    override fun canContinueToUse(): Boolean {
        if (entity.target == null) return false
        return super.canContinueToUse()
    }

    override fun tick() {
        if (cooldown > 0) {
            cooldown--
            return
        }

        if (--timer > 0) return
        timer = adjustedTickDelay(tryIntervalTicks)

        if (Random.nextDouble() > probability) return
        changeTarget()
    }

    private fun changeTarget() {
        val targets = entity.level().getEntitiesOfClass(LivingEntity::class.java, entity.boundingBox.inflate(range), targetFilter)
        if (targets.isEmpty()) return

        val weightedOptions = targets.map { target ->
            val weight = max(1, (range - entity.distanceTo(target)).toInt())
            RandomOption(weight, target)
        }
        val chosen = RandomUtil.pickOne(weightedOptions).option
        entity.target = chosen

        cooldown = adjustedTickDelay(cooldownAfterChange)
    }
}