package de.fuballer.mcendgame.main.component.custom_attribute.effects.heal

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asDoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.messaging.collect_attribute.CollectHealFactorCommand
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable
import kotlin.random.Random

@Injectable
class ChanceToHealMoreService {
    @CommandHandler
    fun on(cmd: CollectHealFactorCommand) {
        val attributes = cmd.attributes[CustomAttributeTypes.CHANCE_TO_HEAL_MORE] ?: return
        val more = attributes.filter { Random.Default.nextDouble() <= it.rolls[0].asDoubleRoll().getValue() }
            .map { it.rolls[1].asDoubleRoll().getValue() }
        cmd.more.addAll(more)
    }
}