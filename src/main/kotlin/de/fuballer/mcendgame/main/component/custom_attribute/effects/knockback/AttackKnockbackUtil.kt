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
     * Applies knockback caused by [source] to this entity. The knockback power is passed through
     * [LivingEntityKnockbackLivingEntityCommand] so that the attacker's MORE_ATTACK_KNOCKBACK
     * attribute is applied.
     */
    fun LivingEntity.takeKnockbackFrom(
        attacker: Entity?,
        power: Double,
        x: Double,
        z: Double,
        source: DamageSource,
        damage: Float,
    ) {
        if (attacker == null) return

        if (attacker !is LivingEntity) return knockback(power, x, z, source, damage)

        knockback(applyAttackKnockbackAttributes(attacker, power), x, z, source, damage)
    }

    /**
     * Overload for knockback that is not caused by a damage instance (e.g. custom attacks, which
     * apply their knockback explicitly). `LivingEntity.knockback` still requires a source, so a
     * plausible one is derived from the attacker.
     */
    fun LivingEntity.takeKnockbackFrom(
        attacker: Entity?,
        power: Double,
        x: Double,
        z: Double,
    ) {
        if (attacker == null) return
        takeKnockbackFrom(attacker, power, x, z, attacker.knockbackDamageSource(), 0f)
    }

    /**
     * Like [takeKnockbackFrom] but with a custom y velocity, similar to `LivingEntity.knockback`
     * but with a custom y component.
     */
    fun LivingEntity.takeKnockbackFrom(
        attacker: Entity?,
        strength: Double,
        x: Double,
        y: Double,
        z: Double,
        source: DamageSource,
        damage: Float,
    ) {
        if (attacker == null) return

        val power = if (attacker is LivingEntity) applyAttackKnockbackAttributes(attacker, strength) else strength
        applyKnockback(power, x, y, z)
    }

    fun LivingEntity.takeKnockbackFrom(
        attacker: Entity?,
        strength: Double,
        x: Double,
        y: Double,
        z: Double,
    ) {
        if (attacker == null) return
        takeKnockbackFrom(attacker, strength, x, y, z, attacker.knockbackDamageSource(), 0f)
    }

    private fun LivingEntity.applyAttackKnockbackAttributes(attacker: LivingEntity, power: Double): Double {
        val command = LivingEntityKnockbackLivingEntityCommand(this, attacker, power)
        return CommandGateway.apply(command).power
    }

    // similar to LivingEntity.knockback() but with a custom y velocity
    private fun LivingEntity.applyKnockback(power: Double, x: Double, y: Double, z: Double) {
        val resistedPower = power * (1.0 - getAttributeValue(Attributes.KNOCKBACK_RESISTANCE))
        if (resistedPower <= 0.0) return
        if (x * x + y * y + z * z < 1.0E-5F) return

        needsSync = true
        val deltaVector = Vec3(x, y, z).normalize().scale(resistedPower)
        setDeltaMovement(
            deltaMovement.x / 2.0 - deltaVector.x,
            deltaMovement.y / 2.0 - deltaVector.y,
            deltaMovement.z / 2.0 - deltaVector.z,
        )
    }

    /**
     * Fallback source for knockback that is not tied to a damage instance. The vanilla knockback
     * implementation does not read the source, but the attacker should at least be the causing
     * entity so the source is legitimate (see #287).
     */
    private fun Entity.knockbackDamageSource(): DamageSource =
        if (this is LivingEntity) damageSources().mobAttack(this) else damageSources().generic()
}