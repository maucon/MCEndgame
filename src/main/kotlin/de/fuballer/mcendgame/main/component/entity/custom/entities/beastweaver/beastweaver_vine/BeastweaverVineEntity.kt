package de.fuballer.mcendgame.main.component.entity.custom.entities.beastweaver.beastweaver_vine

import com.geckolib.animatable.GeoEntity
import com.geckolib.animatable.instance.AnimatableInstanceCache
import com.geckolib.animatable.manager.AnimatableManager
import com.geckolib.util.GeckoLibUtil
import de.fuballer.mcendgame.main.component.entity.custom.CustomEntities
import net.minecraft.core.particles.BlockParticleOption
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
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
import net.minecraft.world.phys.Vec3
import java.util.*
import kotlin.jvm.optionals.getOrDefault

class BeastweaverVineEntity(
    type: EntityType<out BeastweaverVineEntity>,
    level: Level,
) : Mob(type, level), GeoEntity, Enemy, OwnableEntity {
    constructor(level: Level) : this(CustomEntities.BEASTWEAVER_VINE, level)

    companion object {
        fun createAttributes(): AttributeSupplier.Builder {
            return createLivingAttributes()
                .add(Attributes.FOLLOW_RANGE, 2.5)
                .add(Attributes.MOVEMENT_SPEED, 0.0)
                .add(Attributes.ATTACK_DAMAGE, 3.0)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5)
                .add(Attributes.ARMOR, 0.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0)
        }

        private const val OWNER_DATA_ID = "owner"
        private val OWNER_DATA = SynchedEntityData.defineId(BeastweaverVineEntity::class.java, EntityDataSerializers.OPTIONAL_LIVING_ENTITY_REFERENCE)

        private const val EMERGING_DATA_ID = "emerging"
        val EMERGING_DATA = SynchedEntityData.defineId(BeastweaverVineEntity::class.java, EntityDataSerializers.INT)

        const val EMERGE_DURATION_TICKS = 40
        const val ATTACK_DURATION_TICKS = 40
        const val ATTACK_DAMAGE_DELAY = 25
    }

    var attackTime = -1
    var offsetToTarget = Vec3.ZERO

    private val cache: AnimatableInstanceCache = GeckoLibUtil.createInstanceCache(this)
    override fun getAnimatableInstanceCache() = cache

    override fun defineSynchedData(entityData: SynchedEntityData.Builder) {
        super.defineSynchedData(entityData)
        entityData.define(OWNER_DATA, Optional.empty())
        entityData.define(EMERGING_DATA, 0)
    }

    override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {
        targetSelector.addGoal(0, BeastweaverVineNearestAttackableTargetGoal(this, Player::class.java))
    }

    override fun registerGoals() {
        super.registerGoals()
    }

    override fun baseTick() {
        super.baseTick()

        tickEmerging()

        val level = level() as? ServerLevel ?: return
        val owner = owner
        if (owner == null || !owner.isAlive) kill(level)

        tickAttack(level)
    }

    private fun tickEmerging() {
        val emergeTicks = entityData.get(EMERGING_DATA)
        if (emergeTicks > EMERGE_DURATION_TICKS) return

        val level = level()
        if (level !is ServerLevel) {
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
            if (emergeTicks % 4 == 0) {
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
        } else {
            entityData.set(EMERGING_DATA, emergeTicks + 1)
        }
    }

    private fun tickAttack(level: ServerLevel) {
        if (attackTime >= 0) {
            if (++attackTime > ATTACK_DURATION_TICKS) {
                attackTime = -1
                return
            }

            if (attackTime == ATTACK_DAMAGE_DELAY) dealAttackDamage(level)
        } else if (target?.isAlive == true) {
            attackTime = 0
            offsetToTarget = target!!.position().subtract(position())
        }
    }

    private fun dealAttackDamage(level: ServerLevel) {

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

        output.putInt(EMERGING_DATA_ID, entityData.get(EMERGING_DATA))
    }

    override fun readAdditionalSaveData(input: ValueInput) {
        super.readAdditionalSaveData(input)

        val owner = EntityReference.readWithOldOwnerConversion<LivingEntity>(input, OWNER_DATA_ID, level())
        if (owner == null) entityData.set(OWNER_DATA, Optional.empty())
        else entityData.set(OWNER_DATA, Optional.of(owner))

        entityData.set(EMERGING_DATA, input.getInt(EMERGING_DATA_ID).getOrDefault(0))
    }
}