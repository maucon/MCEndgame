package de.fuballer.mcendgame.main.component.custom_attribute.effects.knockback

import de.fuballer.mcendgame.main.messaging.misc.LivingEntityKnockbackLivingEntityCommand
import de.maucon.mauconframework.command.CommandGateway
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.phys.Vec3

object AttackKnockbackUtil {
    fun LivingEntity.takeKnockbackFrom(
        attacker: Entity?,
        strength: Double,
        x: Double,
        z: Double,
    ) {
        if (attacker !is LivingEntity) return knockback(strength, x, z)

        val command = LivingEntityKnockbackLivingEntityCommand(this, attacker, strength)
        val cmd = CommandGateway.apply(command)

        knockback(cmd.strength, x, z)
    }

    fun LivingEntity.takeKnockbackFrom(
        attacker: Entity?,
        strength: Double,
        x: Double,
        y: Double,
        z: Double,
    ) {
        if (attacker !is LivingEntity) return knockback(strength, x, z)

        val command = LivingEntityKnockbackLivingEntityCommand(this, attacker, strength)
        val cmd = CommandGateway.apply(command)
        var power = cmd.strength

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