package de.fuballer.mcendgame.main.component.entity.custom.entities.webshot

import de.fuballer.mcendgame.main.component.block.CustomBlocks
import de.fuballer.mcendgame.main.component.block.blocks.DecayingCobwebBlock
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LazyEntityReference
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import kotlin.math.cos
import kotlin.math.sin

class WebshotEntity(
    type: EntityType<out WebshotEntity>,
    world: World,
    owner: LivingEntity? = null,
) : PersistentProjectileEntity(type, world) {
    private var createdDecayingCobwebs = false
    private var particleTimer = PARTICLE_COOLDOWN

    companion object {
        const val PARTICLE_COOLDOWN = 1
    }

    init {
        if (owner != null) {
            setOwner(owner)
            setPosition(
                owner.x - (owner.width + 1.0) * 0.5 * sin(owner.bodyYaw * (Math.PI / 180.0)),
                owner.eyeY - 0.1f,
                owner.z + (owner.width + 1.0) * 0.5 * cos(owner.bodyYaw * (Math.PI / 180.0))
            )
        }
    }

    override fun getGravity() = 0.06

    override fun tick() {
        super.tick()
        spawnParticles()
    }

    private fun spawnParticles() {
        if (!entityWorld.isClient) return
        if (particleTimer-- > 0) return
        particleTimer = PARTICLE_COOLDOWN

        entityWorld.addParticleClient(ParticleTypes.CLOUD, getParticleX(0.5), randomBodyY, getParticleZ(0.5), 0.0, 0.0, 0.0)
    }

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        val serverWorld = entityWorld as? ServerWorld ?: return
        val attacker = LazyEntityReference.getEntity(owner, serverWorld) as? LivingEntity ?: return
        val entity = entityHitResult.entity

        generateDecayingCobwebs(entity.blockPos)

        val damageSource = damageSources.mobProjectile(this, attacker)
        if (entity.damage(damageSource, 1.0f)) {
            EnchantmentHelper.onTargetDamaged(serverWorld, entity, damageSource)
        }

        if (entityWorld.isClient) return
        discard()
    }

    override fun onBlockHit(blockHitResult: BlockHitResult) {
        val blockState = entityWorld.getBlockState(blockHitResult.blockPos)
        blockState.onProjectileHit(entityWorld, blockState, blockHitResult, this)

        if (entityWorld.isClient) return
        discard()

        generateDecayingCobwebs(blockHitResult.blockPos)
    }

    override fun getDefaultItemStack() = ItemStack(CustomBlocks.DECAYING_COBWEB)

    private fun generateDecayingCobwebs(blockPos: BlockPos) {
        if (createdDecayingCobwebs) return
        createdDecayingCobwebs = true

        var tryCount = 0
        var successCount = 0
        while (tryCount < 5 || (successCount == 0 && tryCount < 25)) {
            tryCount++

            val pos = blockPos.add(random.nextBetween(-2, 2), random.nextBetween(-2, 2), random.nextBetween(-2, 2))
            if (!isValidCobwebPos(pos)) continue
            successCount++
            entityWorld.setBlockState(pos, CustomBlocks.DECAYING_COBWEB.defaultState)
            entityWorld.scheduleBlockTick(pos, CustomBlocks.DECAYING_COBWEB, DecayingCobwebBlock.TICK_INTERVAL)
        }
    }

    private fun isValidCobwebPos(blockPos: BlockPos): Boolean {
        val blockState = entityWorld.getBlockState(blockPos)
        if (!blockState.isAir) return false

        if (!entityWorld.getBlockState(blockPos.up()).isAir) return true
        if (!entityWorld.getBlockState(blockPos.down()).isAir) return true
        if (!entityWorld.getBlockState(blockPos.north()).isAir) return true
        if (!entityWorld.getBlockState(blockPos.east()).isAir) return true
        if (!entityWorld.getBlockState(blockPos.south()).isAir) return true
        if (!entityWorld.getBlockState(blockPos.west()).isAir) return true

        return false
    }
}