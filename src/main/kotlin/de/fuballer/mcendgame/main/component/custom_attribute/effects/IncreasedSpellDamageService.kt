package de.fuballer.mcendgame.main.component.custom_attribute.effects

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asDoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttributeType
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.fuballer.mcendgame.main.messaging.collect_attribute.CollectSpellDamageCommand
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable

@Injectable
class IncreasedSpellDamageService {
    @CommandHandler
    fun on(cmd: DamageCalculationCommand) {
        cmd.increasedSpellDamage.addAll(getIncreasedDamage(cmd.damagerAttributes))
    }

    @CommandHandler
    fun on(cmd: CollectSpellDamageCommand) {
        cmd.increased.addAll(getIncreasedDamage(cmd.attributes))
    }

    private fun getIncreasedDamage(
        attributes: Map<CustomAttributeType, List<CustomAttribute>>,
    ): List<Double> {
        val attr = attributes[CustomAttributeTypes.INCREASED_SPELL_DAMAGE] ?: return listOf()
        return attr.map { it.rolls[0].asDoubleRoll().getValue() }
    }
}