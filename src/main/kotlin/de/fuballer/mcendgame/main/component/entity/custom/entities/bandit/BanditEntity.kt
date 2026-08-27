package de.fuballer.mcendgame.main.component.entity.custom.entities.bandit

import de.fuballer.mcendgame.main.component.entity.custom.CustomEntities
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.DifficultyInstance
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.SpawnGroupData
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.FloatGoal
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.npc.villager.Villager
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.ServerLevelAccessor

class BanditEntity(
    type: EntityType<BanditEntity>,
    level: Level,
) : PathfinderMob(type, level) {
    companion object {
        fun createAttributes(): AttributeSupplier.Builder {
            return createLivingAttributes()
                .add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ATTACK_DAMAGE, 4.0)
                .add(Attributes.ATTACK_KNOCKBACK, 0.3)
        }

        fun create(type: BanditType, level: Level): BanditEntity {
            val bandit = BanditEntity(CustomEntities.BANDIT, level)
            bandit.setType(type)
            return bandit
        }

        private val BANDIT_TYPE_INDEX: EntityDataAccessor<Int> = SynchedEntityData.defineId(BanditEntity::class.java, EntityDataSerializers.INT)
    }

    var banditType: BanditType = BanditType.DEFAULT

    override fun registerGoals() {
        goalSelector.addGoal(0, FloatGoal(this))
        goalSelector.addGoal(1, MeleeAttackGoal(this, 1.2, true))
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

    override fun onSyncedDataUpdated(accessor: EntityDataAccessor<*>) {
        super.onSyncedDataUpdated(accessor)
        if (accessor == BANDIT_TYPE_INDEX) banditType = BanditType.entries[entityData.get(BANDIT_TYPE_INDEX)]
    }

    fun setType(type: BanditType) {
        banditType = type
        entityData.set(BANDIT_TYPE_INDEX, type.ordinal)
        customName = type.customName
    }

    override fun finalizeSpawn(
        level: ServerLevelAccessor,
        difficulty: DifficultyInstance,
        spawnReason: EntitySpawnReason,
        groupData: SpawnGroupData?,
    ): SpawnGroupData? {
        val result = super.finalizeSpawn(level, difficulty, spawnReason, groupData)

        banditType.equip(this)

        return result
    }

    override fun aiStep() {
        super.aiStep()
        updateSwingTime()
    }
}