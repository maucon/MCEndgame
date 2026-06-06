package de.fuballer.mcendgame.main.component.custom_attribute.effects

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributeUtil.isLowHealth
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asDoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttributeType
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.fuballer.mcendgame.main.messaging.collect_attribute.CollectGenericIncreasedDamageCommand
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.world.entity.LivingEntity

@Injectable
class IncreasedDamageWhileLowHealthService {
    @CommandHandler
    fun on(cmd: DamageCalculationCommand) {
        val damager = cmd.damager as? LivingEntity ?: return
        cmd.increasedDamage.addAll(getIncreasedDamage(damager, cmd.damagerAttributes))
    }

    @CommandHandler
    fun on(cmd: CollectGenericIncreasedDamageCommand) {
        cmd.increased.addAll(getIncreasedDamage(cmd.entity, cmd.attributes))
    }

    private fun getIncreasedDamage(
        entity: LivingEntity,
        attributes: Map<CustomAttributeType, List<CustomAttribute>>,
    ): List<Double> {
        val attr = attributes[CustomAttributeTypes.INCREASED_DAMAGE_WHILE_LOW_HEALTH] ?: return listOf()
        if (!entity.isLowHealth()) return listOf()
        return attr.map { it.rolls[0].asDoubleRoll().getValue() }
    }
}