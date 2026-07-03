package de.fuballer.mcendgame.main.component.entity.custom.entities.beastweaver

import com.geckolib.animatable.GeoAnimatable
import com.geckolib.animatable.GeoEntity
import com.geckolib.animatable.instance.AnimatableInstanceCache
import com.geckolib.animatable.manager.AnimatableManager
import com.geckolib.animation.AnimationController
import com.geckolib.animation.RawAnimation
import com.geckolib.animation.`object`.PlayState
import com.geckolib.util.GeckoLibUtil
import de.fuballer.mcendgame.main.component.entity.custom.attack.Attack
import de.fuballer.mcendgame.main.component.entity.custom.attack.AttackPose
import de.fuballer.mcendgame.main.component.entity.custom.attack.damage.instance.AttackDamageInstance
import de.fuballer.mcendgame.main.component.entity.custom.entities.beakburn.BeakburnEntity
import de.fuballer.mcendgame.main.component.entity.custom.goals.*
import de.fuballer.mcendgame.main.component.entity.custom.interfaces.BlockAbleMovementMob
import de.fuballer.mcendgame.main.component.entity.custom.interfaces.CustomAttacksMob
import de.fuballer.mcendgame.main.component.entity.custom.interfaces.DisableAbleGoalsMob
import de.fuballer.mcendgame.main.component.entity.custom.sound.DelayedSoundInstance
import de.fuballer.mcendgame.main.util.random.RandomOption
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
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

class BeastweaverEntity(
    type: EntityType<out BeastweaverEntity>,
    world: Level,
) : PathfinderMob(type, world), GeoEntity, DisableAbleGoalsMob, BlockAbleMovementMob<BeastweaverEntity>, Enemy, CustomAttacksMob<BeastweaverEntity> {
    companion object {
        private val TRANSFORM_BASE_ANIM = RawAnimation.begin().thenLoop("transform.base")
        private const val MAX_TRANSFORM_PROGRESS_PER_TICK = 0.01F

        private const val TRANSFORM_EXTRAS_ANIM_CONTROLLED_ID = "Transform Extras"
        private val TRANSFORM_SHOULDER_SPIKES_ANIM: RawAnimation = RawAnimation.begin().thenPlayAndHold("transform.shoulder_spikes")
        private const val TRANSFORM_SHOULDER_SPIKES_ID = "Transform Shoulder Spikes"

        val TRANSFORM_EXTRAS_DATA = listOf(
            TransformExtrasData(
                0.5,
                TRANSFORM_EXTRAS_ANIM_CONTROLLED_ID,
                TRANSFORM_SHOULDER_SPIKES_ID,
                hiddenBeforeTrigger = setOf(
                    "leftArmSpikes",
                    "rightArmSpikes",
                ),
                hiddenAfterTrigger = setOf(
                    "leftArmPauldronIntact",
                    "rightArmPauldronIntact",
                ),
            )
        )

        private val ATTACKS: List<RandomOption<out Attack<BeastweaverEntity>>> = listOf()

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

        val TRANSFORM_PROGRESS: EntityDataAccessor<Float> = SynchedEntityData.defineId(BeastweaverEntity::class.java, EntityDataSerializers.FLOAT)
        val PREVIOUS_TRANSFORM_PROGRESS: EntityDataAccessor<Float> = SynchedEntityData.defineId(BeastweaverEntity::class.java, EntityDataSerializers.FLOAT)
    }

    override var blockAbleMovementEntity = this
    override var blockedMovementTicks = 0
    override var blockedMovementAirborne = false

    override var attackPose = AttackPose.DEFAULT
    override var attackDuration = 0
    override val attacks = ATTACKS
    override val attackCooldowns: MutableMap<Attack<BeastweaverEntity>, Int> = mutableMapOf()
    override val attackDamageInstances = mutableListOf<AttackDamageInstance>()
    override val attackSoundInstances = mutableListOf<DelayedSoundInstance>()

    private val cache: AnimatableInstanceCache = GeckoLibUtil.createInstanceCache(this)
    override fun getAnimatableInstanceCache() = cache

    private val transformExtrasAnimationController = AnimationController<GeoAnimatable>(TRANSFORM_EXTRAS_ANIM_CONTROLLED_ID) { _ -> PlayState.STOP }
        .triggerableAnim(TRANSFORM_SHOULDER_SPIKES_ID, TRANSFORM_SHOULDER_SPIKES_ANIM)

    override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {
        controllers.add(
            AnimationController<BeakburnEntity>("Transform Base", 0)
            { test -> test.setAndContinue(TRANSFORM_BASE_ANIM) },

            transformExtrasAnimationController,
        )
    }

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        super.defineSynchedData(builder)
        builder.define(TRANSFORM_PROGRESS, 0F)
        builder.define(PREVIOUS_TRANSFORM_PROGRESS, 0F)
    }

    fun getTransformProgress(tickProgress: Float): Float {
        val previous = entityData.get(PREVIOUS_TRANSFORM_PROGRESS)
        val current = entityData.get(TRANSFORM_PROGRESS)
        return previous + (current - previous) * tickProgress
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
        tickTransformProgress()
        tickBlockedMovement()
        tickAttacks(world, this)
    }

    private fun tickTransformProgress() {
        val previousProgress = entityData.get(TRANSFORM_PROGRESS)
        entityData.set(PREVIOUS_TRANSFORM_PROGRESS, previousProgress)

        val healthPercentage = (health / maxHealth).coerceIn(0F, 1F)
        val targetValue = 1 - healthPercentage
        val change = (targetValue - previousProgress).coerceIn(0.0F, MAX_TRANSFORM_PROGRESS_PER_TICK)
        entityData.set(TRANSFORM_PROGRESS, previousProgress + change)

        tickTransformExtras()
    }

    private fun tickTransformExtras() {
        TRANSFORM_EXTRAS_DATA.forEach { data ->
            if (!data.isTriggered(entityData.get(TRANSFORM_PROGRESS)) || data.isTriggered(entityData.get(PREVIOUS_TRANSFORM_PROGRESS))) return@forEach
            triggerAnim(data.animControllerId, data.animId)
        }
    }

    override fun getHurtSound(source: DamageSource): SoundEvent = SoundEvents.PLAYER_HURT

    override fun getDeathSound(): SoundEvent = SoundEvents.PLAYER_DEATH

    data class TransformExtrasData(
        val animationTriggerThreshold: Double,
        val animControllerId: String,
        val animId: String,
        val hiddenBeforeTrigger: Set<String> = setOf(),
        val hiddenAfterTrigger: Set<String> = setOf(),
        val hiddenAfterFinish: Set<String> = setOf(),
    ) {
        fun isTriggered(progress: Float) = progress >= animationTriggerThreshold

        fun getHiddenBones(progress: Float): Set<String> {
            if (!isTriggered(progress)) return hiddenBeforeTrigger
            return hiddenAfterTrigger
        }
    }

    fun getHiddenBones(): Set<String> {
        val progress = entityData.get(TRANSFORM_PROGRESS)
        return TRANSFORM_EXTRAS_DATA.flatMap { it.getHiddenBones(progress) }.toSet()
    }
}