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
import de.fuballer.mcendgame.main.component.entity.custom.attack.Attack
import de.fuballer.mcendgame.main.component.entity.custom.attack.AttackPose
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.AttackAnimationData
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.DelayedAttackData
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.DelayedAttackDataInstance
import de.fuballer.mcendgame.main.component.entity.custom.attack.trigger_condition.DistanceTriggerCondition
import de.fuballer.mcendgame.main.component.entity.custom.entities.magma_spike.MagmaSpikeEntity
import de.fuballer.mcendgame.main.component.entity.custom.goals.*
import de.fuballer.mcendgame.main.component.entity.custom.interfaces.BlockAbleMovementMob
import de.fuballer.mcendgame.main.component.entity.custom.interfaces.CustomAttacksMob
import de.fuballer.mcendgame.main.component.entity.custom.interfaces.DisableAbleGoalsMob
import de.fuballer.mcendgame.main.util.random.RandomOption
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.util.Mth
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
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
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.phys.Vec3
import kotlin.math.max
import kotlin.math.min

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
        private const val ATTACK_ANIM_CONTROLLER_ID = "Attack"

        private val MAGMA_SPIKES_LINE_ANIM: RawAnimation = RawAnimation.begin().thenPlay("misc.idle")
        private const val MAGMA_SPIKES_LINE_ID = "Magma Spikes Line"
        private val MAGMA_SPIKES_LINE_ANIM_DATA = AttackAnimationData(AttackPose.DEFAULT, AttackPose.DEFAULT, ATTACK_ANIM_CONTROLLER_ID, MAGMA_SPIKES_LINE_ID)
        private val MAGMA_SPIKES_LINE_ATTACK =
            Attack<BloodQueenEntity>(
                MAGMA_SPIKES_LINE_ID,
                MAGMA_SPIKES_LINE_ANIM_DATA,
                totalDuration = 35,
                cooldown = 0,
                DistanceTriggerCondition(5.0, 15.0, affectedByScale = true),
                data = listOf(
                    object : DelayedAttackData(5) {
                        override fun apply(level: ServerLevel, entity: Mob, target: LivingEntity?) {
                            (entity as? BloodQueenEntity)?.castMagmaSpikesLine()
                        }
                    },
                ),
            )

        private val ATTACKS: List<RandomOption<out Attack<BloodQueenEntity>>> = listOf(
            RandomOption(1, MAGMA_SPIKES_LINE_ATTACK),
        )

        fun createAttributes(): AttributeSupplier.Builder {
            return createLivingAttributes()
                .add(Attributes.FOLLOW_RANGE, 64.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ATTACK_DAMAGE, 3.0)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5)
                .add(Attributes.ARMOR, 0.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8)
                .add(Attributes.MOVEMENT_EFFICIENCY, 0.85)
                .add(Attributes.SAFE_FALL_DISTANCE, 10.0)
                .add(Attributes.FALL_DAMAGE_MULTIPLIER, 0.1)
                .add(Attributes.WATER_MOVEMENT_EFFICIENCY, 0.65)
        }
    }

    override var blockAbleMovementEntity = this
    override var blockedMovementTicks = 0
    override var blockedMovementAirborne = false

    override var attackPose = AttackPose.DEFAULT
    override var attackDuration = 0
    override val attacks = ATTACKS
    override val attackCooldowns: MutableMap<Attack<BloodQueenEntity>, Int> = mutableMapOf()
    override val attackDataInstances = mutableListOf<DelayedAttackDataInstance>()

    private val cache: AnimatableInstanceCache = GeckoLibUtil.createInstanceCache(this)
    override fun getAnimatableInstanceCache() = cache

    private val attackAnimationController =
        AnimationController<GeoAnimatable>(ATTACK_ANIM_CONTROLLER_ID, 0) { _ -> PlayState.STOP }
            .triggerableAnim(MAGMA_SPIKES_LINE_ID, MAGMA_SPIKES_LINE_ANIM)

    override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {
        controllers.add(
            AnimationController<BloodQueenEntity>("Walk/Idle", 5)
            { test ->
                test.setAndContinue(if (test.isMoving) DefaultAnimations.IDLE else DefaultAnimations.IDLE)
            },

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
        stayInMeleeRangeGoal.isDisabled = movementBlocked
        wanderGoal.isDisabled = movementBlocked
        lookAtPlayerGoal.isDisabled = movementBlocked
        lookAroundGoal.isDisabled = movementBlocked
    }

    override fun tick() {
        super.tick()

        val level = level() as? ServerLevel ?: return
        tickBlockedMovement()
        tickAttacks(level, this)
    }

    override fun getHurtSound(source: DamageSource): SoundEvent = SoundEvents.PLAYER_HURT

    override fun getDeathSound(): SoundEvent = SoundEvents.PLAYER_DEATH

    override fun isInvulnerableTo(level: ServerLevel, source: DamageSource): Boolean {
        if (source.`is`(DamageTypes.HOT_FLOOR)) return true
        return super.isInvulnerableTo(level, source)
    }

    fun castMagmaSpikesLine() {
        val target = this.target ?: return
        val minY = min(target.y, y)
        val maxY = max(target.y, y) + 1.0
        val angleToTarget = Mth.atan2(target.z - z, target.x - x).toFloat()

        val spikesCount = 15 // TODO
        for (i in 0..spikesCount) {
            val reach = 1.25 * (i + 1)
            val spellSpeed = 1 * i
            createMagmaSpikeEntity(
                x + Mth.cos(angleToTarget.toDouble()) * reach,
                z + Mth.sin(angleToTarget.toDouble()) * reach,
                minY,
                maxY,
                angleToTarget - Mth.HALF_PI,
                Math.toRadians(45.0 - (45.0 * i / spikesCount)).toFloat(),
                0.75f + (0.5f * i / spikesCount),
                spellSpeed,
            )
        }
    }

    private fun createMagmaSpikeEntity(
        x: Double,
        z: Double,
        minY: Double,
        maxY: Double,
        yRotRad: Float,
        xzRotRad: Float,
        scale: Float,
        delayTicks: Int,
    ) {
        var pos = BlockPos.containing(x, maxY, z)
        var success = false
        var topOffset = 0.0
        do {
            val below = pos.below()
            val belowState: BlockState = level().getBlockState(below)
            if (!belowState.isFaceSturdy(level(), below, Direction.UP)) {
                pos = pos.below()
                continue
            }

            if (level().isEmptyBlock(pos)) {
                success = true
                break
            }

            val blockState: BlockState = level().getBlockState(pos)
            val shape = blockState.getCollisionShape(level(), pos)
            if (!shape.isEmpty) topOffset = shape.max(Direction.Axis.Y)
        } while (pos.y >= Mth.floor(minY) - 1)

        if (success) {
            level().addFreshEntity(MagmaSpikeEntity(level(), x, pos.y + topOffset, z, yRotRad, xzRotRad, scale, delayTicks, this))
            level().gameEvent(GameEvent.ENTITY_PLACE, Vec3(x, pos.y + topOffset, z), GameEvent.Context.of(this))
        }
    }
}