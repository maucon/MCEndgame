package de.fuballer.mcendgame.main.component.custom_attribute.effects

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asDoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable

@Injectable
class MoreDamageTakenPerMissingHeart {
    @CommandHandler
    fun on(cmd: DamageCalculationCommand) {
        val attributes = cmd.damagedAttributes[CustomAttributeTypes.MORE_DAMAGE_TAKEN_PER_MISSING_HEART] ?: return
        val missingHearts = (cmd.damaged.maxHealth - cmd.damaged.health) / 2
        val moreDamageTaken = attributes.map { it.rolls[0].asDoubleRoll().getValue() * missingHearts }
        cmd.moreDamageTaken.addAll(moreDamageTaken)
    }
}