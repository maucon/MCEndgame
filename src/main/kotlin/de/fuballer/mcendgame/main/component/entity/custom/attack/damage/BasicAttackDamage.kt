package de.fuballer.mcendgame.main.component.entity.custom.attack.damage

import de.fuballer.mcendgame.main.component.custom_attribute.effects.knockback.AttackKnockbackUtil.takeKnockbackFrom
import de.fuballer.mcendgame.main.component.damage.dealing.DamageDealingExtension.dealGenericAttackDamage
import de.fuballer.mcendgame.main.util.extension.EntityExtension.setShieldsCooldown
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.world.ServerWorld
import kotlin.math.min

class BasicAttackDamage(
    damageFactor: Float,
    knockbackFactor: Double,
    private val hitRange: Double,
    private val squaredHitRange: Double = hitRange * hitRange,
    blockable: Boolean = true,
    disableBlockingShield: Float = 0.0F,
) : AttackDamage(damageFactor, knockbackFactor, blockable, disableBlockingShield) {
    override fun apply(
        world: ServerWorld,
        damager: MobEntity,
        target: LivingEntity?
    ): Boolean {
        if (target?.isAlive != true) return false
        val squaredDistance = min(damager.squaredDistanceTo(target), damager.squaredDistanceTo(target.eyePos))
        if (squaredDistance > squaredHitRange) return false

        val damage = getDamage(damager)
        target.dealGenericAttackDamage(damage, damager, blockable)

        if (disableBlockingShield > 0 && target is PlayerEntity && target.isBlocking) target.setShieldsCooldown(disableBlockingShield)

        val knockback = getKnockback(damager)
        val knockbackDirection = target.pos.subtract(damager.pos).normalize()
        target.takeKnockbackFrom(damager, knockback, -knockbackDirection.x, -knockbackDirection.z)

        return true
    }
}