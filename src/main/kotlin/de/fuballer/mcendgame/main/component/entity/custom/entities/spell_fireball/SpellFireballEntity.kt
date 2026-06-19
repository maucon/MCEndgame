package de.fuballer.mcendgame.main.component.entity.custom.entities.spell_fireball

import de.fuballer.mcendgame.main.component.damage.dealing.DamageDealingExtension.dealSpellDamage
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.entity.projectile.ProjectileUtil
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseFireBlock
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult

class SpellFireballEntity(
    type: EntityType<out SpellFireballEntity>,
    level: Level,
) : Projectile(type, level) {
    private var particleTimer = PARTICLE_COOLDOWN

    companion object {
        const val INERTIA = 0.99
        const val GRAVITY = 0.05
        private const val PARTICLE_COOLDOWN = 1
        private const val TARGET_FIRE_TICKS = 70
    }

    override fun tick() {
        super.tick()

        val hitResult = ProjectileUtil.getHitResultOnMoveVector(this) { entity: Entity? -> this.canHitEntity(entity!!) }
        hitTargetOrDeflectSelf(hitResult)

        val newX = x + deltaMovement.x
        val newY = y + deltaMovement.y
        val newZ = z + deltaMovement.z

        deltaMovement = deltaMovement.scale(INERTIA)
        applyGravity()
        setPos(newX, newY, newZ)

        spawnParticles()

        val serverLevel = level() as? ServerLevel ?: return
        if (isInWater) {
            extinguish(serverLevel)
            return
        }
    }

    private fun spawnParticles() {
        if (!level().isClientSide) return
        if (particleTimer-- > 0) return
        particleTimer = PARTICLE_COOLDOWN

        level().addParticle(ParticleTypes.SMOKE, getRandomX(0.35), randomY, getRandomZ(0.35), 0.0, 0.0, 0.0)
    }

    override fun onHitEntity(hitResult: EntityHitResult) {
        super.onHitEntity(hitResult)
        if (level().isClientSide) return

        damageTarget(hitResult.entity)

        playSound(SoundEvents.FIRE_EXTINGUISH, 0.5f, 0.75F + random.nextFloat() * 0.25F)
        playSound(SoundEvents.GENERIC_EXPLODE.value(), 0.5f, 0.75F + random.nextFloat() * 0.25F)

        discard()
    }

    private fun damageTarget(
        target: Entity,
    ) {
        val livingOwner = getOwner() as? LivingEntity ?: return
        target.dealSpellDamage(1.0, livingOwner, this)
        target.remainingFireTicks = TARGET_FIRE_TICKS
    }

    override fun onHitBlock(blockHitResult: BlockHitResult) {
        super.onHitBlock(blockHitResult)
        val serverLevel = level() as? ServerLevel ?: return

        playSound(SoundEvents.FIRE_EXTINGUISH, 0.5f, 0.75F + random.nextFloat() * 0.25F)
        playSound(SoundEvents.GENERIC_EXPLODE.value(), 0.5f, 0.75F + random.nextFloat() * 0.25F)
        serverLevel.sendParticles(ParticleTypes.SMOKE, x, y, z, 5, 0.2, 0.2, 0.2, 0.3)
        spawnFire(serverLevel)

        discard()
    }

    private fun spawnFire(
        level: ServerLevel,
    ) {
        val blockPos = blockPosition()
        val fire = BaseFireBlock.getState(level, blockPos)
        if (level.getBlockState(blockPos).isAir && fire.canSurvive(level, blockPos)) {
            level.setBlockAndUpdate(blockPos, fire)
        }
    }

    override fun defineSynchedData(entityData: SynchedEntityData.Builder) {}

    override fun getDefaultGravity() = GRAVITY

    private fun extinguish(level: ServerLevel) {
        playSound(SoundEvents.LAVA_EXTINGUISH, 0.75f, 0.75F + random.nextFloat() * 0.25F)
        level.sendParticles(ParticleTypes.WHITE_SMOKE, x, y, z, 5, 0.2, 0.2, 0.2, 0.2)

        discard()
    }
}