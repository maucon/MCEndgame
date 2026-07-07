package de.fuballer.mcendgame.main.component.entity.custom.entities.bonecrusher

import de.fuballer.mcendgame.main.component.entity.custom.attack.Attack
import de.fuballer.mcendgame.main.component.entity.custom.attack.AttackPose
import de.fuballer.mcendgame.main.component.entity.custom.attack.damage.AreaAttackDamage
import de.fuballer.mcendgame.main.component.entity.custom.attack.damage.DelayedAttackDamage
import de.fuballer.mcendgame.main.component.entity.custom.attack.damage.instance.AttackDamageInstance
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.AttackAnimationData
import de.fuballer.mcendgame.main.component.entity.custom.attack.teleport.TeleportToTargetAttack
import de.fuballer.mcendgame.main.component.entity.custom.attack.trigger_condition.DistanceTriggerCondition
import de.fuballer.mcendgame.main.component.entity.custom.goals.*
import de.fuballer.mcendgame.main.component.entity.custom.interfaces.BlockAbleMovementMob
import de.fuballer.mcendgame.main.component.entity.custom.interfaces.CustomAttacksMob
import de.fuballer.mcendgame.main.component.entity.custom.interfaces.DisableAbleGoalsMob
import de.fuballer.mcendgame.main.component.entity.custom.interfaces.TeleportAttackMob
import de.fuballer.mcendgame.main.component.entity.custom.sound.DelayedSoundInstance
import de.fuballer.mcendgame.main.util.random.RandomOption
import net.minecraft.entity.EntityType
import net.minecraft.entity.ai.goal.ActiveTargetGoal
import net.minecraft.entity.ai.goal.SwimGoal
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.mob.Monster
import net.minecraft.entity.mob.PathAwareEntity
import net.minecraft.entity.passive.VillagerEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import software.bernie.geckolib.animatable.GeoAnimatable
import software.bernie.geckolib.animatable.GeoEntity
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
import software.bernie.geckolib.animation.AnimatableManager
import software.bernie.geckolib.animation.AnimationController
import software.bernie.geckolib.animation.PlayState
import software.bernie.geckolib.animation.RawAnimation
import software.bernie.geckolib.constant.DefaultAnimations
import software.bernie.geckolib.util.GeckoLibUtil

