package de.fuballer.mcendgame.main.component.custom_attribute.effects.knockback

import de.fuballer.mcendgame.main.messaging.misc.LivingEntityKnockbackLivingEntityCommand
import de.maucon.mauconframework.command.CommandGateway
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.phys.Vec3

object AttackKnockbackUtil {
    /**
     * Applies knockback to this entity exactly like `LivingEntity.knockback`, but with the power
     * scaled by [attacker]'s MORE_ATTACK_KNOCKBACK attribute.
     */
    fun LivingEntity.takeKnockbackFrom(attacker: Entity?, power: Double, x: Double, z: Double) {
        if (attacker == null) return
        knockback(applyAttackKnockback(attacker, power), x, z, attacker.knockbackSource(), 0f)
    }

    /**
     * Like [takeKnockbackFrom], but with a custom y velocity. `LivingEntity.knockback` always uses
     * a y velocity of 0, so this mirrors its math with a custom y component instead.
     */
    fun LivingEntity.takeKnockbackFrom(attacker: Entity?, power: Double, x: Double, y: Double, z: Double) {
        if (attacker == null) return
        applyKnockback(applyAttackKnockback(attacker, power), x, y, z)
    }

    /**
     * Applies [attacker]'s MORE_ATTACK_KNOCKBACK attributes to the given knockback power.
     */
    private fun LivingEntity.applyAttackKnockback(attacker: Entity, power: Double): Double {
        if (attacker !is LivingEntity) return power
        return CommandGateway.apply(LivingEntityKnockbackLivingEntityCommand(this, attacker, power)).power
    }

    /**
     * Mirrors the tail of
     * `LivingEntity.knockback(double, double, double, DamageSource, float, boolean)` with a custom
     * y component. Kept in sync with vanilla: the power is reduced by KNOCKBACK_RESISTANCE and the
     * resulting direction is blended into the current velocity.
     */
    private fun LivingEntity.applyKnockback(power: Double, x: Double, y: Double, z: Double) {
        val resistedPower = power * (1.0 - getAttributeValue(Attributes.KNOCKBACK_RESISTANCE))
        if (resistedPower <= 0.0) return
        if (x * x + y * y + z * z < 1.0E-5F) return

        needsSync = true
        val direction = Vec3(x, y, z).normalize().scale(resistedPower)
        setDeltaMovement(
            deltaMovement.x / 2.0 - direction.x,
            deltaMovement.y / 2.0 - direction.y,
            deltaMovement.z / 2.0 - direction.z,
        )
    }

    /**
     * `LivingEntity.knockback` requires a [DamageSource] argument but never reads it. Use one that
     * at least names the entity that caused the knockback.
     */
    private fun Entity.knockbackSource(): DamageSource =
        if (this is LivingEntity) damageSources().mobAttack(this) else damageSources().generic()
}
