package de.fuballer.mcendgame.main.component.entity.custom.entities.spell_fireball

import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.entity.projectile.ProjectileUtil
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult

class SpellFireballEntity(
    type: EntityType<out SpellFireballEntity>,
    level: Level,
) : Projectile(type, level) {
    companion object {
        const val INERTIA = 0.99
        const val GRAVITY = 0.05
    }

    override fun tick() {
        super.tick()

        val hitResult = ProjectileUtil.getHitResultOnMoveVector(this) { entity: Entity? -> this.canHitEntity(entity!!) }
        hitTargetOrDeflectSelf(hitResult)

        val newX = x + deltaMovement.x
        val newY = y + deltaMovement.y
        val newZ = z + deltaMovement.z

        if (isInWater) {
            discard()
            return
        }
        if (level().getBlockStates(boundingBox).noneMatch(BlockBehaviour.BlockStateBase::isAir)) {
            discard()
            return
        }

        deltaMovement = deltaMovement.scale(INERTIA)
        applyGravity()
        setPos(newX, newY, newZ)
    }

    override fun onHitEntity(hitResult: EntityHitResult) {
        super.onHitEntity(hitResult)
        val livingOwner = getOwner() as? LivingEntity ?: return
        val serverLevel = level() as? ServerLevel ?: return

        val target = hitResult.entity
        val damageSource = this.damageSources().spit(this, livingOwner)

        if (!target.hurtServer(serverLevel, damageSource, 1.0f)) return
        EnchantmentHelper.doPostAttackEffects(serverLevel, target, damageSource)
    }

    override fun onHitBlock(blockHitResult: BlockHitResult) {
        super.onHitBlock(blockHitResult)
        if (!level().isClientSide) discard()
    }

    override fun defineSynchedData(entityData: SynchedEntityData.Builder) {}

    override fun getDefaultGravity() = GRAVITY
}