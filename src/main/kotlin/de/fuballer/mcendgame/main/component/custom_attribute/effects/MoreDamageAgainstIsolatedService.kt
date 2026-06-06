package de.fuballer.mcendgame.main.component.custom_attribute.effects

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributeUtil.isIsolated
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.world.entity.LivingEntity

@Injectable
class MoreDamageAgainstIsolatedService {
    @CommandHandler
    fun on(cmd: DamageCalculationCommand) {
        val damager = cmd.damager as? LivingEntity ?: return
        val attributes = cmd.damagerAttributes[CustomAttributeTypes.MORE_DAMAGE_AGAINST_ISOLATED] ?: return
        if (!cmd.damaged.isIsolated(damager)) return

        attributes.forEach { attribute ->
            cmd.moreDamage.add((attribute.rolls[0] as DoubleRoll).getValue())
        }
    }
}