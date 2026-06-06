package de.fuballer.mcendgame.main.component.entity.custom.entities.webshot

import de.fuballer.mcendgame.main.component.block.CustomBlocks
import de.fuballer.mcendgame.main.component.block.blocks.DecayingCobwebBlock
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EntityReference
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.arrow.AbstractArrow
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import kotlin.math.cos
import kotlin.math.sin

class WebshotEntity(
    type: EntityType<out WebshotEntity>,
    world: Level,
    owner: LivingEntity? = null,
) : AbstractArrow(type, world) {
    private var createdDecayingCobwebs = false
    private var particleTimer = PARTICLE_COOLDOWN

    companion object {
        const val PARTICLE_COOLDOWN = 1
    }

    init {
        if (owner != null) {
            setOwner(owner)
            setPos(
                owner.x - (owner.bbWidth + 1.0) * 0.5 * sin(owner.yBodyRot * (Math.PI / 180.0)),
                owner.eyeY - 0.1f,
                owner.z + (owner.bbWidth + 1.0) * 0.5 * cos(owner.yBodyRot * (Math.PI / 180.0))
            )
        }
    }

    override fun getDefaultGravity() = 0.06

    override fun tick() {
        super.tick()
        spawnParticles()
    }

    private fun spawnParticles() {
        if (!level().isClientSide) return
        if (particleTimer-- > 0) return
        particleTimer = PARTICLE_COOLDOWN

        level().addParticle(ParticleTypes.CLOUD, getRandomX(0.5), randomY, getRandomZ(0.5), 0.0, 0.0, 0.0)
    }

    override fun onHitEntity(entityHitResult: EntityHitResult) {
        val serverWorld = level() as? ServerLevel ?: return
        val attacker = EntityReference.getEntity(owner, serverWorld) as? LivingEntity ?: return
        val entity = entityHitResult.entity

        generateDecayingCobwebs(entity.blockPosition())

        val damageSource = damageSources().mobProjectile(this, attacker)
        if (entity.hurtServer(serverWorld, damageSource, 1.0f)) {
            EnchantmentHelper.doPostAttackEffects(serverWorld, entity, damageSource)
        }

        if (level().isClientSide) return
        discard()
    }

    override fun onHitBlock(blockHitResult: BlockHitResult) {
        val blockState = level().getBlockState(blockHitResult.blockPos)
        blockState.onProjectileHit(level(), blockState, blockHitResult, this)

        if (level().isClientSide) return
        discard()

        generateDecayingCobwebs(blockHitResult.blockPos)
    }

    override fun getDefaultPickupItem() = ItemStack(CustomBlocks.DECAYING_COBWEB)

    private fun generateDecayingCobwebs(blockPos: BlockPos) {
        if (createdDecayingCobwebs) return
        createdDecayingCobwebs = true

        var tryCount = 0
        var successCount = 0
        while (tryCount < 5 || (successCount == 0 && tryCount < 25)) {
            tryCount++

            val pos = blockPos.offset(random.nextIntBetweenInclusive(-2, 2), random.nextIntBetweenInclusive(-2, 2), random.nextIntBetweenInclusive(-2, 2))
            if (!isValidCobwebPos(pos)) continue
            successCount++
            level().setBlockAndUpdate(pos, CustomBlocks.DECAYING_COBWEB.defaultBlockState())
            level().scheduleTick(pos, CustomBlocks.DECAYING_COBWEB, DecayingCobwebBlock.TICK_INTERVAL)
        }
    }

    private fun isValidCobwebPos(blockPos: BlockPos): Boolean {
        val blockState = level().getBlockState(blockPos)
        if (!blockState.isAir) return false

        if (!level().getBlockState(blockPos.above()).isAir) return true
        if (!level().getBlockState(blockPos.below()).isAir) return true
        if (!level().getBlockState(blockPos.north()).isAir) return true
        if (!level().getBlockState(blockPos.east()).isAir) return true
        if (!level().getBlockState(blockPos.south()).isAir) return true
        if (!level().getBlockState(blockPos.west()).isAir) return true

        return false
    }
}