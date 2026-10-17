package de.fuballer.mcendgame.main.component.entity.custom.entities.blood_king

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
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.AttackAnimationData
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.DelayedAttackDataInstance
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.DelayedDamageData
import de.fuballer.mcendgame.main.component.entity.custom.attack.trigger_condition.DistanceTriggerCondition
import de.fuballer.mcendgame.main.component.entity.custom.goals.*
import de.fuballer.mcendgame.main.component.entity.custom.interfaces.BlockAbleMovementMob
import de.fuballer.mcendgame.main.component.entity.custom.interfaces.CustomAttacksMob
import de.fuballer.mcendgame.main.component.entity.custom.interfaces.DisableAbleGoalsMob
import de.fuballer.mcendgame.main.component.particle.DirectionalFlameSweepParticleEffect
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
import net.minecraft.world.phys.Vec3
import kotlin.math.cos
import kotlin.math.sin

class BloodKingEntity(
    type: EntityType<out BloodKingEntity>,
    world: Level,
) : PathfinderMob(type, world),
    GeoEntity,
    Enemy,
    CustomAttacksMob<BloodKingEntity>,
    DisableAbleGoalsMob,
    BlockAbleMovementMob<BloodKingEntity> {
    companion object {
        private const val ATTACK_ANIM_CONTROLLER_ID = "Attack"

        private val SLASH_AREA = AreaAttackDamage.DamageArea(3.25, 1.6, 1.75, -0.1, 0.35, 0.75)
        private val SLASH_ATTACK_DAMAGE = AreaAttackDamage(0.75F, 1.0, SLASH_AREA)
        private val SLASH_ANIM: RawAnimation = RawAnimation.begin().thenPlay("attack.slash")
        private const val SLASH_ID = "Slash"
        private val SLASH_ANIM_DATA = AttackAnimationData(AttackPose.DEFAULT, AttackPose.DEFAULT, ATTACK_ANIM_CONTROLLER_ID, SLASH_ID)
        private val SLASH_ATTACK =
            Attack<BloodKingEntity>(
                SLASH_ID,
                SLASH_ANIM_DATA,
                totalDuration = 35,
                cooldown = 0,
                DistanceTriggerCondition(3.0, affectedByScale = true),
                data = listOf(
                    DelayedDamageData(SLASH_ATTACK_DAMAGE, 12),
                ),
            )

        private val STAB_AREA = AreaAttackDamage.DamageArea(3.25, 1.6, 1.75, -0.1, 0.35, 0.75)
        private val STAB_ATTACK_DAMAGE = AreaAttackDamage(0.75F, 1.0, STAB_AREA)
        private val STAB_ANIM: RawAnimation = RawAnimation.begin().thenPlay("attack.stab")
        private const val STAB_ID = "Stab"
        private val STAB_ANIM_DATA = AttackAnimationData(AttackPose.DEFAULT, AttackPose.DEFAULT, ATTACK_ANIM_CONTROLLER_ID, STAB_ID)
        private val STAB_ATTACK =
            Attack<BloodKingEntity>(
                STAB_ID,
                STAB_ANIM_DATA,
                totalDuration = 35,
                cooldown = 0,
                DistanceTriggerCondition(4.0, affectedByScale = true),
                data = listOf(
                    DelayedDamageData(STAB_ATTACK_DAMAGE, 12),
                ),
            )

        private val ATTACKS: List<RandomOption<out Attack<BloodKingEntity>>> = listOf(
            RandomOption(1, SLASH_ATTACK),
            RandomOption(1, STAB_ATTACK),
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
    override val attackCooldowns: MutableMap<Attack<BloodKingEntity>, Int> = mutableMapOf()
    override val attackDataInstances = mutableListOf<DelayedAttackDataInstance>()

    private val cache: AnimatableInstanceCache = GeckoLibUtil.createInstanceCache(this)
    override fun getAnimatableInstanceCache() = cache

    private val attackAnimationController =
        AnimationController<GeoAnimatable>(ATTACK_ANIM_CONTROLLER_ID, 0) { _ -> PlayState.STOP }
            .triggerableAnim(SLASH_ID, SLASH_ANIM)
            .triggerableAnim(STAB_ID, STAB_ANIM)
            .setParticleKeyframeHandler { event ->
                if (!level().isClientSide) return@setParticleKeyframeHandler

                val keyFrameData = event.keyframeData
                if (keyFrameData.effect == "sword_flame_sweep") {
                    val pos = GeoEntityLocatorService.getLocatorPos(uuid, keyFrameData.locatorName) ?: return@setParticleKeyframeHandler
                    val swordPos0 = GeoEntityLocatorService.getLocatorPos(uuid, "swordParticle0") ?: return@setParticleKeyframeHandler
                    val swordPos1 = GeoEntityLocatorService.getLocatorPos(uuid, "swordParticle1") ?: return@setParticleKeyframeHandler

                    val dir = swordPos1.subtract(swordPos0)
                    val axis = Vec3(0.0, 1.0, 0.0).cross(dir).normalize()
                    val angle = Math.toRadians(20.0)
                    val rotatedDir = dir
                        .scale(cos(angle))
                        .add(axis.cross(dir).scale(sin(angle)))

                    level().addParticle(
                        DirectionalFlameSweepParticleEffect(1.5 * scale, rotatedDir.x, rotatedDir.y, rotatedDir.z),
                        pos.x, pos.y - 0.5 * scale, pos.z,
                        0.0, 0.0, 0.0,
                    )
                }
            }
            .setSoundKeyframeHandler { event ->
                val keyFrameData = event.keyframeData

                val level = level()
                when (keyFrameData.sound) {
                    "breeze_land" -> level.playLocalSound(this, SoundEvents.BREEZE_LAND, SoundSource.HOSTILE, 1.0f, 0.75f)
                    "blaze_burn" -> level.playLocalSound(this, SoundEvents.BLAZE_BURN, SoundSource.HOSTILE, 0.9f, 1.1f)
                    "player_attack_sweep" -> level.playLocalSound(this, SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.HOSTILE, 1.0f, 1.0f)
                    "firecharge_use" -> level.playLocalSound(this, SoundEvents.FIRECHARGE_USE, SoundSource.HOSTILE, 0.6f, 1.0f)
                }
            }

    override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {
        controllers.add(
            AnimationController<BloodKingEntity>("Walk/Idle", 5)
            { test ->
                test.setAndContinue(if (test.isMoving) DefaultAnimations.WALK else DefaultAnimations.IDLE)
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
}