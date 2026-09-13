package de.fuballer.mcendgame.main.component.entity.custom.entities.bandit

import de.fuballer.mcendgame.main.component.custom_attribute.effects.projectile.AdditionalProjectilesUtil
import de.fuballer.mcendgame.main.component.entity.custom.entities.bandit.behaviour.BanditMoveControl
import de.fuballer.mcendgame.main.component.entity.custom.entities.bandit.behaviour.BanditPathNavigation
import de.fuballer.mcendgame.main.component.entity.custom.entities.bandit.behaviour.goals.BanditMeleeGoal
import de.fuballer.mcendgame.main.component.entity.custom.goals.FollowPartnerGoal
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.getHitbox
import net.minecraft.core.component.DataComponents
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.InteractionHand
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.*
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.FloatGoal
import net.minecraft.world.entity.ai.goal.Goal
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.ai.navigation.PathNavigation
import net.minecraft.world.entity.monster.Enemy
import net.minecraft.world.entity.monster.RangedAttackMob
import net.minecraft.world.entity.npc.villager.Villager
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.arrow.AbstractArrow
import net.minecraft.world.entity.projectile.arrow.Arrow
import net.minecraft.world.item.ItemCooldowns
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.minecraft.world.phys.Vec3
import java.util.*
import kotlin.math.sqrt

