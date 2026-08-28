package de.fuballer.mcendgame.main.component.entity.custom.entities.bandit

import de.fuballer.mcendgame.main.component.entity.custom.CustomEntities
import de.fuballer.mcendgame.main.component.entity.custom.entities.bandit.behaviour.BanditMoveControl
import de.fuballer.mcendgame.main.component.entity.custom.entities.bandit.behaviour.BanditPathNavigation
import de.fuballer.mcendgame.main.component.entity.custom.entities.bandit.behaviour.goals.BanditMeleeGoal
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.getHitbox
import net.minecraft.core.component.DataComponents
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.DifficultyInstance
import net.minecraft.world.entity.*
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.FloatGoal
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.ai.navigation.PathNavigation
import net.minecraft.world.entity.npc.villager.Villager
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.ServerLevelAccessor
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput

class BanditEntity(
    type: EntityType<BanditEntity>,
    level: Level,
) : PathfinderMob(type, level) {
    companion object {
        fun createAttributes(): AttributeSupplier.Builder {
            return createLivingAttributes()
                .add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.ATTACK_DAMAGE, 1.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ATTACK_SPEED)
                .add(Attributes.SWEEPING_DAMAGE_RATIO)
        }

        fun create(type: BanditType, level: Level): BanditEntity {
            val bandit = BanditEntity(CustomEntities.BANDIT, level)
            if (!level.isClientSide) bandit.setBanditType(type)
            return bandit
        }

        private const val BANDIT_TYPE_INDEX_ID = "bandit_type_index"
        private val BANDIT_TYPE_INDEX: EntityDataAccessor<Int> = SynchedEntityData.defineId(BanditEntity::class.java, EntityDataSerializers.INT)
    }

    init {
        moveControl = BanditMoveControl(this)
    }

    override fun createNavigation(level: Level): PathNavigation = BanditPathNavigation(this, level)

    override fun registerGoals() {
        goalSelector.addGoal(0, FloatGoal(this))
        goalSelector.addGoal(1, BanditMeleeGoal(this, 1.0))
        goalSelector.addGoal(2, WaterAvoidingRandomStrollGoal(this, 1.0))
        goalSelector.addGoal(3, RandomLookAroundGoal(this))

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
    }

    fun getBanditType() = BanditType.entries[entityData.get(BANDIT_TYPE_INDEX)]

    override fun finalizeSpawn(
        level: ServerLevelAccessor,
        difficulty: DifficultyInstance,
        spawnReason: EntitySpawnReason,
        groupData: SpawnGroupData?,
    ): SpawnGroupData? {
        val result = super.finalizeSpawn(level, difficulty, spawnReason, groupData)

        val type = getBanditType()
        type.equip(this)
        customName = type.customName

        return result
    }

    override fun aiStep() {
        super.aiStep()
        speed = getAttributeValue(Attributes.MOVEMENT_SPEED).toFloat()
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

    override fun getSpeed(): Float = getAttributeValue(Attributes.MOVEMENT_SPEED).toFloat()

    override fun addAdditionalSaveData(output: ValueOutput) {
        super.addAdditionalSaveData(output)
        output.putInt(BANDIT_TYPE_INDEX_ID, entityData.get(BANDIT_TYPE_INDEX))
    }

    override fun readAdditionalSaveData(input: ValueInput) {
        super.readAdditionalSaveData(input)
        input.getInt(BANDIT_TYPE_INDEX_ID).ifPresent { entityData.set(BANDIT_TYPE_INDEX, it) }
    }

    fun getBanditMoveControl() = moveControl as BanditMoveControl

    override fun getFlyingSpeed(): Float = 0.1F
}