package de.fuballer.mcendgame.main.component.custom_attribute.effects.dodge

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asDoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.damage.dodge.DodgeCalculationCommand
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable

@Injectable
class ProjectileDodgeService {
    @CommandHandler
    fun on(cmd: DodgeCalculationCommand) {
        if (!cmd.isProjectile) return
        val attributes = cmd.damagedAttributes[CustomAttributeTypes.PROJECTILE_DODGE] ?: return
        cmd.dodgeChances.addAll(attributes.map { it.rolls[0].asDoubleRoll().getValue() })
    }
}