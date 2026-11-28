package de.fuballer.mcendgame.main.component.custom_attribute.effects.crit

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable
import kotlin.collections.sumOf

@Injectable
class CriticalDamageMultiplierService {
    @CommandHandler
    fun on(cmd: DamageCalculationCommand) {
        val attributes = cmd.damagerAttributes[CustomAttributeTypes.CRITICAL_DAMAGE_MULTIPLIER] ?: return

        val critMulti = attributes.sumOf { (it.rolls[0] as DoubleRoll).getValue() }
        cmd.criticalDamageMulti.add(critMulti)
    }
}