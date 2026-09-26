package de.fuballer.mcendgame.main.component.custom_attribute.effects

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asDoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable

@Injectable
class FlatDamageTakenService {
    @CommandHandler
    fun on(cmd: DamageCalculationCommand) {
        val attributes = cmd.damagedAttributes[CustomAttributeTypes.FLAT_DAMAGE_TAKEN] ?: return

        attributes.forEach { attribute ->
            val flatDamage = attribute.rolls[0].asDoubleRoll().getValue()
            cmd.flatDamageTaken.add(flatDamage)
        }
    }
}