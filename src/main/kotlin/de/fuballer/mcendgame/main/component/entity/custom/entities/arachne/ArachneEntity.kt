package de.fuballer.mcendgame.main.component.entity.custom.entities.arachne

import de.fuballer.mcendgame.main.component.block.CustomBlocks
import de.fuballer.mcendgame.main.component.damage.dealing.DamageDealingExtension.dealGenericAttackDamage
import de.fuballer.mcendgame.main.component.entity.custom.CustomEntities
import de.fuballer.mcendgame.main.component.entity.custom.entities.mount.DirectionalMovementEntity
import de.fuballer.mcendgame.main.component.entity.custom.entities.webhook.WebhookEntity
import de.fuballer.mcendgame.main.component.entity.custom.entities.webshot.WebshotEntity
import de.fuballer.mcendgame.main.component.entity.custom.goals.*
import de.fuballer.mcendgame.main.component.entity.custom.interfaces.CustomPosesEntity
import de.fuballer.mcendgame.main.component.entity.custom.interfaces.HookAttackMob
import de.fuballer.mcendgame.main.component.entity.custom.interfaces.MeleeAttackMob
import de.fuballer.mcendgame.main.util.extension.EntityExtension.setAndSyncVelocity
import de.fuballer.mcendgame.main.util.extension.EntityExtension.setShieldsCooldown
import de.fuballer.mcendgame.main.util.extension.Vec3dExtension.horizontal
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.setWebbed
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.AnimationState
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.RangedAttackMob
import net.minecraft.entity.ai.goal.ActiveTargetGoal
import net.minecraft.entity.ai.goal.RevengeGoal
import net.minecraft.entity.ai.goal.SwimGoal
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.mob.Monster
import net.minecraft.entity.passive.VillagerEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import java.util.*
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

