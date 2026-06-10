package de.fuballer.mcendgame.main.component.custom_attribute.effects.dodge

import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.damage.dodge.DodgeCalculationCommand
import de.fuballer.mcendgame.main.component.tags.CustomTags
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable

@Injectable
class MeleeDamageCanNotBeDodgedService {
    @CommandHandler
    fun on(cmd: DodgeCalculationCommand) {
        if (!cmd.damagerAttributes.contains(CustomAttributeTypes.MELEE_DAMAGE_CAN_NOT_BE_DODGED)) return
        if (!cmd.source.`is`(CustomTags.MELEE_ATTACK)) return
        cmd.canBeDodged = false
    }
}