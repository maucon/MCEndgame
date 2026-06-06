package de.fuballer.mcendgame.main.component.custom_attribute.effects

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asDoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttributeType
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.fuballer.mcendgame.main.messaging.collect_attribute.CollectGenericMoreDamageCommand
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.world.entity.LivingEntity

@Injectable
class MoreDamagePerMissingHeartService {
    @CommandHandler
    fun on(cmd: DamageCalculationCommand) {
        val damager = cmd.damager as? LivingEntity ?: return
        cmd.moreDamage.addAll(getMoreDamage(damager, cmd.damagerAttributes))
    }

    @CommandHandler
    fun on(cmd: CollectGenericMoreDamageCommand) {
        cmd.more.addAll(getMoreDamage(cmd.entity, cmd.attributes))
    }

    private fun getMoreDamage(
        livingEntity: LivingEntity,
        attributes: Map<CustomAttributeType, List<CustomAttribute>>,
    ): List<Double> {
        val attr = attributes[CustomAttributeTypes.MORE_DAMAGE_PER_MISSING_HEART] ?: return listOf()
        val missingHearts = (livingEntity.maxHealth - livingEntity.health) / 2
        return attr.map { it.rolls[0].asDoubleRoll().getValue() * missingHearts }
    }
}