class ArachneEntity(
    type: EntityType<out ArachneEntity>,
    world: World,
) : DirectionalMovementEntity(type, world), Monster, RangedAttackMob, HookAttackMob, MeleeAttackMob {
    private var attackAnimationTicks = 0
    val spitAnimationState = AnimationState()
    val attackAnimationState = AnimationState()

    override val hooker = this
    override val hookPullCount = 3
    override val hookPullInterval = 15
    override val hookPullStrength = 1.0
    override val hookPullAdditionalY = 0.2
    override val hookedEntityUuidMap = mutableMapOf<UUID, Pair<Int, Int>>()
    override val hookedEntityIds = mutableListOf<Int>()

    private var isCurrentlyRanged = true
    private var meleeTicks = 0
    private var disabledMovementTicks = 0
    private var dealAttackDamageDelay = 0

    private val stayInMeleeRangeGoal = StayInRangeGoal(this, 1.0, MELEE_PURSUE_DISTANCE)
    private val meleeAttackGoal = NoMovementMeleeAttackGoal(this, 35, MELEE_ATTACK_RANGE, 20)

    private val hookAttackGoal = HookAttackGoal(this, 100, 15F)
    private val projectileAttackGoal = NoMovementProjectileAttackGoal(this, 35, 15F, 15)
    private val rangedKeepDistanceGoal = KeepDistanceToTargetGoal(this, 1.0, 10F, 15F)

    private val wanderGoal = DisableAbleWanderAroundFarGoal(this, 1.0)
    private val lookAtPlayerGoal = DisableAbleLookAtEntityGoal(this, PlayerEntity::class.java, 8.0f)
    private val lookAroundGoal = DisableAbleLookAroundGoal(this)

    private var previousScale = 0F
    private var maxStayMeleeRangeSquared = MAX_STAY_MELEE_RANGE * MAX_STAY_MELEE_RANGE

    companion object {
        private const val MAX_STAY_MELEE_RANGE = 10.0
        const val MIN_MELEE_TICKS = 100
        const val RANDOM_STOP_MELEE_PROBABILITY = 0.002 // per tick
        private const val MELEE_SHIELD_DISABLE_TIME = 5f // seconds

        const val MELEE_PURSUE_DISTANCE = 2.8
        const val MELEE_ATTACK_RANGE = 3.3
        const val MELEE_ATTACK_LENGTH = 4.0
        const val MELEE_ATTACK_WIDTH = 3.0
        const val MELEE_ATTACK_HEIGHT = 3

        fun createAttributes(): DefaultAttributeContainer.Builder {
            return createLivingAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 35.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.4)
                .add(EntityAttributes.GENERIC_STEP_HEIGHT, 1.1)
                .add(EntityAttributes.GENERIC_SAFE_FALL_DISTANCE, 10.0)
                .add(EntityAttributes.GENERIC_FALL_DAMAGE_MULTIPLIER, 0.2)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.5)
                .add(EntityAttributes.GENERIC_ARMOR, 0.0)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.8)
                .add(EntityAttributes.GENERIC_MOVEMENT_EFFICIENCY, 0.85)
        }

        val ATTACK_POSE: TrackedData<CustomPosesEntity.CustomPose> =
            DataTracker.registerData(ArachneEntity::class.java, CustomPosesEntity.CUSTOM_POSE_TDH)
    }

    init {
        initDynamicGoals()
    }

    private fun initDynamicGoals() {
        goalSelector.add(2, hookAttackGoal)
        goalSelector.add(3, projectileAttackGoal)
        goalSelector.add(3, meleeAttackGoal)
        goalSelector.add(4, rangedKeepDistanceGoal)
        goalSelector.add(4, stayInMeleeRangeGoal)
        goalSelector.add(5, wanderGoal)
        goalSelector.add(6, lookAtPlayerGoal)
        goalSelector.add(7, lookAroundGoal)

        updateGoals()
    }

    override fun initGoals() {
        goalSelector.add(0, SwimGoal(this))
        goalSelector.add(1, ChangeTargetGoal(this, probability = 0.4, tryIntervalTicks = 20, 100, { it is PlayerEntity || it is VillagerEntity }))

        targetSelector.add(0, RevengeGoal(this))
        targetSelector.add(1, ActiveTargetGoal(this, PlayerEntity::class.java, true))
        targetSelector.add(2, ActiveTargetGoal(this, VillagerEntity::class.java, true))
    }

    private fun updateGoals() {
        val movementDisabled = isMovementDisabled()

        meleeAttackGoal.isDisabled = isCurrentlyRanged
        stayInMeleeRangeGoal.isDisabled = isCurrentlyRanged || movementDisabled

        hookAttackGoal.isDisabled = !isCurrentlyRanged
        projectileAttackGoal.isDisabled = !isCurrentlyRanged
        rangedKeepDistanceGoal.isDisabled = !isCurrentlyRanged || movementDisabled

        wanderGoal.isDisabled = movementDisabled
        lookAtPlayerGoal.isDisabled = movementDisabled
        lookAroundGoal.isDisabled = movementDisabled
    }

    override fun initDataTracker(builder: DataTracker.Builder) {
        super.initDataTracker(builder)
        builder.add(ATTACK_POSE, CustomPosesEntity.CustomPose.IDLING)
    }

    override fun onTrackedDataSet(data: TrackedData<*>) {
        if (data == ATTACK_POSE) {
            when (dataTracker.get(ATTACK_POSE)) {
                CustomPosesEntity.CustomPose.SPITTING -> {
                    spitAnimationState.start(age)
                }

                CustomPosesEntity.CustomPose.MELEE_ATTACKING -> {
                    attackAnimationState.start(age)
                }

                else -> {}
            }
        }
        super.onTrackedDataSet(data)
    }

    override fun tick() {
        super.tick()

        if (entityWorld.isClient) return
        tickChangeToRanged()
        updateAttackPose()
        updateBlockMovementTicks()
        tickHooks()
        tickDealAttackDamage()
        tickScaleUpdate()
    }

    private fun tickScaleUpdate() {
        if (scale == previousScale) return
        previousScale = scale

        meleeAttackGoal.setRange(MELEE_ATTACK_RANGE * scale)
        stayInMeleeRangeGoal.setMaxDistance(MELEE_PURSUE_DISTANCE * scale)

        maxStayMeleeRangeSquared = (MAX_STAY_MELEE_RANGE * scale).pow(2)
    }

    private fun tickDealAttackDamage() {
        if (dealAttackDamageDelay <= 0) return
        if (--dealAttackDamageDelay > 0) return
        dealAttackDamage()
    }

    private fun tickChangeToRanged() {
        if (!shouldChangeToRanged()) return
        isCurrentlyRanged = true
        updateGoals()
    }

    private fun shouldChangeToRanged(): Boolean {
        if (isCurrentlyRanged) return false
        if (++meleeTicks <= MIN_MELEE_TICKS) return false
        if (isMovementDisabled()) return false

        val livingTarget = target ?: return true
        if (livingTarget.isDead) return true

        if (squaredDistanceTo(livingTarget) > maxStayMeleeRangeSquared) return true

        return random.nextDouble() < RANDOM_STOP_MELEE_PROBABILITY
    }

    private fun blockMovement(ticks: Int) {
        if (ticks <= 0) return
        val oldTicks = disabledMovementTicks
        disabledMovementTicks = ticks
        if (oldTicks > 0) return

        updateGoals()
        navigation.stop()
    }

    private fun updateBlockMovementTicks() {
        if (disabledMovementTicks <= 0) return
        moveControl.strafeTo(0F, 0F)
        if (--disabledMovementTicks > 0) return
        updateGoals()
    }

    private fun updateAttackPose() {
        if (entityWorld.isClient) return
        if (attackAnimationTicks <= 0) return
        if (--attackAnimationTicks > 0) return

        dataTracker.set(
            ATTACK_POSE, CustomPosesEntity.CustomPose.IDLING
        )
    }

    private fun changeAttackPose(pose: CustomPosesEntity.CustomPose, animationTime: Int) {
        if (attackAnimationTicks > 0) return
        dataTracker.set(ATTACK_POSE, pose)
        attackAnimationTicks = animationTime
    }

    override fun startMovementAnimation(animationState: AnimationState) {
        if (animationState == walkLeftAnimationState || animationState == walkRightAnimationState)
            return super.startMovementAnimation(walkAnimationState)

        super.startMovementAnimation(animationState)
    }

    private fun shootAt(
        target: LivingEntity,
        projectile: ProjectileEntity,
    ) {
        val serverWorld = entityWorld as? ServerWorld ?: return

        val xDistance = target.x - x
        val zDistance = target.z - z
        val aimY = target.eyeY - 1.1f
        val addedYVelocity = sqrt(xDistance * xDistance + zDistance * zDistance) * 0.2f

        projectile.setVelocity(
            xDistance,
            aimY - projectile.y + addedYVelocity,
            zDistance,
            1.6f,
            2.0f,
        )
        serverWorld.spawnEntity(projectile)

        changeAttackPose(CustomPosesEntity.CustomPose.SPITTING, 9)// anim is 0.42s
        playSpitSound()
    }

    override fun shootAt(
        target: LivingEntity,
        pullProgress: Float,
    ) {
        val serverWorld = entityWorld as? ServerWorld ?: return
        val projectile = WebshotEntity(CustomEntities.WEBSHOT, serverWorld, this)
        projectile.setDamage(getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) / 2.0)
        shootAt(target, projectile)
    }

    override fun shootHookAt(
        target: LivingEntity,
    ) {
        val serverWorld = entityWorld as? ServerWorld ?: return
        val projectile = WebhookEntity(CustomEntities.WEBHOOK, serverWorld, this)
        projectile.setDamage(1.0)
        shootAt(target, projectile)
        addHookedEntity(projectile.uuid)
    }

    override fun slowMovement(
        state: BlockState,
        multiplier: Vec3d
    ) {
        onLanding()
        if (state.isOf(Blocks.COBWEB)) return
        if (state.isOf(CustomBlocks.DECAYING_COBWEB)) return
        movementMultiplier = multiplier
    }

    override fun handleFallDamage(fallDistance: Float, damageMultiplier: Float, damageSource: DamageSource) = false

    override fun occludeVibrationSignals() = true

    override fun getAmbientSound(): SoundEvent {
        return SoundEvents.ENTITY_SPIDER_AMBIENT
    }

    override fun getDeathSound(): SoundEvent {
        return SoundEvents.ENTITY_SPIDER_DEATH
    }

    override fun getHurtSound(source: DamageSource): SoundEvent {
        return SoundEvents.ENTITY_SPIDER_HURT
    }

    private fun playSpitSound() {
        playSound(SoundEvents.ENTITY_SPIDER_AMBIENT, 1.0f, 0.75F + random.nextFloat() * 0.25F)
    }

    override fun playStepSound(
        pos: BlockPos,
        state: BlockState
    ) {
        if (!state.fluidState.isEmpty) return

        val blockState = entityWorld.getBlockState(pos.up())
        val blockSoundGroup = if (blockState.isOf(Blocks.SNOW)) blockState.soundGroup else state.soundGroup

        playSound(SoundEvents.ENTITY_SPIDER_STEP, blockSoundGroup.getVolume() * 0.15f, blockSoundGroup.getPitch())
    }

    override fun getLeashOffset() = Vec3d(0.0, standingEyeHeight * 0.9, width * 0.4)

    override fun addHookedEntity(hookedUuid: UUID) {
        super.addHookedEntity(hookedUuid)

        val world = entityWorld as? ServerWorld ?: return
        val entity = world.getEntity(hookedUuid) as? LivingEntity ?: return

        triggerMeleeOnHook(entity)

        entity.setWebbed()
    }

    private fun triggerMeleeOnHook(hookedEntity: LivingEntity) {
        if (target != hookedEntity && hookedEntity !is PlayerEntity) return
        target = hookedEntity

        isCurrentlyRanged = false
        meleeTicks = 0
        updateGoals()
    }

    override fun removeHookedEntity(hookedUuid: UUID) {
        super.removeHookedEntity(hookedUuid)

        val world = entityWorld as? ServerWorld ?: return
        val entity = world.getEntity(hookedUuid) ?: return

        if (entity !is LivingEntity) return
        entity.setWebbed(false)
    }

    override fun onDeath(damageSource: DamageSource?) {
        super.onDeath(damageSource)

        val serverWorld = entityWorld as? ServerWorld ?: return
        hookedEntityUuidMap.keys.forEach { uuid ->
            val entity = serverWorld.getEntity(uuid) as? LivingEntity ?: return@forEach
            entity.setWebbed(false)
        }
    }

    override fun meleeAttack(target: LivingEntity) {
        changeAttackPose(CustomPosesEntity.CustomPose.MELEE_ATTACKING, 28)
        blockMovement(15)
        dealAttackDamageDelay = 7
        lookControl.lookAt(target)
        lookAtEntity(target, 180F, 180F)
        bodyYaw = yaw
    }

    private fun dealAttackDamage() {
        val serverWorld = entityWorld as? ServerWorld ?: return

        var targets = serverWorld.getEntitiesByClass(
            LivingEntity::class.java,
            boundingBox.expand(MELEE_ATTACK_LENGTH * scale)
        ) { it != this }

        val forward = getRotationVector(pitch, bodyYaw).horizontal().normalize()
        val sideways = forward.crossProduct(Vec3d(0.0, 1.0, 0.0))
        targets = targets.filter {
            isInAttackArea(it.pos.subtract(pos), forward, sideways)
                    || isInAttackArea(it.eyePos.subtract(pos), forward, sideways)
        }

        val damage = getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE).toFloat()
        val knockBackDirection = getRotationVector(pitch, bodyYaw).horizontal().normalize()
        val knockBackStrength = getAttributeValue(EntityAttributes.GENERIC_ATTACK_KNOCKBACK) * getAttributeValue(EntityAttributes.GENERIC_SCALE)

        targets.forEach {
            it.dealGenericAttackDamage(damage, this)
            if (entityWorld is ServerWorld && it is PlayerEntity && it.isBlocking) it.setShieldsCooldown(MELEE_SHIELD_DISABLE_TIME)

            it.setAndSyncVelocity(knockBackDirection.multiply(knockBackStrength))
        }
    }

    private fun isInAttackArea(
        relativePos: Vec3d,
        forward: Vec3d,
        sideways: Vec3d
    ): Boolean {
        val forwardDistance = relativePos.dotProduct(forward)
        val sidewaysDistance = relativePos.dotProduct(sideways)
        val heightDistance = relativePos.y

        val scale = getAttributeValue(EntityAttributes.GENERIC_SCALE)
        if (forwardDistance < width * scale * 0.2 || forwardDistance > MELEE_ATTACK_LENGTH * scale) return false
        if (abs(sidewaysDistance) > MELEE_ATTACK_WIDTH / 2.0 * scale) return false
        if (abs(heightDistance) > MELEE_ATTACK_HEIGHT / 2.0 * scale) return false
        return true
    }

    private fun isMovementDisabled() = disabledMovementTicks > 0

    override fun updateMovementPose() {
        if (!isMovementDisabled()) return super.updateMovementPose()

        if (dataTracker.get(MOVEMENT_POSE) == CustomPosesEntity.CustomPose.IDLING) return
        dataTracker.set(MOVEMENT_POSE, CustomPosesEntity.CustomPose.IDLING)
    }

    override fun canHaveStatusEffect(effect: StatusEffectInstance): Boolean {
        if (effect.effectType == StatusEffects.SLOWNESS) return false
        return super.canHaveStatusEffect(effect)
    }
}