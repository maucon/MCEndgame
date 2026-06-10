package de.fuballer.mcendgame.main.component.custom_attribute.effects.dodge

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asDoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.damage.dodge.DodgeCalculationCommand
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable

@Injectable
class DodgePerMaxHeartBelowTenService {
    @CommandHandler
    fun on(cmd: DodgeCalculationCommand) {
        val attributes = cmd.damagedAttributes[CustomAttributeTypes.DODGE_PER_MAX_HEART_BELOW_TEN] ?: return

        val missingHearts = (10 - cmd.damaged.maxHealth / 2).toInt()

        for (attribute in attributes) {
            cmd.dodgeChances += attribute.rolls[0].asDoubleRoll().getValue() * missingHearts
        }
    }
}