class BanditEntity(
    type: EntityType<BanditEntity>,
    level: Level,
) : PathfinderMob(type, level), Enemy, RangedAttackMob, FollowPartnerGoal.PairedMob {
    companion object {
        fun createAttributes(): AttributeSupplier.Builder {
            return createLivingAttributes()
                .add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.ATTACK_DAMAGE, 1.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ATTACK_SPEED)
                .add(Attributes.SWEEPING_DAMAGE_RATIO)
        }

        private const val FIGHTING_GOAL_PRIO = 1

        private const val PARTNER_ID = "partner"

        private const val BANDIT_TYPE_ID = "BanditType"
        private const val BANDIT_TYPE_INDEX_ID = "bandit_type_index"
        private val BANDIT_TYPE_INDEX: EntityDataAccessor<Int> = SynchedEntityData.defineId(BanditEntity::class.java, EntityDataSerializers.INT)
    }

    private var targetPos = Vec3.ZERO
    private var targetMovement = Vec3.ZERO

    private lateinit var fightingGoal: Goal

    private val cooldowns: ItemCooldowns = ItemCooldowns()

    init {
        moveControl = BanditMoveControl(this)
    }

    override fun baseTick() {
        super.baseTick()

        cooldowns.tick()

        if (level().isClientSide) return

        val prevTargetPos = targetPos
        targetPos = target?.position() ?: Vec3.ZERO
        targetMovement = targetPos.subtract(prevTargetPos)
    }

    override fun createNavigation(level: Level): PathNavigation = BanditPathNavigation(this, level)

    override fun registerGoals() {
        goalSelector.addGoal(0, FloatGoal(this))

        fightingGoal = getBanditType().fightingGoal(this)
        goalSelector.addGoal(FIGHTING_GOAL_PRIO, fightingGoal)

        goalSelector.addGoal(2, FollowPartnerGoal(this, 1.0, 10.0, 3.0))
        goalSelector.addGoal(3, WaterAvoidingRandomStrollGoal(this, 1.0))
        goalSelector.addGoal(4, RandomLookAroundGoal(this))

        targetSelector.addGoal(0, HurtByTargetGoal(this))
        targetSelector.addGoal(1, NearestAttackableTargetGoal(this, Player::class.java, false))
        targetSelector.addGoal(2, NearestAttackableTargetGoal(this, Villager::class.java, false))
    }

    override fun defineSynchedData(entityData: SynchedEntityData.Builder) {
        super.defineSynchedData(entityData)
        entityData.define(BANDIT_TYPE_INDEX, BanditType.DEFAULT.ordinal)
    }

    fun setBanditType(type: BanditType) {
        entityData.set(BANDIT_TYPE_INDEX, type.ordinal)

        customName = type.customName

        if (::fightingGoal.isInitialized) goalSelector.removeGoal(fightingGoal)
        fightingGoal = type.fightingGoal(this)
        goalSelector.addGoal(FIGHTING_GOAL_PRIO, fightingGoal)
    }

    fun getBanditType() = BanditType.entries[entityData.get(BANDIT_TYPE_INDEX)]

    override var partnerReference: EntityReference<LivingEntity>? = null

    override fun getLevel() = level()

    fun setPartner(partner: LivingEntity?) {
        partnerReference = EntityReference.of(partner)
    }

    override fun aiStep() {
        super.aiStep()
        updateSwingTime()
    }

    override fun isWithinMeleeAttackRange(target: LivingEntity): Boolean {
        val attackRange = activeItem.get(DataComponents.ATTACK_RANGE)
        val maxRange: Double
        val minRange: Double
        if (attackRange == null) {
            maxRange = getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE)
            minRange = 0.0
        } else {
            maxRange = attackRange.effectiveMaxRange(this).toDouble()
            minRange = attackRange.effectiveMinRange(this).toDouble()
        }

        val hitbox = target.getHitbox()
        return getAttackBoundingBox(maxRange).intersects(hitbox) && (minRange <= 0.0 || !getAttackBoundingBox(minRange).intersects(hitbox))
    }

    override fun hurtServer(level: ServerLevel, source: DamageSource, damage: Float): Boolean {
        val hurt = super.hurtServer(level, source, damage)
        if (source.entity == null) return hurt
        (fightingGoal as? BanditMeleeGoal)?.tookHit()
        return hurt
    }

    override fun addAdditionalSaveData(output: ValueOutput) {
        super.addAdditionalSaveData(output)
        output.putInt(BANDIT_TYPE_INDEX_ID, entityData.get(BANDIT_TYPE_INDEX))
        EntityReference.store(partnerReference, output, PARTNER_ID)
    }

    override fun readAdditionalSaveData(input: ValueInput) {
        super.readAdditionalSaveData(input)

        partnerReference = EntityReference.readWithOldOwnerConversion(input, PARTNER_ID, level())

        val optionalTypeString = input.getString(BANDIT_TYPE_ID)
        if (optionalTypeString.isPresent) {
            val typeString = optionalTypeString.get()
            val type = BanditType.entries.firstOrNull { it.name.equals(typeString, ignoreCase = true) }

            if (type != null) {
                setBanditType(type)
                return
            }
        }

        val optionalIndex = input.getInt(BANDIT_TYPE_INDEX_ID)
        if (optionalIndex.isPresent) {
            val index = optionalIndex.get()
            val type = BanditType.entries[index]
            setBanditType(type)
            return
        }

        setBanditType(BanditType.entries.random())
    }

    fun getBanditMoveControl() = moveControl as BanditMoveControl

    override fun getFlyingSpeed(): Float = 0.1F

    override fun blockUsingItem(
        level: ServerLevel,
        attacker: LivingEntity,
        source: DamageSource,
        damage: Float,
    ) {
        super.blockUsingItem(level, attacker, source, damage)

        val itemBlockingWith = getItemBlockingWith() ?: return
        val blocksAttacks = itemBlockingWith.get(DataComponents.BLOCKS_ATTACKS) ?: return

        val secondsToDisableBlocking = attacker.getSecondsToDisableBlocking()
        if (secondsToDisableBlocking > 0.0F) blocksAttacks.disable(level, this, secondsToDisableBlocking, itemBlockingWith)
    }

    fun getCooldowns() = cooldowns

    override fun performRangedAttack(target: LivingEntity, power: Float) {
        val serverLevel = level() as? ServerLevel ?: return
        val banditType = getBanditType()

        var aimPosition = target.position()
        if (random.nextDouble() < banditType.predictMovementProbability) {
            val arrowTravelTime = distanceTo(target) / 3.0
            val randomFactorRange = banditType.predictedMovementRandomFactorRange
            val randomFactor = randomFactorRange.first + random.nextDouble() * (randomFactorRange.second - randomFactorRange.first)
            val predictedMovement = targetMovement.scale(arrowTravelTime * randomFactor)
            aimPosition = aimPosition.add(predictedMovement)
        }

        val xd = aimPosition.x - x
        val zd = aimPosition.z - z
        val horizontalDistance = sqrt(xd * xd + zd * zd)
        val direction = Vec3(xd, horizontalDistance * 0.1f, zd)

        AdditionalProjectilesUtil.shootProjectile(
            this,
            null,
            direction,
            {
                Arrow(
                    serverLevel,
                    this,
                    ItemStack(Items.ARROW),
                    getItemInHand(InteractionHand.MAIN_HAND)
                )
            },
            { projectile, spreadVelocity, _ ->
                projectile.shoot(
                    spreadVelocity.x,
                    spreadVelocity.y + target.getY(0.3333333333333333) - projectile.y,
                    spreadVelocity.z,
                    3f,
                    1f,
                )
                (projectile as? AbstractArrow)?.isCritArrow = true
                serverLevel.addFreshEntity(projectile)
            }
        )

        playSound(SoundEvents.SKELETON_SHOOT, 1.0f, 1.0f / (getRandom().nextFloat() * 0.4f + 0.8f))
    }

    override fun removeWhenFarAway(distSqr: Double) = false
}