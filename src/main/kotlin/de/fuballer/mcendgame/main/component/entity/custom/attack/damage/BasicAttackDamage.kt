package de.fuballer.mcendgame.main.component.entity.custom.attack.damage

import de.fuballer.mcendgame.main.component.custom_attribute.effects.knockback.AttackKnockbackUtil.takeKnockbackFrom
import de.fuballer.mcendgame.main.component.damage.dealing.DamageDealingExtension.dealGenericAttackDamage
import de.fuballer.mcendgame.main.util.extension.EntityExtension.setShieldsCooldown
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Avatar
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
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
        world: ServerLevel,
        damager: Mob,
        target: LivingEntity?
    ): Boolean {
        if (target?.isAlive != true) return false
        val squaredDistance = min(damager.distanceToSqr(target), damager.distanceToSqr(target.eyePosition))
        if (squaredDistance > squaredHitRange) return false

        val damage = getDamage(damager)
        val dealtDamage = target.dealGenericAttackDamage(damage, damager, blockable)

        if (disableBlockingShield > 0 && target is Avatar && target.isBlocking) target.setShieldsCooldown(disableBlockingShield)

        if (dealtDamage || knockbackWhenBlocked) {
            val knockback = getKnockback(damager)
            val knockbackDirection = target.position().subtract(damager.position()).normalize()
            target.takeKnockbackFrom(damager, knockback, -knockbackDirection.x, -knockbackDirection.z)
            target.needsSync = true
            target.hurtMarked = true
        }

        return true
    }
}