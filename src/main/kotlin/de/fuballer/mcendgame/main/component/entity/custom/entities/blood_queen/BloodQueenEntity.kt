package de.fuballer.mcendgame.main.component.entity.custom.entities.blood_queen

import com.geckolib.animatable.GeoAnimatable
import com.geckolib.animatable.GeoEntity
import com.geckolib.animatable.instance.AnimatableInstanceCache
import com.geckolib.animatable.manager.AnimatableManager
import com.geckolib.animation.AnimationController
import com.geckolib.animation.RawAnimation
import com.geckolib.animation.`object`.PlayState
import com.geckolib.constant.DefaultAnimations
import com.geckolib.util.GeckoLibUtil
import de.fuballer.mcendgame.main.component.entity.custom.GeoEntityLocatorService
import de.fuballer.mcendgame.main.component.entity.custom.attack.Attack
import de.fuballer.mcendgame.main.component.entity.custom.attack.AttackPose
import de.fuballer.mcendgame.main.component.entity.custom.attack.damage.AreaAttackDamage
import de.fuballer.mcendgame.main.component.entity.custom.attack.damage.damage_area.BoxDamageArea
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.AttackAnimationData
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.DelayedAttackData
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.DelayedAttackDataInstance
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.DelayedDamageData
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.particle.DelayedParticleData
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.particle.ParticleData
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.sound.DelayedSoundData
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.sound.RangeDefinedSoundData
import de.fuballer.mcendgame.main.component.entity.custom.attack.trigger_condition.DistanceTriggerCondition
import de.fuballer.mcendgame.main.component.entity.custom.attack.trigger_condition.StrafeTriggerCondition
import de.fuballer.mcendgame.main.component.entity.custom.attack.trigger_condition.TriggerConditionGroup
import de.fuballer.mcendgame.main.component.entity.custom.entities.magma_spike.MagmaSpikesUtil
import de.fuballer.mcendgame.main.component.entity.custom.entities.spell_fireball.SpellFireballUtil
import de.fuballer.mcendgame.main.component.entity.custom.goals.*
import de.fuballer.mcendgame.main.component.entity.custom.interfaces.BlockAbleMovementMob
import de.fuballer.mcendgame.main.component.entity.custom.interfaces.CustomAttacksMob
import de.fuballer.mcendgame.main.component.entity.custom.interfaces.DisableAbleGoalsMob
import de.fuballer.mcendgame.main.component.particle.CustomParticleTypes
import de.fuballer.mcendgame.main.util.extension.mixin.MoveControlMixinExtension.getStrafeForwards
import de.fuballer.mcendgame.main.util.extension.mixin.MoveControlMixinExtension.getStrafeRight
import de.fuballer.mcendgame.main.util.random.RandomOption
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.tags.FluidTags
import net.minecraft.util.Mth
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.FloatGoal
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.ai.util.LandRandomPos
import net.minecraft.world.entity.monster.Enemy
import net.minecraft.world.entity.npc.villager.Villager
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.FluidState
import net.minecraft.world.phys.Vec3
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sin
import kotlin.random.Random