class BonecrusherEntity(
    type: EntityType<out BonecrusherEntity>,
    world: World,
) : PathAwareEntity(type, world), GeoEntity, DisableAbleGoalsMob, BlockAbleMovementMob<BonecrusherEntity>, Monster, CustomAttacksMob<BonecrusherEntity>, TeleportAttackMob {
    companion object {
        val WALK_ANIM: RawAnimation = RawAnimation.begin().thenLoop("movement.walk")

        private const val ATTACK_ANIM_CONTROLLER_ID = "Attack"
        private const val SPIN_ANIM_CONTROLLER_ID = "Spin"

        private val HIT_ANIM: RawAnimation = RawAnimation.begin().thenPlay("attack.hit")
        private const val HIT_ID = "Hit"
        private val HIT_AREA = AreaAttackDamage.DamageArea(3.8, 1.6, 1.5, -0.2, 0.5, 0.25)
        private val HIT_ATTACK_DAMAGE = AreaAttackDamage(0.7F, 0.35, HIT_AREA, knockbackType = AreaAttackDamage.KnockbackType.FACING)
        private val HIT_ANIMATION_DATA = AttackAnimationData(AttackPose.DEFAULT, AttackPose.DEFAULT, ATTACK_ANIM_CONTROLLER_ID, HIT_ID)
        private val HIT_ATTACK = Attack<BonecrusherEntity>(
            HIT_ANIMATION_DATA,
            totalDuration = 16,
            cooldown = 0,
            DistanceTriggerCondition(3.0),
            DelayedAttackDamage(HIT_ATTACK_DAMAGE, 3),
        )

        private val SLAM_ANIM: RawAnimation = RawAnimation.begin().thenPlay("attack.slam")
        private const val SLAM_ID = "Slam"
        private val SLAM_AREA = AreaAttackDamage.DamageArea(6.0, 3.0, 1.5, 0.5, 0.0, 0.5)
        private val SLAM_ATTACK_DAMAGE = AreaAttackDamage(1.2F, 1.0, SLAM_AREA, knockbackType = AreaAttackDamage.KnockbackType.AREA_CENTER, blockable = false)
            .setParticles(100, 0.25, ParticleTypes.CRIT, 0.5)
            .setSound(false, SoundEvents.ENTITY_GENERIC_EXPLODE.value(), 1F, 1F)
        private val SLAM_ANIMATION_DATA = AttackAnimationData(AttackPose.DEFAULT, AttackPose.DEFAULT, ATTACK_ANIM_CONTROLLER_ID, SLAM_ID)
        private val SLAM_ATTACK = Attack<BonecrusherEntity>(
            SLAM_ANIMATION_DATA,
            totalDuration = 28,
            cooldown = 100,
            DistanceTriggerCondition(1.5, 4.0),
            DelayedAttackDamage(SLAM_ATTACK_DAMAGE, 14),
            blockMovementDuration = 22,
        )

        private val TELEPORT_PRESS_ANIM: RawAnimation = RawAnimation.begin().thenPlay("attack.press")
        private const val TELEPORT_PRESS_ID = "Teleport Press"
        private val TELEPORT_PRESS_AREA = AreaAttackDamage.DamageArea(6.0, 3.0, 1.5, -2.6, 0.0, 0.5)
        private val TELEPORT_PRESS_DAMAGE = AreaAttackDamage(1F, 1.0, TELEPORT_PRESS_AREA, knockbackType = AreaAttackDamage.KnockbackType.AREA_CENTER)
            .setParticles(100, 0.25, ParticleTypes.CRIT, 0.5)
            .setSound(false, SoundEvents.ENTITY_GENERIC_EXPLODE.value(), 1F, 1F)
        private val TELEPORT_PRESS_DATA = AttackAnimationData(AttackPose.DEFAULT, AttackPose.DEFAULT, ATTACK_ANIM_CONTROLLER_ID, TELEPORT_PRESS_ID)
        private val TELEPORT_PRESS_ATTACK = TeleportToTargetAttack<BonecrusherEntity>(
            TELEPORT_PRESS_DATA,
            totalDuration = 45,
            cooldown = 65,
            DistanceTriggerCondition(6.0, 50.0),
            DelayedAttackDamage(TELEPORT_PRESS_DAMAGE, 25),
            teleportDelay = 20,
            choseLocationDelayTicks = 12,
            blockMovementDuration = 40,
        )

        const val SPIN_ATTACK_ROTATIONS = 3
        private val SPIN_ANIM: RawAnimation = RawAnimation.begin()
            .thenPlay("attack.spin.start")
            .thenPlayXTimes("attack.spin", SPIN_ATTACK_ROTATIONS)
            .thenPlay("attack.spin.end")
        private const val SPIN_ID = "Spin"
        private val SPIN_FRONT_AREA = AreaAttackDamage.DamageArea(4.0, 3.0, 1.0, 0.0, 0.0, 0.5)
        private val SPIN_FRONT_DAMAGE = AreaAttackDamage(1F, 0.25, SPIN_FRONT_AREA, knockbackType = AreaAttackDamage.KnockbackType.DAMAGER_CENTER, disableBlockingShield = 5F)
        private val SPIN_BACK_AREA = AreaAttackDamage.DamageArea(4.0, 3.0, 1.0, -4.0, 0.0, 0.5)
        private val SPIN_BACK_DAMAGE = AreaAttackDamage(1F, 0.25, SPIN_BACK_AREA, knockbackType = AreaAttackDamage.KnockbackType.DAMAGER_CENTER, disableBlockingShield = 5F)
        private val SPIN_LEFT_AREA = AreaAttackDamage.DamageArea(6.0, 2.0, 1.0, -3.0, -2.0, 0.5)
        private val SPIN_LEFT_DAMAGE = AreaAttackDamage(1F, 0.25, SPIN_LEFT_AREA, knockbackType = AreaAttackDamage.KnockbackType.DAMAGER_CENTER, disableBlockingShield = 5F)
        private val SPIN_RIGHT_AREA = AreaAttackDamage.DamageArea(6.0, 2.0, 1.0, -3.0, 2.0, 0.5)
        private val SPIN_RIGHT_DAMAGE = AreaAttackDamage(1F, 0.25, SPIN_RIGHT_AREA, knockbackType = AreaAttackDamage.KnockbackType.DAMAGER_CENTER, disableBlockingShield = 5F)
        private val SPIN_ANIMATION_DATA = AttackAnimationData(AttackPose.DEFAULT, AttackPose.DEFAULT, SPIN_ANIM_CONTROLLER_ID, SPIN_ID)
        private val SPIN_ATTACK = Attack<BonecrusherEntity>(
            SPIN_ANIMATION_DATA,
            50 + 13 * SPIN_ATTACK_ROTATIONS,
            50 + 13 * SPIN_ATTACK_ROTATIONS + 200,
            DistanceTriggerCondition(3.0),
            getSpinAttackDamage(),
        )

        private fun getSpinAttackDamage(): List<DelayedAttackDamage> {
            val damage = mutableListOf<DelayedAttackDamage>()

            // spin start
            damage.add(DelayedAttackDamage(SPIN_LEFT_DAMAGE, 5))
            damage.add(DelayedAttackDamage(SPIN_BACK_DAMAGE, 9))
            damage.add(DelayedAttackDamage(SPIN_RIGHT_DAMAGE, 13))

            // main spin
            for (rot in 0 until SPIN_ATTACK_ROTATIONS) {
                val base = 16 + (rot * 20 * 2 / 3.0).toInt()
                damage.add(DelayedAttackDamage(SPIN_FRONT_DAMAGE, base))
                damage.add(DelayedAttackDamage(SPIN_LEFT_DAMAGE, base + 3))
                damage.add(DelayedAttackDamage(SPIN_BACK_DAMAGE, base + 6))
                damage.add(DelayedAttackDamage(SPIN_RIGHT_DAMAGE, base + 10))
            }

            // spin end
            val base = 16 + (SPIN_ATTACK_ROTATIONS * 20 * 2 / 3.0).toInt()
            damage.add(DelayedAttackDamage(SPIN_FRONT_DAMAGE, base))
            damage.add(DelayedAttackDamage(SPIN_LEFT_DAMAGE, base + 5))
            damage.add(DelayedAttackDamage(SPIN_BACK_DAMAGE, base + 10))

            return damage
        }

        private val ATTACKS = listOf(
            RandomOption(5, HIT_ATTACK),
            RandomOption(2, SLAM_ATTACK),
            RandomOption(1, SPIN_ATTACK),
            RandomOption(1, TELEPORT_PRESS_ATTACK),
        )

        fun createAttributes(): DefaultAttributeContainer.Builder {
            return createLivingAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 35.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 7.0)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 2.0)
                .add(EntityAttributes.GENERIC_ARMOR, 0.0)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.8)
                .add(EntityAttributes.GENERIC_MOVEMENT_EFFICIENCY, 0.85)
        }
    }

    private val cache: AnimatableInstanceCache = GeckoLibUtil.createInstanceCache(this)
    override fun getAnimatableInstanceCache() = cache

    override var attackPose = AttackPose.DEFAULT
    override var attackDuration = 0
    override val attacks = ATTACKS
    override val attackCooldowns: MutableMap<Attack<BonecrusherEntity>, Int> = mutableMapOf()
    override val attackDamageInstances = mutableListOf<AttackDamageInstance>()
    override val attackSoundInstances = mutableListOf<DelayedSoundInstance>()

    override var teleportAttackTargetPosition: Vec3d? = null

    override var blockAbleMovementEntity = this
    override var blockedMovementTicks = 0
    override var blockedMovementAirborne = false

    private val attackGoal = CustomAttacksGoal(this)
    private val stayInMeleeRangeGoal = StayInRangeGoal(this, 1.0, 2.5)
    private val wanderGoal = DisableAbleWanderAroundFarGoal(this, 1.0)
    private val lookAtPlayerGoal = DisableAbleLookAtEntityGoal(this, PlayerEntity::class.java, 8F)
    private val lookAroundGoal = DisableAbleLookAroundGoal(this)

    init {
        initDynamicGoals()
    }

    private fun initDynamicGoals() {
        goalSelector.add(2, attackGoal)
        goalSelector.add(3, stayInMeleeRangeGoal)
        goalSelector.add(4, lookAtPlayerGoal)
        goalSelector.add(4, lookAroundGoal)
        goalSelector.add(5, wanderGoal)
    }

    override fun initGoals() {
        goalSelector.add(0, SwimGoal(this))
        goalSelector.add(1, ChangeTargetGoal(this, probability = 0.4, tryIntervalTicks = 20, 100, { e -> e is PlayerEntity || e is VillagerEntity }))

        targetSelector.add(2, ActiveTargetGoal(this, PlayerEntity::class.java, true))
        targetSelector.add(3, ActiveTargetGoal(this, VillagerEntity::class.java, true))
    }

    override fun updateGoals() {
        val movementBlocked = isMovementBlocked()
        attackGoal.isDisabled = movementBlocked
        stayInMeleeRangeGoal.isDisabled = movementBlocked
        wanderGoal.isDisabled = movementBlocked
        lookAtPlayerGoal.isDisabled = movementBlocked
        lookAroundGoal.isDisabled = movementBlocked
    }

    override fun tick() {
        super.tick()
        tickBlockedMovement()

        val world = entityWorld as? ServerWorld ?: return
        tickAttacks(world, this)
    }

    override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {
        controllers.add(
            AnimationController<GeoAnimatable>(this, "Walk/Idle", 5)
            { test -> test.setAndContinue(if (test.isMoving) WALK_ANIM else DefaultAnimations.IDLE) },
            AnimationController<GeoAnimatable>(this, ATTACK_ANIM_CONTROLLER_ID, 0) { _ -> PlayState.STOP }
                .triggerableAnim(HIT_ID, HIT_ANIM)
                .triggerableAnim(SLAM_ID, SLAM_ANIM)
                .triggerableAnim(TELEPORT_PRESS_ID, TELEPORT_PRESS_ANIM),
            AnimationController<GeoAnimatable>(this, SPIN_ANIM_CONTROLLER_ID, 0) { _ -> PlayState.STOP }
                .triggerableAnim(SPIN_ID, SPIN_ANIM)
        )
    }
}