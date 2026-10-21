package de.fuballer.mcendgame.main.component.entity.custom.entities.magma_spike

import com.geckolib.animatable.GeoAnimatable
import com.geckolib.animatable.instance.AnimatableInstanceCache
import com.geckolib.animatable.manager.AnimatableManager
import com.geckolib.util.GeckoLibUtil
import de.fuballer.mcendgame.main.component.entity.custom.CustomEntities
import de.fuballer.mcendgame.main.util.extension.EntityExtension.setAndSyncVelocity
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.*
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.level.Level
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.minecraft.world.phys.Vec3

class MagmaSpikeEntity(
    type: EntityType<out MagmaSpikeEntity>,
    level: Level,
) : Entity(type, level), GeoAnimatable, TraceableEntity {
    companion object {
        const val EMERGE_TICKS: Int = 4
        const val HOLD_EMERGED_TICKS: Int = 15
        const val RETRACT_TICKS: Int = 15
        const val TICKS_UNTIL_RETRACT: Int = EMERGE_TICKS + HOLD_EMERGED_TICKS
        const val TOTAL_TICKS: Int = EMERGE_TICKS + HOLD_EMERGED_TICKS + TICKS_UNTIL_RETRACT
        const val PARTICLE_TRIGGER_TICK: Int = 1
        const val DAMAGE_TICK: Int = 3
        val DATA_XZ_ROT_RAD: EntityDataAccessor<Float> = SynchedEntityData.defineId(MagmaSpikeEntity::class.java, EntityDataSerializers.FLOAT)
        val DATA_SCALE: EntityDataAccessor<Float> = SynchedEntityData.defineId(MagmaSpikeEntity::class.java, EntityDataSerializers.FLOAT)
        val DATA_LARGE: EntityDataAccessor<Boolean> = SynchedEntityData.defineId(MagmaSpikeEntity::class.java, EntityDataSerializers.BOOLEAN)
    }

    private var progressTicks = 0
    private var sentSpikeEvent = false
    private var lifeTicks = 0
    private var clientSideAttackStarted = false
    private var owner: EntityReference<LivingEntity>? = null

    constructor(
        level: Level,
        x: Double,
        y: Double,
        z: Double,
        yRotRad: Float,
        xzRotRad: Float,
        scale: Float,
        delay: Int,
        owner: LivingEntity?,
        large: Boolean,
    ) : this(CustomEntities.MAGMA_SPIKE, level) {
        progressTicks = -delay
        setOwner(owner)
        yRot = yRotRad * (180.0f / Math.PI.toFloat())
        entityData.set(DATA_XZ_ROT_RAD, xzRotRad)
        entityData.set(DATA_SCALE, scale)
        entityData.set(DATA_LARGE, large)
        setPos(x, y, z)
    }

    private val cache: AnimatableInstanceCache = GeckoLibUtil.createInstanceCache(this)
    override fun getAnimatableInstanceCache() = cache

    override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {
    }

    protected override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        builder.define(DATA_XZ_ROT_RAD, 0.0f)
        builder.define(DATA_SCALE, 1f)
        builder.define(DATA_LARGE, false)
    }

    override fun onSyncedDataUpdated(accessor: EntityDataAccessor<*>) {
        super.onSyncedDataUpdated(accessor)
        if (accessor == DATA_SCALE) refreshDimensions()
    }

    fun setOwner(owner: LivingEntity?) {
        this.owner = EntityReference.of(owner)
    }

    override fun getOwner(): LivingEntity? = EntityReference.getLivingEntity(owner, level())

    override fun readAdditionalSaveData(input: ValueInput) {
        progressTicks = input.getIntOr("ProgressTicks", 0)
        entityData.set(DATA_XZ_ROT_RAD, input.getFloatOr("XZRotRad", 0.0f))
        entityData.set(DATA_SCALE, input.getFloatOr("Scale", 1.0f))
        entityData.set(DATA_LARGE, input.getBooleanOr("Large", false))
        owner = EntityReference.read(input, "Owner")
    }

    override fun addAdditionalSaveData(output: ValueOutput) {
        output.putInt("ProgressTicks", progressTicks)
        output.putFloat("XZRotRad", entityData.get(DATA_XZ_ROT_RAD))
        output.putFloat("Scale", entityData.get(DATA_SCALE))
        output.putBoolean("Large", entityData.get(DATA_LARGE))
        EntityReference.store(owner, output, "Owner")
    }

    private fun getRandomParticlePos(): Vec3 {
        val x = (random.nextDouble() * 2.0 - 1.0) * bbWidth * 0.5
        val y = 0.15 + random.nextDouble()
        val z = (random.nextDouble() * 2.0 - 1.0) * bbWidth * 0.5
        val vec = Vec3(x, y, z).scale(getScale().toDouble())
        return Vec3(this.x, this.y, this.z).add(vec)
    }

    private fun getRandomParticleVector(): Vec3 {
        val xd = (random.nextDouble() * 2.0 - 1.0) * 0.3
        val yd = 0.3 + random.nextDouble() * 0.3
        val zd = (random.nextDouble() * 2.0 - 1.0) * 0.3
        return Vec3(xd, yd, zd).scale(getScale().toDouble())
    }

    private fun createParticles(
        type: SimpleParticleType,
        count: Int,
    ) {
        repeat(count) {
            val pos = getRandomParticlePos()
            val vec = getRandomParticleVector()
            level().addParticle(type, pos.x, pos.y, pos.z, vec.x, vec.y, vec.z)
        }
    }

    override fun tick() {
        super.tick()
        if (level().isClientSide) {
            if (!clientSideAttackStarted) return
            if (++lifeTicks == PARTICLE_TRIGGER_TICK) {
                createParticles(ParticleTypes.LAVA, if (isLarge()) 10 else 1)
                if (isLarge()) {
                    createParticles(ParticleTypes.SMOKE, 10)
                    createParticles(ParticleTypes.FLAME, 10)
                    createParticles(ParticleTypes.EXPLOSION, 3)
                }
            }
            return
        }

        if (++progressTicks < 0) return
        if (progressTicks == DAMAGE_TICK) {
            val inflate = if (isLarge()) 0.8 else 0.4
            level().getEntitiesOfClass(LivingEntity::class.java, boundingBox.inflate(inflate))
                .forEach { dealDamageTo(it) }
        }

        if (!sentSpikeEvent) {
            level().broadcastEntityEvent(this, 4.toByte())
            sentSpikeEvent = true
        }

        if (++lifeTicks > TOTAL_TICKS) {
            discard()
        }
    }

    private fun dealDamageTo(entity: LivingEntity) {
        val serverLevel = level() as? ServerLevel ?: return
        val currentOwner = getOwner()
        if (!entity.isAlive) return
        if (entity.isInvulnerable) return
        if (entity == currentOwner) return

        // TODO get damage from owner & use mcendgame system
        val damageSource = if (currentOwner == null) damageSources().magic() else damageSources().indirectMagic(this, currentOwner)
        if (entity.hurtServer(serverLevel, damageSource, if (isLarge()) 10.0f else 6.0f)) {
            EnchantmentHelper.doPostAttackEffects(serverLevel, entity, damageSource)
        }

        entity.setAndSyncVelocity(entity.deltaMovement.scale(0.5).add(0.0, 0.35 * getScale(), 0.0))
    }

    override fun handleEntityEvent(id: @EntityEvent.Value Byte) {
        super.handleEntityEvent(id)
        if (id.toInt() == 4) {
            clientSideAttackStarted = true

            level().playLocalSound(x, y, z, SoundEvents.LAVA_POP, soundSource, 1.0f, random.nextFloat() * 0.2f + 0.85f, false)
            if (!isLarge()) return
            level().playLocalSound(x, y, z, SoundEvents.GENERIC_EXPLODE.value(), soundSource, 0.8f, random.nextFloat() * 0.2f + 0.85f, false)
        }
    }

    fun getEmergedPercentage(partialTick: Float): Float {
        if (!clientSideAttackStarted) return 0.0f

        val preciseTicks = lifeTicks + partialTick
        if (preciseTicks < EMERGE_TICKS) return preciseTicks / EMERGE_TICKS
        return 1.0f - ((preciseTicks - TICKS_UNTIL_RETRACT) / RETRACT_TICKS).coerceIn(0.0f, 1.0f)
    }

    override fun hurtServer(level: ServerLevel, source: DamageSource, damage: Float) = false

    override fun getDimensions(pose: Pose) = super.getDimensions(pose).scale(getScale())

    fun getScale() = entityData.get(DATA_SCALE)
    fun isLarge() = entityData.get(DATA_LARGE)
}