class BloodQueenEntity(
    type: EntityType<out BloodQueenEntity>,
    world: Level,
) : PathfinderMob(type, world),
    GeoEntity,
    Enemy,
    CustomAttacksMob<BloodQueenEntity>,
    DisableAbleGoalsMob,
    BlockAbleMovementMob<BloodQueenEntity> {
    companion object {
        private val LEAN_ANIM: RawAnimation = RawAnimation.begin().thenPlay("misc.lean")

        private val SWEEP_SOUND =
            RangeDefinedSoundData(
                SoundEvents.PLAYER_ATTACK_SWEEP,
                { 0.9f },
                { Random.nextDouble(0.95, 1.05).toFloat() },
                SoundSource.HOSTILE,
                range = 24.0,
            )

        private val FIRECHARGE_SOUND =
            RangeDefinedSoundData(
                SoundEvents.FIRECHARGE_USE,
                { 0.9f },
                { Random.nextDouble(0.95, 1.05).toFloat() },
                SoundSource.HOSTILE,
                range = 24.0,
            )

        private val FIRE_SOUND =
            RangeDefinedSoundData(
                SoundEvents.FIRE_AMBIENT,
                { 1.0F },
                { Random.nextDouble(0.9, 1.0).toFloat() },
                SoundSource.HOSTILE,
                range = 24.0,
            )

        private const val ATTACK_ANIM_CONTROLLER_ID = "Attack"

        private const val MELEE_RANGE = 4.5

        private val SPIKES_LINE_DATA_DELAYED = object : DelayedAttackData(12) {
            override fun apply(level: ServerLevel, entity: Mob, target: LivingEntity?) {
                if (target == null) return
                MagmaSpikesUtil.castMagmaSpikesLine(
                    entity,
                    target,
                    origin = entity.position(),
                    offset = 1.25f,
                    delay = 6 + (0.25 * entity.distanceTo(target)).toInt(),
                    minScale = 0.75f,
                    maxScale = 1.5f,
                    finalScale = 2.5f,
                    finalDelay = 0,
                    level,
                )
            }
        }

        private val MAGMA_SPIKES_LINE_LEFT_ANIM: RawAnimation = RawAnimation.begin().thenPlay("attack.spikes_line_left")
        private const val MAGMA_SPIKES_LINE_LEFT_ID = "Magma Spikes Line Left"
        private val MAGMA_SPIKES_LINE_LEFT_ANIM_DATA = AttackAnimationData(AttackPose.DEFAULT, AttackPose.DEFAULT, ATTACK_ANIM_CONTROLLER_ID, MAGMA_SPIKES_LINE_LEFT_ID)
        private val MAGMA_SPIKES_LINE_LEFT_ATTACK =
            Attack<BloodQueenEntity>(
                MAGMA_SPIKES_LINE_LEFT_ID,
                MAGMA_SPIKES_LINE_LEFT_ANIM_DATA,
                totalDuration = 35,
                cooldown = 0,
                TriggerConditionGroup(
                    TriggerConditionGroup.TriggerConditionJoinType.AND,
                    listOf(
                        DistanceTriggerCondition(MELEE_RANGE, 25.0, affectedByScale = true),
                        StrafeTriggerCondition(minRight = 0.0f),
                    ),
                ),
                data = listOf(
                    DelayedSoundData(SWEEP_SOUND, 12),
                    SPIKES_LINE_DATA_DELAYED,
                ),
            )

        private val MAGMA_SPIKES_LINE_RIGHT_ANIM: RawAnimation = RawAnimation.begin().thenPlay("attack.spikes_line_right")
        private const val MAGMA_SPIKES_LINE_RIGHT_ID = "Magma Spikes Line Right"
        private val MAGMA_SPIKES_LINE_RIGHT_ANIM_DATA = AttackAnimationData(AttackPose.DEFAULT, AttackPose.DEFAULT, ATTACK_ANIM_CONTROLLER_ID, MAGMA_SPIKES_LINE_RIGHT_ID)
        private val MAGMA_SPIKES_LINE_RIGHT_ATTACK =
            Attack<BloodQueenEntity>(
                MAGMA_SPIKES_LINE_RIGHT_ID,
                MAGMA_SPIKES_LINE_RIGHT_ANIM_DATA,
                totalDuration = 35,
                cooldown = 0,
                TriggerConditionGroup(
                    TriggerConditionGroup.TriggerConditionJoinType.AND,
                    listOf(
                        DistanceTriggerCondition(MELEE_RANGE, 25.0, affectedByScale = true),
                        StrafeTriggerCondition(maxRight = 0.0f),
                    ),
                ),
                data = listOf(
                    DelayedSoundData(SWEEP_SOUND, 12),
                    SPIKES_LINE_DATA_DELAYED,
                ),
            )

        private val MAGMA_SPIKES_SLAM_ANIM: RawAnimation = RawAnimation.begin().thenPlay("attack.spikes_slam")
        private const val MAGMA_SPIKES_SLAM_ID = "Magma Spikes Slam"
        private val MAGMA_SPIKES_SLAM_ANIM_DATA = AttackAnimationData(AttackPose.DEFAULT, AttackPose.DEFAULT, ATTACK_ANIM_CONTROLLER_ID, MAGMA_SPIKES_SLAM_ID)
        private val MAGMA_SPIKES_SLAM_ATTACK =
            Attack<BloodQueenEntity>(
                MAGMA_SPIKES_SLAM_ID,
                MAGMA_SPIKES_SLAM_ANIM_DATA,
                totalDuration = 45,
                cooldown = 0,
                DistanceTriggerCondition(MELEE_RANGE, affectedByScale = true),
                data = listOf(
                    DelayedSoundData(
                        RangeDefinedSoundData(
                            SoundEvents.AMETHYST_BLOCK_CHIME,
                            { Random.nextDouble(1.8, 2.0).toFloat() },
                            { Random.nextDouble(0.9, 1.0).toFloat() },
                            SoundSource.HOSTILE,
                            range = 24.0,
                        ),
                        5,
                    ),
                    DelayedSoundData(
                        RangeDefinedSoundData(
                            SoundEvents.GENERIC_EXPLODE.value(),
                            { 0.8f },
                            { Random.nextDouble(0.95, 1.05).toFloat() },
                            SoundSource.HOSTILE,
                            range = 24.0,
                        ),
                        18,
                    ),
                    object : DelayedAttackData(18) {
                        override fun apply(level: ServerLevel, entity: Mob, target: LivingEntity?) {
                            MagmaSpikesUtil.castMagmaSpikesSlam(
                                entity,
                                ringDelay = 6,
                                minScale = 0.75f,
                                maxScale = 1.5f,
                                ringSpikeCount = listOf(8, 10, 12),
                                ringRadius = 1.25f,
                                level,
                            )
                        }
                    },
                ),
                blockMovementDuration = 40,
            )

        private const val TELEPORT_MIN_PLAYER_DISTANCE = 8.0
        private const val TELEPORT_MIN_PLAYER_DISTANCE_SQUARED = TELEPORT_MIN_PLAYER_DISTANCE * TELEPORT_MIN_PLAYER_DISTANCE
        private const val TELEPORT_HORIZONTAL_RANGE = 16
        private const val TELEPORT_VERTICAL_RANGE = 7
        private const val TELEPORT_ATTEMPTS = 20

        private const val TELEPORT_ID = "Teleport"
        private val TELEPORT_ANIM_DATA = AttackAnimationData(AttackPose.DEFAULT, AttackPose.DEFAULT, null, null)
        private val TELEPORT_ATTACK =
            Attack<BloodQueenEntity>(
                TELEPORT_ID,
                TELEPORT_ANIM_DATA,
                totalDuration = 7,
                cooldown = 0,
                DistanceTriggerCondition(MELEE_RANGE, affectedByScale = true),
                data = listOf(
                    DelayedSoundData(FIRE_SOUND, 0),
                    DelayedParticleData(
                        ParticleData(
                            particle = { _, _ -> CustomParticleTypes.REVERSE_FLAME },
                            offset = { Vec3(0.0, 1.0, 0.0) },
                            count = 30,
                            dist = { Vec3(0.3, 0.5, 0.3) },
                            speed = 1.0,
                        ),
                        0,
                    ),
                    DelayedSoundData(
                        RangeDefinedSoundData(
                            SoundEvents.ENDERMAN_TELEPORT,
                            { 1.0F },
                            { Random.nextDouble(0.9, 1.0).toFloat() },
                            SoundSource.HOSTILE,
                            range = 24.0,
                        ),
                        5,
                    ),
                    DelayedSoundData(
                        RangeDefinedSoundData(
                            SoundEvents.LAVA_EXTINGUISH,
                            { 1.0F },
                            { Random.nextDouble(0.9, 1.0).toFloat() },
                            SoundSource.HOSTILE,
                            range = 24.0,
                        ),
                        5,
                    ),
                    DelayedParticleData(
                        ParticleData(
                            particle = { _, _ -> ParticleTypes.LAVA },
                            offset = { Vec3.ZERO },
                            count = 3,
                            dist = { Vec3(0.1, 0.0, 0.1) },
                            speed = 1.0,
                        ),
                        5,
                    ),
                    object : DelayedAttackData(6) {
                        override fun apply(level: ServerLevel, entity: Mob, target: LivingEntity?) {
                            (entity as? BloodQueenEntity)?.teleportEscape()
                        }
                    },
                    DelayedParticleData(
                        ParticleData(
                            particle = { _, _ -> ParticleTypes.FLAME },
                            offset = { Vec3.ZERO },
                            count = 30,
                            dist = { Vec3.ZERO },
                            speed = 1.0,
                        ),
                        7,
                    ),
                    DelayedParticleData(
                        ParticleData(
                            particle = { _, _ -> ParticleTypes.LAVA },
                            offset = { Vec3.ZERO },
                            count = 5,
                            dist = { Vec3(0.1, 0.0, 0.1) },
                            speed = 1.0,
                        ),
                        7,
                    ),
                    DelayedSoundData(FIRECHARGE_SOUND, 11)
                ),
                blockMovementDuration = 7,
            )

        private val FIREBALL_RIGHT_ANIM: RawAnimation = RawAnimation.begin().thenPlay("attack.throw_fireball_right")
        private const val FIREBALL_RIGHT_ID = "Fireball Right"
        private val FIREBALL_RIGHT_ANIM_DATA = AttackAnimationData(AttackPose.DEFAULT, AttackPose.DEFAULT, ATTACK_ANIM_CONTROLLER_ID, FIREBALL_RIGHT_ID)
        private val FIREBALL_RIGHT_ATTACK =
            Attack<BloodQueenEntity>(
                FIREBALL_RIGHT_ID,
                FIREBALL_RIGHT_ANIM_DATA,
                totalDuration = 35,
                cooldown = 0,
                TriggerConditionGroup(
                    TriggerConditionGroup.TriggerConditionJoinType.AND,
                    listOf(
                        DistanceTriggerCondition(MELEE_RANGE, 35.0, affectedByScale = true),
                        StrafeTriggerCondition(maxRight = 0.0f),
                    ),
                ),
                data = listOf(
                    DelayedSoundData(FIRE_SOUND, 0),
                    DelayedSoundData(FIRECHARGE_SOUND, 19),
                    object : DelayedAttackData(19) {
                        override fun apply(level: ServerLevel, entity: Mob, target: LivingEntity?) {
                            (entity as? BloodQueenEntity)?.throwFireball(true)
                        }
                    },
                ),
            )

        private val FIREBALL_LEFT_ANIM: RawAnimation = RawAnimation.begin().thenPlay("attack.throw_fireball_left")
        private const val FIREBALL_LEFT_ID = "Fireball Left"
        private val FIREBALL_LEFT_ANIM_DATA = AttackAnimationData(AttackPose.DEFAULT, AttackPose.DEFAULT, ATTACK_ANIM_CONTROLLER_ID, FIREBALL_LEFT_ID)
        private val FIREBALL_LEFT_ATTACK =
            Attack<BloodQueenEntity>(
                FIREBALL_LEFT_ID,
                FIREBALL_LEFT_ANIM_DATA,
                totalDuration = 35,
                cooldown = 0,
                TriggerConditionGroup(
                    TriggerConditionGroup.TriggerConditionJoinType.AND,
                    listOf(
                        DistanceTriggerCondition(MELEE_RANGE, 35.0, affectedByScale = true),
                        StrafeTriggerCondition(minRight = 0.0f),
                    ),
                ),
                data = listOf(
                    DelayedSoundData(FIRE_SOUND, 0),
                    DelayedSoundData(FIRECHARGE_SOUND, 19),
                    object : DelayedAttackData(19) {
                        override fun apply(level: ServerLevel, entity: Mob, target: LivingEntity?) {
                            (entity as? BloodQueenEntity)?.throwFireball(false)
                        }
                    },
                ),
            )

        private val PUNCH_RIGHT_AREA = BoxDamageArea(4.0, 0.5, 0.5, 0.0, 0.5, 1.5)
        private val PUNCH_RIGHT_ATTACK_DAMAGE = AreaAttackDamage(0.75F, 3.0, PUNCH_RIGHT_AREA, knockbackType = AreaAttackDamage.KnockbackType.FACING, knockbackWhenBlocked = true)
        private val PUNCH_RIGHT_ANIM: RawAnimation = RawAnimation.begin().thenPlay("attack.punch_right")
        private const val PUNCH_RIGHT_ID = "Punch Right"
        private val PUNCH_RIGHT_ANIM_DATA = AttackAnimationData(AttackPose.DEFAULT, AttackPose.DEFAULT, ATTACK_ANIM_CONTROLLER_ID, PUNCH_RIGHT_ID)
        private val PUNCH_RIGHT_ATTACK =
            Attack<BloodQueenEntity>(
                PUNCH_RIGHT_ID,
                PUNCH_RIGHT_ANIM_DATA,
                totalDuration = 25,
                cooldown = 0,
                DistanceTriggerCondition(MELEE_RANGE, affectedByScale = true),
                data = listOf(
                    DelayedSoundData(FIRE_SOUND, 0),
                    DelayedSoundData(FIRECHARGE_SOUND, 14),
                    DelayedDamageData(PUNCH_RIGHT_ATTACK_DAMAGE, 14),
                ),
            )

        private val PUNCH_LEFT_AREA = BoxDamageArea(4.0, 0.5, 0.5, 0.0, -0.5, 1.5)
        private val PUNCH_LEFT_ATTACK_DAMAGE = AreaAttackDamage(0.75F, 3.0, PUNCH_LEFT_AREA, knockbackType = AreaAttackDamage.KnockbackType.FACING, knockbackWhenBlocked = true)
        private val PUNCH_LEFT_ANIM: RawAnimation = RawAnimation.begin().thenPlay("attack.punch_left")
        private const val PUNCH_LEFT_ID = "Punch Left"
        private val PUNCH_LEFT_ANIM_DATA = AttackAnimationData(AttackPose.DEFAULT, AttackPose.DEFAULT, ATTACK_ANIM_CONTROLLER_ID, PUNCH_LEFT_ID)
        private val PUNCH_LEFT_ATTACK =
            Attack<BloodQueenEntity>(
                PUNCH_LEFT_ID,
                PUNCH_LEFT_ANIM_DATA,
                totalDuration = 25,
                cooldown = 0,
                DistanceTriggerCondition(MELEE_RANGE, affectedByScale = true),
                data = listOf(
                    DelayedSoundData(FIRE_SOUND, 0),
                    DelayedSoundData(FIRECHARGE_SOUND, 14),
                    DelayedDamageData(PUNCH_LEFT_ATTACK_DAMAGE, 14),
                ),
            )

        private val ATTACKS: List<RandomOption<out Attack<BloodQueenEntity>>> = listOf(
            RandomOption(1, MAGMA_SPIKES_LINE_LEFT_ATTACK),
            RandomOption(1, MAGMA_SPIKES_LINE_RIGHT_ATTACK),
            RandomOption(1, MAGMA_SPIKES_SLAM_ATTACK),
            RandomOption(1, TELEPORT_ATTACK),
            RandomOption(1, FIREBALL_RIGHT_ATTACK),
            RandomOption(1, FIREBALL_LEFT_ATTACK),
            RandomOption(1, PUNCH_RIGHT_ATTACK),
            RandomOption(1, PUNCH_LEFT_ATTACK),
        )

        fun createAttributes(): AttributeSupplier.Builder {
            return createLivingAttributes()
                .add(Attributes.FOLLOW_RANGE, 64.0)
                .add(Attributes.MOVEMENT_SPEED, 0.4)
                .add(Attributes.ATTACK_DAMAGE, 3.0)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5)
                .add(Attributes.ARMOR, 0.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8)
                .add(Attributes.MOVEMENT_EFFICIENCY, 0.85)
                .add(Attributes.SAFE_FALL_DISTANCE, 10.0)
                .add(Attributes.FALL_DAMAGE_MULTIPLIER, 0.1)
                .add(Attributes.WATER_MOVEMENT_EFFICIENCY, 0.65)
        }

        private val STRAFE_RIGHT: EntityDataAccessor<Float> = SynchedEntityData.defineId(BloodQueenEntity::class.java, EntityDataSerializers.FLOAT)
        private val STRAFE_FORWARDS: EntityDataAccessor<Float> = SynchedEntityData.defineId(BloodQueenEntity::class.java, EntityDataSerializers.FLOAT)

        private const val LEAN_CHANGE = 0.1F
    }

    override var blockAbleMovementEntity = this
    override var blockedMovementTicks = 0
    override var blockedMovementAirborne = false

    override var attackPose = AttackPose.DEFAULT
    override var attackDuration = 0
    override val attacks = ATTACKS
    override val attackCooldowns: MutableMap<Attack<BloodQueenEntity>, Int> = mutableMapOf()
    override val attackDataInstances = mutableListOf<DelayedAttackDataInstance>()

    var leanRight = 0F
    var lastLeanRight = 0F
    var leanForwards = 0F
    var lastLeanForwards = 0F

    private val cache: AnimatableInstanceCache = GeckoLibUtil.createInstanceCache(this)
    override fun getAnimatableInstanceCache() = cache

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        super.defineSynchedData(builder)
        builder.define(STRAFE_RIGHT, 0F)
        builder.define(STRAFE_FORWARDS, 0F)
    }

    private val attackAnimationController =
        AnimationController<GeoAnimatable>(ATTACK_ANIM_CONTROLLER_ID, 0) { _ -> PlayState.STOP }
            .triggerableAnim(MAGMA_SPIKES_LINE_LEFT_ID, MAGMA_SPIKES_LINE_LEFT_ANIM)
            .triggerableAnim(MAGMA_SPIKES_LINE_RIGHT_ID, MAGMA_SPIKES_LINE_RIGHT_ANIM)
            .triggerableAnim(MAGMA_SPIKES_SLAM_ID, MAGMA_SPIKES_SLAM_ANIM)
            .triggerableAnim(FIREBALL_RIGHT_ID, FIREBALL_RIGHT_ANIM)
            .triggerableAnim(FIREBALL_LEFT_ID, FIREBALL_LEFT_ANIM)
            .triggerableAnim(PUNCH_RIGHT_ID, PUNCH_RIGHT_ANIM)
            .triggerableAnim(PUNCH_LEFT_ID, PUNCH_LEFT_ANIM)
            .setParticleKeyframeHandler { event ->
                if (!level().isClientSide) return@setParticleKeyframeHandler

                val keyFrameData = event.keyframeData
                when (keyFrameData.effect) {
                    "create_flame" -> {
                        val pos = GeoEntityLocatorService.getLocatorPos(uuid, keyFrameData.locatorName) ?: return@setParticleKeyframeHandler
                        createFlameParticles(pos, 1, ParticleTypes.FLAME)
                        createFlameParticles(pos, 1, ParticleTypes.SMOKE)
                    }

                    "flame_punch" -> {
                        val pos = GeoEntityLocatorService.getLocatorPos(uuid, keyFrameData.locatorName) ?: return@setParticleKeyframeHandler
                        createFlameParticles(pos, 10, ParticleTypes.FLAME)
                        createFlameParticles(pos, 10, ParticleTypes.SMOKE)
                        createFlameParticles(pos, 1, ParticleTypes.EXPLOSION)
                    }
                }
            }

    override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {
        controllers.add(
            AnimationController<BloodQueenEntity>("Walk/Idle", 5)
            { test -> test.setAndContinue(if (test.isMoving) DefaultAnimations.IDLE else DefaultAnimations.IDLE) },

            attackAnimationController,

            AnimationController<BloodQueenEntity>("Lean", 5)
            { test -> if (test.isMoving) test.setAndContinue(LEAN_ANIM) else PlayState.STOP }
                .additiveAnimations(),
        )
    }

    private val attackGoal = CustomAttacksGoal(this)
    private val keepDistanceToTargetGoal = KeepDistanceToTargetGoal(this, 1.0, 12F, 20F)
    private val wanderGoal = DisableAbleWanderAroundFarGoal(this, 0.7)
    private val lookAtPlayerGoal = DisableAbleLookAtEntityGoal(this, Player::class.java, 8F)
    private val lookAroundGoal = DisableAbleLookAroundGoal(this)

    init {
        initDynamicGoals()
    }

    private fun initDynamicGoals() {
        goalSelector.addGoal(2, attackGoal)
        goalSelector.addGoal(3, keepDistanceToTargetGoal)
        goalSelector.addGoal(5, wanderGoal)
        goalSelector.addGoal(6, lookAtPlayerGoal)
        goalSelector.addGoal(7, lookAroundGoal)
    }

    override fun registerGoals() {
        goalSelector.addGoal(1, ChangeTargetGoal(this, probability = 0.4, tryIntervalTicks = 20, 100, { e -> e is Player || e is Villager }))
        goalSelector.addGoal(4, FloatGoal(this))

        targetSelector.addGoal(0, HurtByTargetGoal(this))
        targetSelector.addGoal(1, NearestAttackableTargetGoal(this, Player::class.java, false))
    }

    override fun updateGoals() {
        val movementBlocked = isMovementBlocked()
        attackGoal.isDisabled = movementBlocked
        keepDistanceToTargetGoal.isDisabled = movementBlocked
        wanderGoal.isDisabled = movementBlocked
        lookAtPlayerGoal.isDisabled = movementBlocked
        lookAroundGoal.isDisabled = movementBlocked
    }

    override fun tick() {
        super.tick()

        val level = level()
        if (level !is ServerLevel) {
            tickLean()
            return
        }

        tickBlockedMovement()
        tickAttacks(level, this)
        tickStrafe()
        floatLiquid()
    }

    private fun floatLiquid() {
        if (!(isInLava || isInWater)) return

        val context = CollisionContext.of(this)
        if (context.isAbove(liquidCollisionShape, blockPosition(), true)) {
            val aboveFluidState = level().getFluidState(blockPosition().above())
            if (!aboveFluidState.`is`(FluidTags.LAVA) && !aboveFluidState.`is`(FluidTags.WATER)) {
                setOnGround(true)
                return
            }
        }

        deltaMovement = deltaMovement.scale(0.5).add(0.0, 0.05, 0.0)
    }

    override fun getLiquidCollisionShape(): VoxelShape = Block.column(16.0, 0.0, 16.0)

    private fun tickStrafe() {
        entityData.set(STRAFE_RIGHT, moveControl.getStrafeRight())
        entityData.set(STRAFE_FORWARDS, moveControl.getStrafeForwards())
    }

    private fun tickLean() {
        lastLeanRight = leanRight
        val strafeRight = entityData.get(STRAFE_RIGHT)
        leanRight = getNewLean(strafeRight, leanRight)

        lastLeanForwards = leanForwards
        val strafeForwards = entityData.get(STRAFE_FORWARDS)
        leanForwards = getNewLean(strafeForwards, leanForwards)
    }

    private fun getNewLean(
        strafe: Float,
        currentLean: Float,
    ) = when {
        strafe < 0 -> (currentLean + LEAN_CHANGE).coerceAtMost(1F)
        strafe > 0 -> (currentLean - LEAN_CHANGE).coerceAtLeast(-1F)
        else -> when {
            currentLean > 0F -> (currentLean - LEAN_CHANGE).coerceAtLeast(0F)
            currentLean < 0F -> (currentLean + LEAN_CHANGE).coerceAtMost(0F)
            else -> 0F
        }
    }

    fun getLeanRight(tickProgress: Float) = Mth.lerp(tickProgress, lastLeanRight, leanRight)
    fun getLeanForwards(tickProgress: Float) = Mth.lerp(tickProgress, lastLeanForwards, leanForwards)

    override fun getHurtSound(source: DamageSource): SoundEvent = SoundEvents.PLAYER_HURT

    override fun getDeathSound(): SoundEvent = SoundEvents.PLAYER_DEATH

    override fun canStandOnFluid(fluid: FluidState) = fluid.`is`(FluidTags.LAVA) || fluid.`is`(FluidTags.WATER)

    fun teleportEscape(): Boolean {
        val level = level() as? ServerLevel ?: return false

        repeat(TELEPORT_ATTEMPTS) {
            val pos = LandRandomPos.getPos(this, TELEPORT_HORIZONTAL_RANGE, TELEPORT_VERTICAL_RANGE) ?: return@repeat

            val players = level.players()
            if (players.any { !it.isSpectator && it.distanceToSqr(pos) < TELEPORT_MIN_PLAYER_DISTANCE_SQUARED }) return@repeat

            val path = navigation.createPath(BlockPos.containing(pos), 0) ?: return@repeat
            if (!path.canReach()) return@repeat

            teleportTo(pos.x, pos.y, pos.z)
            navigation.stop()

            target?.also { lookAt(it, 360F, 360F) }

            return true
        }

        return false
    }

    fun throwFireball(right: Boolean) {
        val serverLevel = level() as? ServerLevel ?: return
        val existingTarget = target ?: return
        SpellFireballUtil.shoot(
            this,
            existingTarget,
            getFireballStartPos(right),
            { distance -> max(1, (distance * 3).pow(0.8).toInt()) },
            serverLevel,
            scale = 2F,
        )
    }

    private fun getFireballStartPos(right: Boolean): Vec3 {
        val rotRad = yBodyRot * (Math.PI / 180.0)
        return position().add(
            Vec3(
                (if (right) -1.0 else 1.0) * cos(rotRad) - sin(rotRad),
                3.5,
                (if (right) -1.0 else 1.0) * sin(rotRad) + cos(rotRad),
            ).scale(scale.toDouble())
        )
    }

    private fun createFlameParticles(
        pos: Vec3,
        count: Int,
        type: SimpleParticleType,
    ) {
        repeat(count) {
            level().addParticle(
                type,
                pos.x + Random.nextDouble(-0.1, 0.1) * scale,
                pos.y + Random.nextDouble(-0.1, 0.1) * scale,
                pos.z + Random.nextDouble(-0.1, 0.1) * scale,
                Random.nextDouble(-0.1, 0.1) * scale,
                Random.nextDouble(-0.1, 0.1) * scale,
                Random.nextDouble(-0.1, 0.1) * scale,
            )
        }
    }
}