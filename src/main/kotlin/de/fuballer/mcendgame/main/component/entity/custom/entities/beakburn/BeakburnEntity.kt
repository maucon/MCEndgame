package de.fuballer.mcendgame.main.component.entity.custom.entities.beakburn

import com.geckolib.animatable.GeoAnimatable
import com.geckolib.animatable.GeoEntity
import com.geckolib.animatable.instance.AnimatableInstanceCache
import com.geckolib.animatable.manager.AnimatableManager
import com.geckolib.animation.AnimationController
import com.geckolib.animation.RawAnimation
import com.geckolib.animation.`object`.PlayState
import com.geckolib.constant.DefaultAnimations
import com.geckolib.util.GeckoLibUtil
import de.fuballer.mcendgame.main.component.entity.custom.attack.Attack
import de.fuballer.mcendgame.main.component.entity.custom.attack.AttackPose
import de.fuballer.mcendgame.main.component.entity.custom.attack.LeapAttack
import de.fuballer.mcendgame.main.component.entity.custom.attack.WindBurstAttack
import de.fuballer.mcendgame.main.component.entity.custom.attack.damage.BasicAttackDamage
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.AttackAnimationData
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.DelayedAttackDataInstance
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.DelayedDamageData
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.DelayedLeapDamageData
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.sound.DelayedSoundData
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.sound.SoundData
import de.fuballer.mcendgame.main.component.entity.custom.attack.fire_geysers.FireGeysersAttack
import de.fuballer.mcendgame.main.component.entity.custom.attack.flame_breath.FlameBreathAttack
import de.fuballer.mcendgame.main.component.entity.custom.attack.trigger_condition.DistanceTriggerCondition
import de.fuballer.mcendgame.main.component.entity.custom.attack.trigger_condition.HealthTriggerCondition
import de.fuballer.mcendgame.main.component.entity.custom.attack.trigger_condition.TriggerConditionGroup
import de.fuballer.mcendgame.main.component.entity.custom.attack.trigger_condition.YDistanceTriggerCondition
import de.fuballer.mcendgame.main.component.entity.custom.goals.*
import de.fuballer.mcendgame.main.component.entity.custom.interfaces.BlockAbleMovementMob
import de.fuballer.mcendgame.main.component.entity.custom.interfaces.CustomAttacksMob
import de.fuballer.mcendgame.main.component.entity.custom.interfaces.DisableAbleGoalsMob
import de.fuballer.mcendgame.main.util.random.RandomOption
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.FloatGoal
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.monster.Enemy
import net.minecraft.world.entity.npc.villager.Villager
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import kotlin.math.sqrt
import kotlin.random.Random

