package de.fuballer.mcendgame.main.component.custom_attribute.effects.knockback

import de.fuballer.mcendgame.main.messaging.misc.LivingEntityKnockbackLivingEntityCommand
import de.maucon.mauconframework.command.CommandGateway
import net.minecraft.core.registries.Registries
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity

object AttackKnockbackUtil {
    fun LivingEntity.takeKnockbackFrom(
        attacker: Entity?,
        strength: Double,
        x: Double,
        z: Double,
    ) {
        // FIXME
        val damageType = attacker!!.level().registryAccess()
            .lookupOrThrow(Registries.DAMAGE_TYPE)
            .get(DamageTypes.GENERIC.identifier())
            .get()
        val source = DamageSource(damageType)
        val damage = 0F

        if (attacker !is LivingEntity) return knockback(strength, x, z, source, damage)

        val command = LivingEntityKnockbackLivingEntityCommand(this, attacker, strength)
        val cmd = CommandGateway.apply(command)

        knockback(cmd.strength, x, z, source, damage)
    }
}