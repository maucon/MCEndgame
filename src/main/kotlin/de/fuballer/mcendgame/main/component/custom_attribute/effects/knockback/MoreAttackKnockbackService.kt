package de.fuballer.mcendgame.main.component.custom_attribute.effects.knockback

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asDoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getAllCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.messaging.misc.LivingEntityKnockbackLivingEntityCommand
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable

@Injectable
class MoreAttackKnockbackService {
    @CommandHandler
    fun on(cmd: LivingEntityKnockbackLivingEntityCommand) {
        val attributes = cmd.attacker.getAllCustomAttributes()[CustomAttributeTypes.MORE_ATTACK_KNOCKBACK] ?: return
        attributes.forEach { cmd.power *= 1 + it.rolls[0].asDoubleRoll().getValue() }
    }
}