class BeakburnEntity(
    type: EntityType<out BeakburnEntity>,
    world: Level,
) : PathfinderMob(type, world), GeoEntity, DisableAbleGoalsMob, BlockAbleMovementMob<BeakburnEntity>, Enemy, CustomAttacksMob<BeakburnEntity> {
    companion object {
        private val RUN_ANIM = RawAnimation.begin().thenLoop("move.run")

        private const val ATTACK_ANIM_CONTROLLER_ID = "Attack"

        private val BASIC_ATTACK_DAMAGE = BasicAttackDamage(0.45F, 1.0, 3.5)

        private val PECK_ANIM: RawAnimation = RawAnimation.begin().thenPlay("attack.peck")
        private const val PECK_ID = "Peck"
        private val PECK_ANIM_DATA = AttackAnimationData(AttackPose.DEFAULT, AttackPose.DEFAULT, ATTACK_ANIM_CONTROLLER_ID, PECK_ID)
        private val PECK_ATTACK =
            Attack<BeakburnEntity>(
                PECK_ID,
                PECK_ANIM_DATA,
                10 + 2,
                0,
                DistanceTriggerCondition(3.0),
                data = listOf(
                    DelayedDamageData(BASIC_ATTACK_DAMAGE, 4),
                    DelayedSoundData(SoundData(SoundEvents.NOTE_BLOCK_HAT.value(), { 1F }, { 0.7F + 0.3F * Random.nextFloat() }, SoundSource.HOSTILE), 4),
                ),
            )

        private val BITE_SOUND = DelayedSoundData(SoundData(SoundEvents.NOTE_BLOCK_HAT.value(), { 1F }, { 0.7F + 0.3F * Random.nextFloat() }, SoundSource.HOSTILE), 6)
        private val BITE_RIGHT_ANIM: RawAnimation = RawAnimation.begin().thenPlay("attack.bite_right")
        private const val BITE_RIGHT_ID = "Bite Right"
        private val BITE_RIGHT_ANIM_DATA = AttackAnimationData(AttackPose.DEFAULT, AttackPose.DEFAULT, ATTACK_ANIM_CONTROLLER_ID, BITE_RIGHT_ID)
        private val BITE_RIGHT_ATTACK =
            Attack<BeakburnEntity>(
                BITE_RIGHT_ID,
                BITE_RIGHT_ANIM_DATA,
                13 + 2,
                0,
                DistanceTriggerCondition(3.0),
                data = listOf(
                    DelayedDamageData(BASIC_ATTACK_DAMAGE, 6),
                    BITE_SOUND,
                ),
            )

        private val BITE_LEFT_ANIM: RawAnimation = RawAnimation.begin().thenPlay("attack.bite_left")
        private const val BITE_LEFT_ID = "Bite Left"
        private val BITE_LEFT_ANIM_DATA = AttackAnimationData(AttackPose.DEFAULT, AttackPose.DEFAULT, ATTACK_ANIM_CONTROLLER_ID, BITE_LEFT_ID)
        private val BITE_LEFT_ATTACK =
            Attack<BeakburnEntity>(
                BITE_LEFT_ID,
                BITE_LEFT_ANIM_DATA,
                13 + 2,
                0,
                DistanceTriggerCondition(3.0),
                data = listOf(
                    DelayedDamageData(BASIC_ATTACK_DAMAGE, 6),
                    BITE_SOUND,
                ),
            )

        private val WING_SWIPE_RIGHT_ANIM: RawAnimation = RawAnimation.begin().thenPlay("attack.wing_swipe_right")
        private const val WING_SWIPE_RIGHT_ID = "Wing Swipe Right"
        private val WING_SWIPE_RIGHT_ANIM_DATA = AttackAnimationData(AttackPose.DEFAULT, AttackPose.DEFAULT, ATTACK_ANIM_CONTROLLER_ID, WING_SWIPE_RIGHT_ID)
        private val WING_SWIPE_RIGHT_ATTACK =
            Attack<BeakburnEntity>(
                WING_SWIPE_RIGHT_ID,
                WING_SWIPE_RIGHT_ANIM_DATA,
                13 + 2,
                0,
                DistanceTriggerCondition(3.0),
                data = listOf(
                    DelayedDamageData(BASIC_ATTACK_DAMAGE, 4),
                ),
            )

        private val WING_SWIPE_LEFT_ANIM: RawAnimation = RawAnimation.begin().thenPlay("attack.wing_swipe_left")
        private const val WING_SWIPE_LEFT_ID = "Wing Swipe Left"
        private val WING_SWIPE_LEFT_ANIM_DATA = AttackAnimationData(AttackPose.DEFAULT, AttackPose.DEFAULT, ATTACK_ANIM_CONTROLLER_ID, WING_SWIPE_LEFT_ID)
        private val WING_SWIPE_LEFT_ATTACK =
            Attack<BeakburnEntity>(
                WING_SWIPE_LEFT_ID,
                WING_SWIPE_LEFT_ANIM_DATA,
                13 + 2,
                0,
                DistanceTriggerCondition(3.0),
                data = listOf(
                    DelayedDamageData(BASIC_ATTACK_DAMAGE, 4),
                ),
            )

        private val POUNCE_TRIGGER_CONDITION = TriggerConditionGroup(
            TriggerConditionGroup.TriggerConditionJoinType.AND,
            listOf(
                YDistanceTriggerCondition(-2.5, 2.5),
                DistanceTriggerCondition(5.0, 12.0),
            )
        )

        private val POUNCE_ANIM: RawAnimation = RawAnimation.begin().thenPlay("attack.pounce")
        private const val POUNCE_ID = "Pounce"
        private val POUNCE_ANIM_DATA = AttackAnimationData(AttackPose.DEFAULT, AttackPose.DEFAULT, ATTACK_ANIM_CONTROLLER_ID, POUNCE_ID)
        private val POUNCE_ATTACK =
            LeapAttack<BeakburnEntity>(
                POUNCE_ID,
                POUNCE_ANIM_DATA,
                20 + 4,
                0,
                POUNCE_TRIGGER_CONDITION,
                data = listOf(
                    DelayedLeapDamageData(BASIC_ATTACK_DAMAGE, 5, 11),
                ),
                LeapAttack.LeapType.BASIC,
                blockMovementDuration = 20,
            )

        private val WIND_BURST_ANIM: RawAnimation = RawAnimation.begin().thenPlay("attack.wind_burst")
        private const val WIND_BURST_ID = "Wind Burst"
        private val WIND_BURST_ANIM_DATA = AttackAnimationData(AttackPose.DEFAULT, AttackPose.DEFAULT, ATTACK_ANIM_CONTROLLER_ID, WIND_BURST_ID)
        private val WIND_BURST_ATTACK =
            WindBurstAttack<BeakburnEntity>(
                WIND_BURST_ID,
                WIND_BURST_ANIM_DATA,
                20 + 3,
                80,
                DistanceTriggerCondition(12.0),
                data = listOf(),
                LeapAttack.LeapType.JUMP_BACK,
                projectileCount = { distance -> (distance / 2).toInt() },
                projectileSpeed = { 1.2F + 0.2F * Random.nextFloat() },
                projectileDirectionSpread = { distance -> sqrt(distance * 3).toFloat() + 1F },
                projectileExplosionPower = 0.8F,
                blockMovementDuration = 20,
            )

        private val BREATH_ANIM: RawAnimation = RawAnimation.begin().thenPlay("attack.breath")
        private const val BREATH_ID = "Breath"
        private val BREATH_ANIM_DATA = AttackAnimationData(AttackPose.DEFAULT, AttackPose.DEFAULT, ATTACK_ANIM_CONTROLLER_ID, BREATH_ID)
        private val BREATH_ATTACK =
            FlameBreathAttack<BeakburnEntity>(
                BREATH_ID,
                BREATH_ANIM_DATA,
                60 + 5,
                220,
                DistanceTriggerCondition(3.0, 8.0),
                data = listOf(
                    DelayedSoundData(SoundData(SoundEvents.BREEZE_INHALE, 0.7F, 0.6F, SoundSource.HOSTILE), 0),
                ),
                damageConversion = 0.5,
                delay = 20,
                duration = 30,
                angle = 70.0,
                entityWidthOffsetFactor = 1.2,
                entityHeightOffsetFactor = 0.3,
                blockMovementDuration = 60,
            )

        private val ULTIMATE_ANIM: RawAnimation = RawAnimation.begin().thenPlay("attack.ultimate")
        private const val ULTIMATE_ID = "Ultimate"
        private val ULTIMATE_ANIM_DATA = AttackAnimationData(AttackPose.DEFAULT, AttackPose.DEFAULT, ATTACK_ANIM_CONTROLLER_ID, ULTIMATE_ID)
        private val ULTIMATE_ATTACK =
            FireGeysersAttack<BeakburnEntity>(
                ULTIMATE_ID,
                ULTIMATE_ANIM_DATA,
                43 + 5,
                Int.MAX_VALUE,
                HealthTriggerCondition(0.0, 0.5),
                data = listOf(
                    DelayedSoundData(SoundData(SoundEvents.ENDER_DRAGON_GROWL, { 0.4F }, { 3.5F + 0.3F * Random.nextFloat() }, SoundSource.HOSTILE), 10),
                ),
                burstDamageConversion = 0.75,
                durationDamageConversion = 0.4,
                delay = 10,
                radius = 15,
                geyserProbability = 0.07,
                indicatorDuration = 50,
                pillarDuration = 50,
                blockMovementDuration = 43,
            )

        private val ATTACKS: List<RandomOption<out Attack<BeakburnEntity>>> = listOf(
            RandomOption(5, PECK_ATTACK),
            RandomOption(5, BITE_RIGHT_ATTACK),
            RandomOption(5, BITE_LEFT_ATTACK),
            RandomOption(5, WING_SWIPE_RIGHT_ATTACK),
            RandomOption(5, WING_SWIPE_LEFT_ATTACK),
            RandomOption(1, POUNCE_ATTACK),
            RandomOption(1, WIND_BURST_ATTACK),
            RandomOption(4, BREATH_ATTACK),
            RandomOption(10000, ULTIMATE_ATTACK), // guaranteed once at health threshold + Int.MAX_VALUE cd
        )

        fun createAttributes(): AttributeSupplier.Builder {
            return createLivingAttributes()
                .add(Attributes.FOLLOW_RANGE, 35.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ATTACK_DAMAGE, 3.0)
                .add(Attributes.ATTACK_KNOCKBACK, 0.3)
                .add(Attributes.ARMOR, 0.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8)
                .add(Attributes.MOVEMENT_EFFICIENCY, 0.85)
                .add(Attributes.SAFE_FALL_DISTANCE, 10.0)
                .add(Attributes.FALL_DAMAGE_MULTIPLIER, 0.1)
        }
    }

    override var blockAbleMovementEntity = this
    override var blockedMovementTicks = 0
    override var blockedMovementAirborne = false

    override var attackPose = AttackPose.DEFAULT
    override var attackDuration = 0
    override val attacks = ATTACKS
    override val attackCooldowns: MutableMap<Attack<BeakburnEntity>, Int> = mutableMapOf()
    override val attackDataInstances = mutableListOf<DelayedAttackDataInstance>()

    private val cache: AnimatableInstanceCache = GeckoLibUtil.createInstanceCache(this)
    override fun getAnimatableInstanceCache() = cache

    private val attackAnimationController = AnimationController<GeoAnimatable>(ATTACK_ANIM_CONTROLLER_ID) { _ -> PlayState.STOP }
        .triggerableAnim(PECK_ID, PECK_ANIM)
        .triggerableAnim(BITE_RIGHT_ID, BITE_RIGHT_ANIM)
        .triggerableAnim(BITE_LEFT_ID, BITE_LEFT_ANIM)
        .triggerableAnim(WING_SWIPE_RIGHT_ID, WING_SWIPE_RIGHT_ANIM)
        .triggerableAnim(WING_SWIPE_LEFT_ID, WING_SWIPE_LEFT_ANIM)
        .triggerableAnim(POUNCE_ID, POUNCE_ANIM)
        .triggerableAnim(WIND_BURST_ID, WIND_BURST_ANIM)
        .triggerableAnim(BREATH_ID, BREATH_ANIM)
        .triggerableAnim(ULTIMATE_ID, ULTIMATE_ANIM)

    override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {
        controllers.add(
            AnimationController<BeakburnEntity>("Walk/Idle", 5)
            { test -> test.setAndContinue(if (test.isMoving) RUN_ANIM else DefaultAnimations.IDLE) },

            attackAnimationController,
        )
    }

    private val attackGoal = CustomAttacksGoal(this)
    private val stayInMeleeRangeGoal = StayInRangeGoal(this, 1.0, 2.5)
    private val wanderGoal = DisableAbleWanderAroundFarGoal(this, 0.7)
    private val lookAtPlayerGoal = DisableAbleLookAtEntityGoal(this, Player::class.java, 8F)
    private val lookAroundGoal = DisableAbleLookAroundGoal(this)

    init {
        initDynamicGoals()
    }

    private fun initDynamicGoals() {
        goalSelector.addGoal(2, attackGoal)
        goalSelector.addGoal(3, stayInMeleeRangeGoal)
        goalSelector.addGoal(4, wanderGoal)
        goalSelector.addGoal(5, lookAtPlayerGoal)
        goalSelector.addGoal(5, lookAroundGoal)
    }

    override fun registerGoals() {
        goalSelector.addGoal(0, FloatGoal(this))
        goalSelector.addGoal(1, ChangeTargetGoal(this, probability = 0.4, tryIntervalTicks = 20, 100, { e -> e is Player || e is Villager }))

        targetSelector.addGoal(0, HurtByTargetGoal(this))
        targetSelector.addGoal(1, NearestAttackableTargetGoal(this, Player::class.java, true))
        targetSelector.addGoal(2, NearestAttackableTargetGoal(this, Villager::class.java, true))
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
        val world = level() as? ServerLevel ?: return
        tickBlockedMovement()
        tickAttacks(world, this)
    }

    override fun getHurtSound(source: DamageSource): SoundEvent = SoundEvents.CAMEL_HUSK_HURT

    override fun getDeathSound(): SoundEvent = SoundEvents.CAMEL_HUSK_DEATH

    override fun addAdditionalSaveData(output: ValueOutput) {
        super.addAdditionalSaveData(output)
        addAttackCooldownsSaveData(output)
    }

    override fun readAdditionalSaveData(input: ValueInput) {
        super.readAdditionalSaveData(input)
        readAttackCooldownsSaveData(input)
    }
}