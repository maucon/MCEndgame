package de.fuballer.mcendgame.main.component.entity.custom.entities.arachne

import de.fuballer.mcendgame.main.component.block.CustomBlocks
import de.fuballer.mcendgame.main.component.custom_attribute.effects.projectile.AdditionalProjectilesUtil
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
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.setWebbed
import net.minecraft.core.BlockPos
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.AnimationState
import net.minecraft.world.entity.Avatar
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.FloatGoal
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.monster.Enemy
import net.minecraft.world.entity.monster.RangedAttackMob
import net.minecraft.world.entity.npc.villager.Villager
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.entity.projectile.arrow.AbstractArrow
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3
import java.util.*
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

class ArachneEntity(
    type: EntityType<out ArachneEntity>,
    world: Level,
) : DirectionalMovementEntity(type, world), Enemy, RangedAttackMob, HookAttackMob, MeleeAttackMob {
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
    private val lookAtPlayerGoal = DisableAbleLookAtEntityGoal(this, Player::class.java, 8.0f)
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

        fun createAttributes(): AttributeSupplier.Builder {
            return createLivingAttributes()
                .add(Attributes.FOLLOW_RANGE, 35.0)
                .add(Attributes.MOVEMENT_SPEED, 0.4)
                .add(Attributes.STEP_HEIGHT, 1.1)
                .add(Attributes.SAFE_FALL_DISTANCE, 10.0)
                .add(Attributes.FALL_DAMAGE_MULTIPLIER, 0.2)
                .add(Attributes.ATTACK_DAMAGE, 4.0)
                .add(Attributes.ATTACK_KNOCKBACK, 1.5)
                .add(Attributes.ARMOR, 0.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8)
                .add(Attributes.MOVEMENT_EFFICIENCY, 0.85)
        }

        val ATTACK_POSE: EntityDataAccessor<CustomPosesEntity.CustomPose> =
            SynchedEntityData.defineId(ArachneEntity::class.java, CustomPosesEntity.CUSTOM_POSE_TDH)
    }

    init {
        initDynamicGoals()
    }

    private fun initDynamicGoals() {
        goalSelector.addGoal(2, hookAttackGoal)
        goalSelector.addGoal(3, projectileAttackGoal)
        goalSelector.addGoal(3, meleeAttackGoal)
        goalSelector.addGoal(4, rangedKeepDistanceGoal)
        goalSelector.addGoal(4, stayInMeleeRangeGoal)
        goalSelector.addGoal(5, wanderGoal)
        goalSelector.addGoal(6, lookAtPlayerGoal)
        goalSelector.addGoal(7, lookAroundGoal)

        updateGoals()
    }

    override fun registerGoals() {
        goalSelector.addGoal(0, FloatGoal(this))
        goalSelector.addGoal(1, ChangeTargetGoal(this, probability = 0.4, tryIntervalTicks = 20, 100, { it is Player || it is Villager }))

        targetSelector.addGoal(0, HurtByTargetGoal(this))
        targetSelector.addGoal(1, NearestAttackableTargetGoal(this, Player::class.java, true))
        targetSelector.addGoal(2, NearestAttackableTargetGoal(this, Villager::class.java, true))
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

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        super.defineSynchedData(builder)
        builder.define(ATTACK_POSE, CustomPosesEntity.CustomPose.IDLING)
    }

    override fun onSyncedDataUpdated(data: EntityDataAccessor<*>) {
        if (data == ATTACK_POSE) {
            when (entityData.get(ATTACK_POSE)) {
                CustomPosesEntity.CustomPose.SPITTING -> {
                    spitAnimationState.start(tickCount)
                }

                CustomPosesEntity.CustomPose.MELEE_ATTACKING -> {
                    attackAnimationState.start(tickCount)
                }

                else -> {}
            }
        }
        super.onSyncedDataUpdated(data)
    }

    override fun tick() {
        super.tick()

        if (level().isClientSide) return
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
        if (livingTarget.isDeadOrDying) return true

        if (distanceToSqr(livingTarget) > maxStayMeleeRangeSquared) return true

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
        moveControl.strafe(0F, 0F)
        if (--disabledMovementTicks > 0) return
        updateGoals()
    }

    private fun updateAttackPose() {
        if (level().isClientSide) return
        if (attackAnimationTicks <= 0) return
        if (--attackAnimationTicks > 0) return

        entityData.set(
            ATTACK_POSE, CustomPosesEntity.CustomPose.IDLING
        )
    }

    private fun changeAttackPose(pose: CustomPosesEntity.CustomPose, animationTime: Int) {
        if (attackAnimationTicks > 0) return
        entityData.set(ATTACK_POSE, pose)
        attackAnimationTicks = animationTime
    }

    override fun startMovementAnimation(animationState: AnimationState) {
        if (animationState == walkLeftAnimationState || animationState == walkRightAnimationState)
            return super.startMovementAnimation(walkAnimationState)

        super.startMovementAnimation(animationState)
    }

    private fun shootAt(
        target: LivingEntity,
        entityFactory: (AdditionalProjectilesUtil.ProjectileIndex) -> Projectile,
        applyMisc: (Projectile) -> Unit,
        serverLevel: ServerLevel,
    ) {
        val xDistance = target.x - x
        val zDistance = target.z - z
        val aimY = target.eyeY - 1.1f
        val addedYVelocity = sqrt(xDistance * xDistance + zDistance * zDistance) * 0.2f

        AdditionalProjectilesUtil.shootProjectile(
            this,
            null,
            Vec3(xDistance, addedYVelocity, zDistance),
            entityFactory,
        ) { projectile, spreadVelocity, _ ->
            val itemStack = ItemStack(Items.AIR)
            Projectile.spawnProjectile(projectile, serverLevel, itemStack)
            { entity: Projectile ->
                entity.shoot(spreadVelocity.x, spreadVelocity.y + aimY - projectile.y, spreadVelocity.z, 1.6f, 2.0f)
            }

            applyMisc(projectile)
        }

        changeAttackPose(CustomPosesEntity.CustomPose.SPITTING, 9)// anim is 0.42s
        playSpitSound()
    }

    override fun performRangedAttack(
        target: LivingEntity,
        pullProgress: Float,
    ) {
        val serverLevel = level() as? ServerLevel ?: return
        shootAt(
            target,
            { WebshotEntity(CustomEntities.WEBSHOT, serverLevel, this) },
            { projectile ->
                (projectile as? AbstractArrow)?.setBaseDamage(getAttributeValue(Attributes.ATTACK_DAMAGE) / 2.0)
            },
            serverLevel,
        )
    }

    override fun shootHookAt(
        target: LivingEntity,
    ) {
        val serverLevel = level() as? ServerLevel ?: return
        shootAt(
            target,
            { WebhookEntity(CustomEntities.WEBHOOK, serverLevel, this) },
            { projectile ->
                addHookedEntity(projectile.uuid)
                (projectile as? AbstractArrow)?.setBaseDamage(1.0)
            },
            serverLevel,
        )
    }

    override fun makeStuckInBlock(
        state: BlockState,
        multiplier: Vec3
    ) {
        resetFallDistance()
        if (state.`is`(Blocks.COBWEB)) return
        if (state.`is`(CustomBlocks.DECAYING_COBWEB)) return
        stuckSpeedMultiplier = multiplier
    }

    override fun causeFallDamage(fallDistance: Double, damagePerDistance: Float, damageSource: DamageSource) = false

    override fun dampensVibrations() = true

    override fun getAmbientSound(): SoundEvent {
        return SoundEvents.SPIDER_AMBIENT
    }

    override fun getDeathSound(): SoundEvent {
        return SoundEvents.SPIDER_DEATH
    }

    override fun getHurtSound(source: DamageSource): SoundEvent {
        return SoundEvents.SPIDER_HURT
    }

    private fun playSpitSound() {
        playSound(SoundEvents.SPIDER_AMBIENT, 1.0f, 0.75F + random.nextFloat() * 0.25F)
    }

    override fun playStepSound(
        pos: BlockPos,
        state: BlockState
    ) {
        if (!state.fluidState.isEmpty) return

        val blockState = level().getBlockState(pos.above())
        val blockSoundGroup = if (blockState.`is`(Blocks.SNOW)) blockState.soundType else state.soundType

        playSound(SoundEvents.SPIDER_STEP, blockSoundGroup.getVolume() * 0.15f, blockSoundGroup.getPitch())
    }

    override fun getLeashOffset() = Vec3(0.0, eyeHeight * 0.9, bbWidth * 0.4)

    override fun addHookedEntity(hookedUuid: UUID) {
        super.addHookedEntity(hookedUuid)

        val world = level() as? ServerLevel ?: return
        val entity = world.getEntity(hookedUuid) as? LivingEntity ?: return

        triggerMeleeOnHook(entity)

        entity.setWebbed()
    }

    private fun triggerMeleeOnHook(hookedEntity: LivingEntity) {
        if (target != hookedEntity && hookedEntity !is Player) return
        target = hookedEntity

        isCurrentlyRanged = false
        meleeTicks = 0
        updateGoals()
    }

    override fun removeHookedEntity(hookedUuid: UUID) {
        super.removeHookedEntity(hookedUuid)

        val world = level() as? ServerLevel ?: return
        val entity = world.getEntity(hookedUuid) ?: return

        if (entity !is LivingEntity) return
        entity.setWebbed(false)
    }

    override fun die(damageSource: DamageSource) {
        super.die(damageSource)

        val serverWorld = level() as? ServerLevel ?: return
        hookedEntityUuidMap.keys.forEach { uuid ->
            val entity = serverWorld.getEntity(uuid) as? LivingEntity ?: return@forEach
            entity.setWebbed(false)
        }
    }

    override fun meleeAttack(target: LivingEntity) {
        changeAttackPose(CustomPosesEntity.CustomPose.MELEE_ATTACKING, 28)
        blockMovement(15)
        dealAttackDamageDelay = 7
        lookControl.setLookAt(target)
        lookAt(target, 180F, 180F)
        yBodyRot = yRot
    }

    private fun dealAttackDamage() {
        val serverWorld = level() as? ServerLevel ?: return

        var targets = serverWorld.getEntitiesOfClass(
            LivingEntity::class.java,
            boundingBox.inflate(MELEE_ATTACK_LENGTH * scale)
        ) { it != this }

        val forward = calculateViewVector(xRot, yBodyRot).horizontal().normalize()
        val sideways = forward.cross(Vec3(0.0, 1.0, 0.0))
        targets = targets.filter {
            isInAttackArea(it.position().subtract(position()), forward, sideways)
                    || isInAttackArea(it.eyePosition.subtract(position()), forward, sideways)
        }

        val damage = getAttributeValue(Attributes.ATTACK_DAMAGE).toFloat()
        val knockBackDirection = calculateViewVector(xRot, yBodyRot).horizontal().normalize()
        val knockBackStrength = getAttributeValue(Attributes.ATTACK_KNOCKBACK) * getAttributeValue(Attributes.SCALE)

        targets.forEach {
            it.dealGenericAttackDamage(damage, this)
            if (level() is ServerLevel && it is Avatar && it.isBlocking) it.setShieldsCooldown(MELEE_SHIELD_DISABLE_TIME)

            it.setAndSyncVelocity(knockBackDirection.scale(knockBackStrength))
        }
    }

    private fun isInAttackArea(
        relativePos: Vec3,
        forward: Vec3,
        sideways: Vec3
    ): Boolean {
        val forwardDistance = relativePos.dot(forward)
        val sidewaysDistance = relativePos.dot(sideways)
        val heightDistance = relativePos.y

        val scale = getAttributeValue(Attributes.SCALE)
        if (forwardDistance < bbWidth * scale * 0.2 || forwardDistance > MELEE_ATTACK_LENGTH * scale) return false
        if (abs(sidewaysDistance) > MELEE_ATTACK_WIDTH / 2.0 * scale) return false
        if (abs(heightDistance) > MELEE_ATTACK_HEIGHT / 2.0 * scale) return false
        return true
    }

    private fun isMovementDisabled() = disabledMovementTicks > 0

    override fun updateMovementPose() {
        if (!isMovementDisabled()) return super.updateMovementPose()

        if (entityData.get(MOVEMENT_POSE) == CustomPosesEntity.CustomPose.IDLING) return
        entityData.set(MOVEMENT_POSE, CustomPosesEntity.CustomPose.IDLING)
    }

    override fun canBeAffected(effect: MobEffectInstance): Boolean {
        if (effect.effect == MobEffects.SLOWNESS) return false
        return super.canBeAffected(effect)
    }
}