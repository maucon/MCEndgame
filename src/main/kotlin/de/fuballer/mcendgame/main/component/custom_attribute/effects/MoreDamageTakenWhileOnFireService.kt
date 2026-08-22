package de.fuballer.mcendgame.main.component.custom_attribute.effects

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asDoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable

@Injectable
class MoreDamageTakenWhileOnFireService {
    @CommandHandler
    fun on(cmd: DamageCalculationCommand) {
        if (!cmd.damaged.isOnFire) return
        val attributes = cmd.damagedAttributes[CustomAttributeTypes.MORE_DAMAGE_TAKEN_WHILE_ON_FIRE] ?: return

        attributes.forEach { attribute ->
            val moreDamage = attribute.rolls[0].asDoubleRoll().getValue()
            cmd.moreDamageTaken.add(moreDamage)
        }
    }
}