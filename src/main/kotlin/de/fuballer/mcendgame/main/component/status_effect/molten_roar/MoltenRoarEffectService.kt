package de.fuballer.mcendgame.main.component.status_effect.molten_roar

import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.fuballer.mcendgame.main.component.status_effect.CustomStatusEffects
import de.fuballer.mcendgame.main.messaging.collect_attribute.CollectGenericMoreDamageCommand
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.world.entity.LivingEntity

private const val BASE_MORE_DAMAGE = 0.1
private const val MORE_DAMAGE_PER_AMPLIFIER = 0.05

@Injectable
class MoltenRoarEffectService {
    @CommandHandler
    fun on(cmd: DamageCalculationCommand) {
        val damager = cmd.damager as? LivingEntity ?: return
        getValue(damager)?.let { cmd.moreDamage.add(it) }
    }

    @CommandHandler
    fun on(cmd: CollectGenericMoreDamageCommand) {
        getValue(cmd.entity)?.let { cmd.more.add(it) }
    }

    private fun getValue(entity: LivingEntity): Double? {
        val amplifier = entity.getEffect(CustomStatusEffects.MOLTEN_ROAR)?.amplifier ?: return null
        return BASE_MORE_DAMAGE + (amplifier * MORE_DAMAGE_PER_AMPLIFIER)
    }
}