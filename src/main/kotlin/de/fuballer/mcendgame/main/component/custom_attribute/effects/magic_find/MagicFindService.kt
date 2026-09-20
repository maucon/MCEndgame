package de.fuballer.mcendgame.main.component.custom_attribute.effects.magic_find

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asIntRoll
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getAllCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.messaging.misc.MagicFindCommand
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable

@Injectable
class MagicFindService {
    @CommandHandler
    fun on(cmd: MagicFindCommand) {
        val attributes = cmd.entity.getAllCustomAttributes()[CustomAttributeTypes.MAGIC_FIND] ?: return
        cmd.magicFind += attributes.sumOf { it.rolls[0].asIntRoll().getValue() }
    }
}