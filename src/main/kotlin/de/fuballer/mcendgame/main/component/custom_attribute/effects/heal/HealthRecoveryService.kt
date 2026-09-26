package de.fuballer.mcendgame.main.component.custom_attribute.effects.heal

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asDoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getAllCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.messaging.misc.LivingEntityHealthRecoveryCommand
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable

@Injectable
class HealthRecoveryService {
    @CommandHandler
    fun onIncrease(cmd: LivingEntityHealthRecoveryCommand) {
        val attributes = cmd.entity.getAllCustomAttributes()[CustomAttributeTypes.INCREASED_HEALTH_RECOVERY] ?: return
        attributes.forEach {
            cmd.increase.add(it.rolls[0].asDoubleRoll().getValue().toFloat())
        }
    }

    @CommandHandler
    fun onMore(cmd: LivingEntityHealthRecoveryCommand) {
        val attributes = cmd.entity.getAllCustomAttributes()[CustomAttributeTypes.MORE_HEALTH_RECOVERY] ?: return
        attributes.forEach {
            cmd.more.add(it.rolls[0].asDoubleRoll().getValue().toFloat())
        }
    }
}