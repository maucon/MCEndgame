package de.fuballer.mcendgame.main.component.custom_attribute.effects.increased_damage

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributeUtil.isFullHealth
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asDoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable

@Injectable
class IncreasedDamageAgainstFullHealthService {
    @CommandHandler
    fun on(cmd: DamageCalculationCommand) {
        val attributes = cmd.damagerAttributes[CustomAttributeTypes.INCREASED_DAMAGE_AGAINST_FULL_HEALTH] ?: return
        if (!cmd.damaged.isFullHealth()) return

        attributes.forEach { attribute ->
            val increasedDamage = attribute.rolls[0].asDoubleRoll().getValue()
            cmd.increasedDamage.add(increasedDamage)
        }
    }
}