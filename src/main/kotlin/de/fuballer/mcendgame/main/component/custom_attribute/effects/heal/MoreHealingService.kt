package de.fuballer.mcendgame.main.component.custom_attribute.effects.heal

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asDoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.messaging.collect_attribute.CollectHealFactorCommand
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable

@Injectable
class MoreHealingService {
    @CommandHandler
    fun on(cmd: CollectHealFactorCommand) {
        val more = cmd.attributes[CustomAttributeTypes.MORE_HEALING]?.map { it.rolls[0].asDoubleRoll().getValue() } ?: return
        cmd.more.addAll(more)
    }
}