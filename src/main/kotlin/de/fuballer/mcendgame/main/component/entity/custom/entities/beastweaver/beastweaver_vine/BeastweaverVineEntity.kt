package de.fuballer.mcendgame.main.component.entity.custom.entities.beastweaver.beastweaver_vine

import com.geckolib.animatable.GeoAnimatable
import com.geckolib.animatable.GeoEntity
import com.geckolib.animatable.instance.AnimatableInstanceCache
import com.geckolib.animatable.manager.AnimatableManager
import com.geckolib.animation.AnimationController
import com.geckolib.animation.RawAnimation
import com.geckolib.animation.`object`.PlayState
import com.geckolib.util.GeckoLibUtil
import de.fuballer.mcendgame.main.component.entity.custom.CustomEntities
import de.fuballer.mcendgame.main.component.entity.custom.attack.damage.AreaAttackDamage
import net.minecraft.core.particles.BlockParticleOption
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.entity.*
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.monster.Enemy
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import java.util.*
import kotlin.jvm.optionals.getOrDefault
import kotlin.math.atan2
import kotlin.math.max
import kotlin.random.Random

class BeastweaverVineEntity(
    type: EntityType<out BeastweaverVineEntity>,
    level: Level,
) : Mob(type, level), GeoEntity, Enemy, OwnableEntity {
    constructor(level: Level) : this(CustomEntities.BEASTWEAVER_VINE, level)

    companion object {
        const val EMERGE_DURATION_TICKS = 40
        const val DEATH_DURATION_TICKS = 40
        const val ATTACK_DURATION_TICKS = 57
        const val ATTACK_DAMAGE_DELAY = 25

        private const val MIN_MAX_LIFETIME = 500
        private const val MAX_MAX_LIFETIME = 600

        private const val MAX_DEATH_DELAY_AFTER_OWNER_DEATH = ATTACK_DURATION_TICKS + 40

        private const val ATTACK_ANIM_CONTROLLER_ID = "Attack"
        private val SLAM_ATTACK_ANIM: RawAnimation = RawAnimation.begin().thenPlay("attack.slam")
        private const val SLAM_ATTACK_ID = "Slam Attack"
        private val SLAM_ATTACK_AREA = AreaAttackDamage.DamageArea(4.5, 1.5, 1.0, -0.5, 0.0, 0.5)
        private val SLAM_ATTACK_DAMAGE = AreaAttackDamage(
            damageFactor = 1.0f,
            knockbackFactor = 1.0,
            area = SLAM_ATTACK_AREA,
            blockable = true,
        ).setParticles(
            25,
            0.6,
            ParticleTypes.CLOUD,
            0.5,
        ).setSound(
            false,
            SoundEvents.ROOTED_DIRT_BREAK,
            0.85F,
            1.4F,
        )

        fun createAttributes(): AttributeSupplier.Builder {
            return createLivingAttributes()
                .add(Attributes.FOLLOW_RANGE, 4.0)
                .add(Attributes.MOVEMENT_SPEED, 0.0)
                .add(Attributes.ATTACK_DAMAGE, 4.0)
                .add(Attributes.ATTACK_KNOCKBACK, 1.0)
                .add(Attributes.ARMOR, 0.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0)
        }

        private const val LIFETIME_ID = "lifetime"

        private const val OWNER_DATA_ID = "owner"
        private val OWNER_DATA = SynchedEntityData.defineId(BeastweaverVineEntity::class.java, EntityDataSerializers.OPTIONAL_LIVING_ENTITY_REFERENCE)

        private const val EMERGING_TICKS_DATA_ID = "emerging_ticks"
        val EMERGING_TICKS_DATA = SynchedEntityData.defineId(BeastweaverVineEntity::class.java, EntityDataSerializers.INT)

        val RAD_ROTATION_TO_TARGET_DATA = SynchedEntityData.defineId(BeastweaverVineEntity::class.java, EntityDataSerializers.FLOAT)
    }

    private val maxLifetime = Random.nextInt(MIN_MAX_LIFETIME, MAX_MAX_LIFETIME)
    private var lifetime = 0

    private var attackTime = -1
    var emergingTicksClient = 0
    private var delayUntilStartDeath = -1

    val swayData = BeastweaverVineSwayData()

    private val cache: AnimatableInstanceCache = GeckoLibUtil.createInstanceCache(this)
    override fun getAnimatableInstanceCache() = cache

    override fun defineSynchedData(entityData: SynchedEntityData.Builder) {
        super.defineSynchedData(entityData)
        entityData.define(OWNER_DATA, Optional.empty())
        entityData.define(EMERGING_TICKS_DATA, 0)
        entityData.define(RAD_ROTATION_TO_TARGET_DATA, 0F)
    }

    override fun onSyncedDataUpdated(accessor: EntityDataAccessor<*>) {
        super.onSyncedDataUpdated(accessor)
        if (accessor == EMERGING_TICKS_DATA) emergingTicksClient = max(emergingTicksClient, entityData.get(EMERGING_TICKS_DATA))
    }

    private val attackAnimationController =
        AnimationController<GeoAnimatable>(ATTACK_ANIM_CONTROLLER_ID, 0) { _ -> PlayState.STOP }
            .triggerableAnim(SLAM_ATTACK_ID, SLAM_ATTACK_ANIM)

    override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {
        controllers.add(attackAnimationController)
    }

    fun isPlayingAttackAnimation() = attackAnimationController.isPlayingTriggeredAnimation

    fun getCurrentAttackAnimTime(tickProgress: Float) = attackAnimationController.currentAnimationTime.toFloat() + tickProgress / 20F

    override fun registerGoals() {
        targetSelector.addGoal(0, BeastweaverVineNearestAttackableTargetGoal(this, Player::class.java))
    }

    override fun baseTick() {
        super.baseTick()

        tickEmerging()

        val level = level() as? ServerLevel ?: return
        tickShouldDie()
        tickAttack(level)
    }

    private fun tickEmerging() {
        val level = level()
        if (level is ServerLevel) tickEmergingServer()
        else tickEmergingClient(level)
    }

    private fun tickEmergingServer() {
        val emergeTicks = entityData.get(EMERGING_TICKS_DATA)
        if (emergeTicks > EMERGE_DURATION_TICKS) return
        entityData.set(EMERGING_TICKS_DATA, emergeTicks + 1)
    }

    private fun tickEmergingClient(level: Level) {
        if (emergingTicksClient > EMERGE_DURATION_TICKS) return
        playEmergeAndDeathEffects(level, emergingTicksClient)
        emergingTicksClient++
    }

    private fun tickShouldDie() {
        if (health <= 0F) return

        if (++lifetime > maxLifetime || delayUntilStartDeath == 0) health = 0F
        else if (delayUntilStartDeath > 0) delayUntilStartDeath--
        else if (owner?.isAlive != true) delayUntilStartDeath = Random.nextInt(ATTACK_DURATION_TICKS, MAX_DEATH_DELAY_AFTER_OWNER_DEATH)
    }

    override fun tickDeath() {
        deathTime++
        val level = level()
        if (level.isClientSide) {
            playEmergeAndDeathEffects(level, deathTime)
            return
        }

        if (deathTime >= DEATH_DURATION_TICKS && !isRemoved) remove(RemovalReason.KILLED)
    }

    private fun playEmergeAndDeathEffects(
        level: Level,
        progressTicks: Int,
    ) {
        val pos = blockPosition().below()
        val state = level.getBlockState(pos)
        level.addParticle(
            BlockParticleOption(ParticleTypes.BLOCK, state),
            x,
            y + 0.1,
            z,
            0.0,
            0.1,
            0.0
        )
        if (progressTicks % 4 == 0) {
            level.playLocalSound(
                x,
                y,
                z,
                state.soundType.hitSound,
                SoundSource.BLOCKS,
                0.75F,
                0.9F + 0.2F * random.nextFloat(),
                false
            )
        }
    }

    private fun tickAttack(level: ServerLevel) {
        if (entityData.get(EMERGING_TICKS_DATA) < EMERGE_DURATION_TICKS) return

        if (attackTime >= 0) {
            attackTime++
            if (attackTime > ATTACK_DURATION_TICKS) {
                attackTime = -1
                return
            }

            if (attackTime == ATTACK_DAMAGE_DELAY) dealAttackDamage(level)
        } else if (target?.isAlive == true && delayUntilStartDeath < 0 && lifetime + ATTACK_DURATION_TICKS <= maxLifetime) {
            attackTime = 0
            triggerAnim(ATTACK_ANIM_CONTROLLER_ID, SLAM_ATTACK_ID)

            val toTargetHorizontal = target!!.position().subtract(position()).horizontal().normalize()
            val facingHorizontal = lookAngle.horizontal().normalize()

            val cross = facingHorizontal.x * toTargetHorizontal.z - facingHorizontal.z * toTargetHorizontal.x
            val angleRad = atan2(cross, facingHorizontal.dot(toTargetHorizontal))

            entityData.set(RAD_ROTATION_TO_TARGET_DATA, angleRad.toFloat())
        }
    }

    private fun dealAttackDamage(level: ServerLevel) {
        SLAM_ATTACK_DAMAGE.applyRotated(level, this, -entityData.get(RAD_ROTATION_TO_TARGET_DATA))
    }

    fun setOwner(owner: LivingEntity) {
        val reference = EntityReference.of(owner)
        setOwnerReference(reference)
    }

    fun setOwnerReference(owner: EntityReference<LivingEntity>?) {
        entityData.set(OWNER_DATA, Optional.ofNullable(owner))
    }

    override fun getOwnerReference(): EntityReference<LivingEntity>? = entityData.get(OWNER_DATA).orElse(null)

    override fun isPushable() = false

    override fun isInvulnerableTo(level: ServerLevel, source: DamageSource): Boolean {
        if (source.`is`(DamageTypes.GENERIC_KILL)) return false
        if (source.`is`(DamageTypes.FELL_OUT_OF_WORLD)) return false
        if (source.`is`(DamageTypes.OUTSIDE_BORDER)) return false
        return true
    }

    override fun addAdditionalSaveData(output: ValueOutput) {
        super.addAdditionalSaveData(output)

        EntityReference.store(ownerReference, output, OWNER_DATA_ID)

        output.putInt(LIFETIME_ID, lifetime)
        output.putInt(EMERGING_TICKS_DATA_ID, entityData.get(EMERGING_TICKS_DATA))
    }

    override fun readAdditionalSaveData(input: ValueInput) {
        super.readAdditionalSaveData(input)

        val owner = EntityReference.readWithOldOwnerConversion<LivingEntity>(input, OWNER_DATA_ID, level())
        if (owner == null) entityData.set(OWNER_DATA, Optional.empty())
        else entityData.set(OWNER_DATA, Optional.of(owner))

        lifetime = input.getInt(LIFETIME_ID).getOrDefault(0)
        entityData.set(EMERGING_TICKS_DATA, input.getInt(EMERGING_TICKS_DATA_ID).getOrDefault(0))
    }
}