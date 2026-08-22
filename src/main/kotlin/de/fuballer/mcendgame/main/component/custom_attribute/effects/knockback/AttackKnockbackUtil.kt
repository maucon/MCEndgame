package de.fuballer.mcendgame.main.component.custom_attribute.effects.knockback

import de.fuballer.mcendgame.main.messaging.misc.LivingEntityKnockbackLivingEntityCommand
import de.maucon.mauconframework.command.CommandGateway
import net.minecraft.core.registries.Registries
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.phys.Vec3

object AttackKnockbackUtil {
    fun LivingEntity.takeKnockbackFrom(
        attacker: Entity?,
        power: Double,
        x: Double,
        z: Double,
    ) {
        // TODO #287 damage source and amount should be legit
        if (attacker == null) return

        val damageType = attacker.level().registryAccess()
            .lookupOrThrow(Registries.DAMAGE_TYPE)
            .get(DamageTypes.GENERIC.identifier())
            .get()
        val source = DamageSource(damageType)
        val damage = 0F

        if (attacker !is LivingEntity) return knockback(power, x, z, source, damage)

        val command = LivingEntityKnockbackLivingEntityCommand(this, attacker, power)
        val cmd = CommandGateway.apply(command)

        knockback(cmd.power, x, z, source, damage)
    }

    fun LivingEntity.takeKnockbackFrom(
        attacker: Entity?,
        strength: Double,
        x: Double,
        y: Double,
        z: Double,
    ) {
        // TODO #287 damage source and amount should be legit
        if (attacker == null) return

        val damageType = attacker.level().registryAccess()
            .lookupOrThrow(Registries.DAMAGE_TYPE)
            .get(DamageTypes.GENERIC.identifier())
            .get()
        val source = DamageSource(damageType)
        val damage = 0F

        if (attacker !is LivingEntity) return knockback(strength, x, z, source, damage)

        val command = LivingEntityKnockbackLivingEntityCommand(this, attacker, strength)
        val cmd = CommandGateway.apply(command)
        var power = cmd.power

        // similar to LivingEntity.knockback() but with custom y velocity
        power *= 1.0 - getAttributeValue(Attributes.KNOCKBACK_RESISTANCE)
        if (power <= 0.0) return
        if (x * x + y * y + z * z < 1.0E-5F) return

        needsSync = true
        val deltaVector = Vec3(x, y, z).normalize().scale(power)
        setDeltaMovement(
            deltaMovement.x / 2.0 - deltaVector.x,
            deltaMovement.y / 2.0 - deltaVector.y,
            deltaMovement.z / 2.0 - deltaVector.z
        )
    }
}