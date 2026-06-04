package de.fuballer.mcendgame.main.component.entity.custom.entities.spiderling

import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.getHitbox
import de.fuballer.mcendgame.main.util.extension.mixin.GoalMixinExtension.getTargetX
import de.fuballer.mcendgame.main.util.extension.mixin.GoalMixinExtension.getTargetY
import de.fuballer.mcendgame.main.util.extension.mixin.GoalMixinExtension.getTargetZ
import de.fuballer.mcendgame.main.util.extension.mixin.GoalMixinExtension.getUpdateCountdownTicks
import de.fuballer.mcendgame.main.util.extension.mixin.GoalMixinExtension.setCooldown
import de.fuballer.mcendgame.main.util.extension.mixin.GoalMixinExtension.setTargetX
import de.fuballer.mcendgame.main.util.extension.mixin.GoalMixinExtension.setTargetY
import de.fuballer.mcendgame.main.util.extension.mixin.GoalMixinExtension.setTargetZ
import de.fuballer.mcendgame.main.util.extension.mixin.GoalMixinExtension.setUpdateCountdownTicks
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponents
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.*
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.*
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import kotlin.math.max

class SpiderlingEntity(
    type: EntityType<out SpiderlingEntity>,
    world: Level,
) : TamableAnimal(type, world) {
    companion object {
        private const val MAX_ATTACK_RANGE = 2.0

        fun createAttributes(): AttributeSupplier.Builder {
            return createLivingAttributes()
                .add(Attributes.FOLLOW_RANGE, 24.0)
                .add(Attributes.MOVEMENT_SPEED, 0.4)
                .add(Attributes.ATTACK_DAMAGE, 3.0)
                .add(Attributes.ATTACK_KNOCKBACK, 0.3)
                .add(Attributes.ARMOR, 0.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8)
                .add(Attributes.MOVEMENT_EFFICIENCY, 0.85)
                .add(Attributes.SAFE_FALL_DISTANCE, 10.0)
                .add(Attributes.FALL_DAMAGE_MULTIPLIER, 0.1)
                .add(Attributes.SCALE, 0.5)
        }

        class AttackGoal(
            mob: SpiderlingEntity,
            val speed: Double,
            pauseWhenMobIdle: Boolean,
        ) : MeleeAttackGoal(mob, speed, pauseWhenMobIdle) {
            // copied from MeleeAttackGoal but replaced canSee with true
            override fun tick() {
                val livingEntity = mob.target ?: return
                mob.getLookControl().setLookAt(livingEntity, 30.0f, 30.0f)

                var updateCountdownTicks = getUpdateCountdownTicks()
                val targetX = getTargetX()
                val targetY = getTargetY()
                val targetZ = getTargetZ()

                updateCountdownTicks = (max(updateCountdownTicks - 1, 0))

                if (updateCountdownTicks <= 0
                    && (targetX == 0.0 && targetY == 0.0 && targetZ == 0.0
                            || livingEntity.distanceToSqr(targetX, targetY, targetZ) >= 1.0
                            || mob.getRandom().nextFloat() < 0.05f)
                ) {
                    setTargetX(livingEntity.x)
                    setTargetY(livingEntity.y)
                    setTargetZ(livingEntity.z)

                    updateCountdownTicks = 4 + mob.getRandom().nextInt(7)
                    val d = mob.distanceToSqr(livingEntity)
                    if (d > 1024.0) {
                        updateCountdownTicks += 10
                    } else if (d > 256.0) {
                        updateCountdownTicks += 5
                    }

                    if (!mob.getNavigation().moveTo(livingEntity, speed)) {
                        updateCountdownTicks += 15
                    }

                    updateCountdownTicks = adjustedTickDelay(updateCountdownTicks)
                }

                setUpdateCountdownTicks(updateCountdownTicks)
                setCooldown(max(ticksUntilNextAttack - 1, 0))
                checkAndPerformAttack(livingEntity)
            }

            // copied from MeleeAttackGoal but replaced canSee with true
            override fun canPerformAttack(target: LivingEntity) = isTimeToAttack && mob.isWithinMeleeAttackRange(target)
        }
    }

    override fun registerGoals() {
        goalSelector.addGoal(1, FloatGoal(this))
        goalSelector.addGoal(2, AttackGoal(this, 1.0, true))
        goalSelector.addGoal(3, FollowOwnerGoal(this, 1.0, 6.0f, 1.5f))
        goalSelector.addGoal(4, RandomStrollGoal(this, 1.0))
        goalSelector.addGoal(5, LookAtPlayerGoal(this, Player::class.java, 8.0f))
        goalSelector.addGoal(5, RandomLookAroundGoal(this))
    }

    override fun isFood(item: ItemStack) = false

    override fun getBreedOffspring(world: ServerLevel, entity: AgeableMob) = null

    // copied from MobEntity but using own MAX_ATTACK_RANGE
    override fun isWithinMeleeAttackRange(entity: LivingEntity): Boolean {
        val attackRangeComponent = activeItem.get(DataComponents.ATTACK_RANGE)
        val maxRange: Double
        val minRange: Double
        if (attackRangeComponent == null) {
            maxRange = MAX_ATTACK_RANGE
            minRange = 0.0
        } else {
            maxRange = attackRangeComponent.effectiveMaxRange(this).toDouble()
            minRange = attackRangeComponent.effectiveMinRange(this).toDouble()
        }

        val box = entity.getHitbox()
        return this.getAttackBoundingBox(maxRange).intersects(box) && (minRange <= 0.0 || !this.getAttackBoundingBox(minRange).intersects(box))
    }

    override fun doPush(entity: Entity) {
        if (entity is SpiderlingEntity) super.doPush(entity)
    }

    override fun getSoundSource() = SoundSource.NEUTRAL

    override fun getAmbientSound(): SoundEvent = SoundEvents.SPIDER_AMBIENT

    override fun getHurtSound(source: DamageSource): SoundEvent = SoundEvents.SPIDER_HURT

    override fun getDeathSound(): SoundEvent = SoundEvents.SPIDER_DEATH

    override fun playStepSound(pos: BlockPos, state: BlockState) {
        playSound(SoundEvents.SPIDER_STEP, 0.15f, 1.0f)
    }
}