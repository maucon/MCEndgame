package de.fuballer.mcendgame.main.component.custom_attribute.effects

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.messaging.collect_attribute.CollectSpellDamageCommand
import de.fuballer.mcendgame.main.messaging.collect_attribute.CollectHealFactorCommand
import de.maucon.mauconframework.command.CommandGateway
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable

@Injectable
class IncreasedHealingPerSpellDamageService {
    @CommandHandler
    fun on(cmd: CollectHealFactorCommand) {
        val attributes = cmd.attributes[CustomAttributeTypes.INCREASED_HEALING_PER_SPELL_DAMAGE] ?: return
        val increaseSum = attributes.sumOf { (it.rolls[0] as DoubleRoll).getValue() }

        val collectSpellCommand = CollectSpellDamageCommand(cmd.entity)
        val spellDamage = CommandGateway.apply(collectSpellCommand).calculate()

        cmd.increased.add(increaseSum * spellDamage)
    }
}