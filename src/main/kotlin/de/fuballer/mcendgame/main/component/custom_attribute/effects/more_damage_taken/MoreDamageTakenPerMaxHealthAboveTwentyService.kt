package de.fuballer.mcendgame.main.component.custom_attribute.effects.more_damage_taken

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asDoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable
import kotlin.math.max

@Injectable
class MoreDamageTakenPerMaxHealthAboveTwentyService {
    @CommandHandler
    fun on(cmd: DamageCalculationCommand) {
        val attributes = cmd.damagedAttributes[CustomAttributeTypes.MORE_DAMAGE_TAKEN_PER_MAX_HEALTH_ABOVE_TWENTY] ?: return

        val extraHealth = max((cmd.damaged.maxHealth - 20).toInt(), 0)
        for (attribute in attributes) {
            val moreDamageTaken = attribute.rolls[0].asDoubleRoll().getValue() * extraHealth
            cmd.moreDamageTaken.add(moreDamageTaken)
        }
    }
}