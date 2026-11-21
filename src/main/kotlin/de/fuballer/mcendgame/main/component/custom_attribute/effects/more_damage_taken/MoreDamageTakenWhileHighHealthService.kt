package de.fuballer.mcendgame.main.component.custom_attribute.effects.more_damage_taken

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributeUtil.isHighHealth
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asDoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable

@Injectable
class MoreDamageTakenWhileHighHealthService {
    @CommandHandler
    fun on(cmd: DamageCalculationCommand) {
        val attributes = cmd.damagedAttributes[CustomAttributeTypes.MORE_DAMAGE_TAKEN_WHILE_HIGH_HEALTH] ?: return
        if (!cmd.damaged.isHighHealth()) return

        attributes.forEach { attribute ->
            val moreDamage = attribute.rolls[0].asDoubleRoll().getValue()
            cmd.moreDamageTaken.add(moreDamage)
        }
    }
}