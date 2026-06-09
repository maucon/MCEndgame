package de.fuballer.mcendgame.main.component.custom_attribute.effects.dodge

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asDoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asIntRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.damage.dodge.DodgeCalculationCommand
import de.fuballer.mcendgame.main.messaging.misc.LivingEntityDodgedEvent
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.hasDodged
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.updateDodged
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber

@Injectable
class DodgeIfNotDodgedInLastSecondsService {
    @CommandHandler
    fun on(cmd: DodgeCalculationCommand) {
        val attributes = cmd.damagedAttributes[CustomAttributeTypes.DODGE_IF_NOT_DODGED_IN_LAST_SECONDS] ?: return

        for (attribute in attributes) {
            val ticks = attribute.rolls[1].asIntRoll().getValue() * 20
            if (cmd.damaged.hasDodged(ticks)) continue

            cmd.dodgeChances += attribute.rolls[0].asDoubleRoll().getValue()
        }
    }

    @EventSubscriber(sync = true)
    fun on(event: LivingEntityDodgedEvent) {
        event.entity.updateDodged()
    }
}