package de.fuballer.mcendgame.main.component.entity.custom.entities.magma_spike

import com.geckolib.animatable.GeoAnimatable
import com.geckolib.animatable.instance.AnimatableInstanceCache
import com.geckolib.animatable.manager.AnimatableManager
import com.geckolib.util.GeckoLibUtil
import de.fuballer.mcendgame.main.component.entity.custom.CustomEntities
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.*
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.level.Level
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput

class MagmaSpikeEntity(
    type: EntityType<out MagmaSpikeEntity>,
    level: Level,
) : Entity(type, level), GeoAnimatable, TraceableEntity {
    companion object {
        const val ATTACK_DURATION: Int = 10
        const val LIFE_OFFSET: Int = 1
        const val PARTICLE_TRIGGER_TICKS: Int = 7
        const val DAMAGE_TICKS: Int = 4
        private const val DEFAULT_WARMUP_DELAY = 0
    }

    private var warmupDelayTicks = 0
    private var sentSpikeEvent = false
    private var lifeTicks = ATTACK_DURATION + LIFE_OFFSET
    private var clientSideAttackStarted = false
    private var owner: EntityReference<LivingEntity>? = null

    constructor(
        level: Level,
        x: Double,
        y: Double,
        z: Double,
        rotationRadians: Float,
        warmupDelayTicks: Int,
        owner: LivingEntity?,
    ) : this(CustomEntities.MAGMA_SPIKE, level) {
        this.warmupDelayTicks = warmupDelayTicks
        setOwner(owner)
        yRot = rotationRadians * (180.0f / Math.PI.toFloat())
        setPos(x, y, z)
    }

    private val cache: AnimatableInstanceCache = GeckoLibUtil.createInstanceCache(this)
    override fun getAnimatableInstanceCache() = cache

    override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {
    }

    protected override fun defineSynchedData(entityData: SynchedEntityData.Builder) {
    }

    fun setOwner(owner: LivingEntity?) {
        this.owner = EntityReference.of(owner)
    }

    override fun getOwner(): LivingEntity? = EntityReference.getLivingEntity(owner, level())

    override fun readAdditionalSaveData(input: ValueInput) {
        warmupDelayTicks = input.getIntOr("Warmup", 0)
        owner = EntityReference.read(input, "Owner")
    }

    override fun addAdditionalSaveData(output: ValueOutput) {
        output.putInt("Warmup", warmupDelayTicks)
        EntityReference.store(owner, output, "Owner")
    }

    override fun tick() {
        super.tick()
        if (level().isClientSide) {
            if (!clientSideAttackStarted) return
            if (--lifeTicks == PARTICLE_TRIGGER_TICKS) {
                val x = x + (random.nextDouble() * 2.0 - 1.0) * bbWidth * 0.5
                val y = y + 0.05 + random.nextDouble()
                val z = z + (random.nextDouble() * 2.0 - 1.0) * bbWidth * 0.5
                val xd = (random.nextDouble() * 2.0 - 1.0) * 0.3
                val yd = 0.3 + random.nextDouble() * 0.3
                val zd = (random.nextDouble() * 2.0 - 1.0) * 0.3
                level().addParticle(ParticleTypes.LAVA, x, y + 1.0, z, xd, yd, zd)
            }
        } else if (--warmupDelayTicks < 0) {
            if (warmupDelayTicks == -DAMAGE_TICKS) {
                for (entity in level().getEntitiesOfClass(LivingEntity::class.java, boundingBox.inflate(0.2, 0.0, 0.2))) {
                    dealDamageTo(entity)
                }
            }

            if (!sentSpikeEvent) {
                level().broadcastEntityEvent(this, 4.toByte())
                sentSpikeEvent = true
            }

            if (--lifeTicks < 0) {
                discard()
            }
        }
    }

    private fun dealDamageTo(entity: LivingEntity) {
        val currentOwner = getOwner()
        if (entity.isAlive && !entity.isInvulnerable && entity !== currentOwner) {
            if (currentOwner == null) {
                entity.hurt(damageSources().magic(), 6.0f)
            } else {
                if (currentOwner.isAlliedTo(entity)) return

                val damageSource = damageSources().indirectMagic(this, currentOwner)
                val serverLevel = level() as? ServerLevel ?: return
                if (entity.hurtServer(serverLevel, damageSource, 6.0f)) {
                    EnchantmentHelper.doPostAttackEffects(serverLevel, entity, damageSource)
                }
            }
        }
    }

    override fun handleEntityEvent(id: @EntityEvent.Value Byte) {
        super.handleEntityEvent(id)
        if (id.toInt() == 4) {
            clientSideAttackStarted = true
            if (!isSilent) {
                level().playLocalSound(x, y, z, SoundEvents.LAVA_POP, soundSource, 1.0f, random.nextFloat() * 0.2f + 0.85f, false)
            }
        }
    }

    fun getAnimationProgress(a: Float): Float {
        if (!clientSideAttackStarted) return 0.0f

        val remainingLife = lifeTicks - LIFE_OFFSET
        return if (remainingLife <= 0) 1.0f else 1.0f - (remainingLife - a) / ATTACK_DURATION
    }

    override fun hurtServer(level: ServerLevel, source: DamageSource, damage: Float) = false
}
