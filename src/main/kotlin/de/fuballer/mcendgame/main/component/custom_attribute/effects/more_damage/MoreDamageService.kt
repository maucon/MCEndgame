package de.fuballer.mcendgame.main.component.custom_attribute.effects.more_damage

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asDoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttributeType
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.fuballer.mcendgame.main.messaging.collect_attribute.CollectGenericMoreDamageCommand
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable

@Injectable
class MoreDamageService {
    @CommandHandler
    fun on(cmd: DamageCalculationCommand) {
        cmd.moreDamage.addAll(getMoreDamage(cmd.damagerAttributes))
    }

    @CommandHandler
    fun on(cmd: CollectGenericMoreDamageCommand) {
        cmd.more.addAll(getMoreDamage(cmd.attributes))
    }

    private fun getMoreDamage(
        attributes: Map<CustomAttributeType, List<CustomAttribute>>,
    ): List<Double> {
        val attr = attributes[CustomAttributeTypes.MORE_DAMAGE] ?: return listOf()
        return attr.map { it.rolls[0].asDoubleRoll().getValue() }
    }
}