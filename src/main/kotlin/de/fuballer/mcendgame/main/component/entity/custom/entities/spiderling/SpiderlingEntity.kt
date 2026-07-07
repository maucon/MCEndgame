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
import net.minecraft.block.BlockState
import net.minecraft.component.DataComponentTypes
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.passive.PassiveEntity
import net.minecraft.entity.passive.TameableEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.world.World
import kotlin.math.max
import kotlin.math.min

class SpiderlingEntity(
    type: EntityType<out SpiderlingEntity>,
    world: World,
) : TameableEntity(type, world) {
    companion object {
        private const val MAX_ATTACK_RANGE = 2.0

        fun createAttributes(): DefaultAttributeContainer.Builder {
            return createLivingAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 24.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.4)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 0.3)
                .add(EntityAttributes.GENERIC_ARMOR, 0.0)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.8)
                .add(EntityAttributes.GENERIC_MOVEMENT_EFFICIENCY, 0.85)
                .add(EntityAttributes.GENERIC_SAFE_FALL_DISTANCE, 10.0)
                .add(EntityAttributes.GENERIC_FALL_DAMAGE_MULTIPLIER, 0.1)
                .add(EntityAttributes.GENERIC_SCALE, 0.5)
        }

        class AttackGoal(
            mob: SpiderlingEntity,
            val speed: Double,
            pauseWhenMobIdle: Boolean,
        ) : MeleeAttackGoal(mob, speed, pauseWhenMobIdle) {
            // copied from MeleeAttackGoal but replaced canSee with true
            override fun tick() {
                val livingEntity = mob.target ?: return
                mob.getLookControl().lookAt(livingEntity, 30.0f, 30.0f)

                var updateCountdownTicks = getUpdateCountdownTicks()
                val targetX = getTargetX()
                val targetY = getTargetY()
                val targetZ = getTargetZ()

                updateCountdownTicks = (max(updateCountdownTicks - 1, 0))

                if (updateCountdownTicks <= 0
                    && (targetX == 0.0 && targetY == 0.0 && targetZ == 0.0
                            || livingEntity.squaredDistanceTo(targetX, targetY, targetZ) >= 1.0
                            || mob.getRandom().nextFloat() < 0.05f)
                ) {
                    setTargetX(livingEntity.x)
                    setTargetY(livingEntity.y)
                    setTargetZ(livingEntity.z)

                    updateCountdownTicks = 4 + mob.getRandom().nextInt(7)
                    val d = mob.squaredDistanceTo(livingEntity)
                    if (d > 1024.0) {
                        updateCountdownTicks += 10
                    } else if (d > 256.0) {
                        updateCountdownTicks += 5
                    }

                    if (!mob.getNavigation().startMovingTo(livingEntity, speed)) {
                        updateCountdownTicks += 15
                    }

                    updateCountdownTicks = getTickCount(updateCountdownTicks)
                }

                setUpdateCountdownTicks(updateCountdownTicks)
                setCooldown(max(cooldown - 1, 0))
                attack(livingEntity)
            }

            // copied from MeleeAttackGoal but replaced canSee with true
            override fun canAttack(target: LivingEntity) = isCooledDown && mob.isInAttackRange(target)
        }
    }

    override fun initGoals() {
        goalSelector.add(1, SwimGoal(this))
        goalSelector.add(2, AttackGoal(this, 1.0, true))
        goalSelector.add(3, FollowOwnerGoal(this, 1.0, 6.0f, 1.5f))
        goalSelector.add(4, WanderAroundGoal(this, 1.0))
        goalSelector.add(5, LookAtEntityGoal(this, PlayerEntity::class.java, 8.0f))
        goalSelector.add(5, LookAroundGoal(this))
    }

    override fun isBreedingItem(item: ItemStack) = false

    override fun createChild(world: ServerWorld, entity: PassiveEntity) = null

    // copied from MobEntity but using own MAX_ATTACK_RANGE
    override fun getAttackBox(): Box {
        val entity = vehicle
        val box3: Box
        if (entity != null) {
            val box = entity.boundingBox
            val box2 = boundingBox
            box3 = Box(min(box2.minX, box.minX), box2.minY, min(box2.minZ, box.minZ), max(box2.maxX, box.maxX), box2.maxY, max(box2.maxZ, box.maxZ))
        } else {
            box3 = boundingBox
        }

        return box3.expand(MAX_ATTACK_RANGE, 0.0, MAX_ATTACK_RANGE)
    }

    override fun pushAway(entity: Entity) {
        if (entity is SpiderlingEntity) super.pushAway(entity)
    }

    override fun getSoundCategory() = SoundCategory.NEUTRAL

    override fun getAmbientSound(): SoundEvent = SoundEvents.ENTITY_SPIDER_AMBIENT

    override fun getHurtSound(source: DamageSource): SoundEvent = SoundEvents.ENTITY_SPIDER_HURT

    override fun getDeathSound(): SoundEvent = SoundEvents.ENTITY_SPIDER_DEATH

    override fun playStepSound(pos: BlockPos, state: BlockState) {
        playSound(SoundEvents.ENTITY_SPIDER_STEP, 0.15f, 1.0f)
    }
}