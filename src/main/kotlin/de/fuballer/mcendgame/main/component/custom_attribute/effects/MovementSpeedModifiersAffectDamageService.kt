package de.fuballer.mcendgame.main.component.custom_attribute.effects

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asDoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttributeType
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.fuballer.mcendgame.main.messaging.collect_attribute.CollectGenericIncreasedAndMoreDamageCommand
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes

@Injectable
class MovementSpeedModifiersAffectDamageService {
    @CommandHandler
    fun on(cmd: DamageCalculationCommand) {
        val damager = cmd.damager as? LivingEntity ?: return

        val increaseFactors = getFactors(
            damager,
            cmd.damagerAttributes,
            CustomAttributeTypes.INCREASED_MOVEMENT_SPEED_MODIFIERS_AFFECT_DAMAGE,
            EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
        )
        cmd.increasedDamage.addAll(increaseFactors)

        val moreFactors = getFactors(
            damager,
            cmd.damagerAttributes,
            CustomAttributeTypes.MORE_MOVEMENT_SPEED_MODIFIERS_AFFECT_DAMAGE,
            EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        )
        cmd.moreDamage.addAll(moreFactors)
    }

    @CommandHandler
    fun on(cmd: CollectGenericIncreasedAndMoreDamageCommand) {
        val increaseFactors = getFactors(
            cmd.entity,
            cmd.attributes,
            CustomAttributeTypes.INCREASED_MOVEMENT_SPEED_MODIFIERS_AFFECT_DAMAGE,
            EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
        )
        cmd.increased.addAll(increaseFactors)

        val moreFactors = getFactors(
            cmd.entity,
            cmd.attributes,
            CustomAttributeTypes.MORE_MOVEMENT_SPEED_MODIFIERS_AFFECT_DAMAGE,
            EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        )
        cmd.more.addAll(moreFactors)
    }

    private fun getFactors(
        entity: LivingEntity,
        customAttributes: Map<CustomAttributeType, List<CustomAttribute>>,
        attributeType: CustomAttributeType,
        operation: EntityAttributeModifier.Operation,
    ): List<Double> {
        val attr = customAttributes[attributeType] ?: return listOf()
        val effectiveness = attr.sumOf { it.rolls[0].asDoubleRoll().getValue() }

        val movementSpeedInstance = entity.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED) ?: return listOf()
        return movementSpeedInstance.modifiers
            .filter { it.operation == operation }
            .map { it.value * effectiveness }
            .toList()
    }
}