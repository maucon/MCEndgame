package de.fuballer.mcendgame.main.component.entity.custom.entities.webhook

import de.fuballer.mcendgame.main.component.block.CustomBlocks
import de.fuballer.mcendgame.main.component.entity.custom.interfaces.HookAttackMob
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

class WebhookEntity(
    type: EntityType<out WebhookEntity>,
    world: Level,
    owner: LivingEntity? = null,
) : AbstractArrow(type, world) {
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

    override fun onHitEntity(entityHitResult: EntityHitResult) {
        val serverWorld = level() as? ServerLevel ?: return
        val attacker = EntityReference.getEntity(owner, serverWorld) as? LivingEntity ?: return
        val entity = entityHitResult.entity

        val damageSource = damageSources().mobProjectile(this, attacker)
        if (entity.hurtServer(serverWorld, damageSource, 1.0f)) {
            EnchantmentHelper.doPostAttackEffects(serverWorld, entity, damageSource)
        }

        discard()

        val hooker = attacker as? HookAttackMob ?: return
        hooker.addHookedEntity(entity.uuid)
    }

    override fun onHitBlock(blockHitResult: BlockHitResult) {
        val blockState = level().getBlockState(blockHitResult.blockPos)
        blockState.onProjectileHit(level(), blockState, blockHitResult, this)

        if (level().isClientSide) return
        discard()
    }

    override fun getDefaultPickupItem() = ItemStack(CustomBlocks.DECAYING_COBWEB)
}