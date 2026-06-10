package de.fuballer.mcendgame.main.component.custom_attribute.effects

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asDoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttributeType
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.fuballer.mcendgame.main.component.damage.dodge.DodgeCalculationCommand
import de.fuballer.mcendgame.main.messaging.collect_attribute.CollectElementalDamageCommand
import de.fuballer.mcendgame.main.messaging.collect_attribute.CollectGenericIncreasedDamageCommand
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity

@Injectable
class AttributesWhilePoisonedService {
    @CommandHandler
    fun on(cmd: DamageCalculationCommand) {
        if (cmd.damaged.hasEffect(MobEffects.POISON)) {
            cmd.moreDamageTaken.addAll(getDoubleValues(cmd.damagedAttributes, CustomAttributeTypes.MORE_DAMAGE_TAKEN_WHILE_POISONED))
        }

        val damager = cmd.damager as? LivingEntity ?: return
        if (!damager.hasEffect(MobEffects.POISON)) return

        cmd.increasedDamage.addAll(getDoubleValues(cmd.damagerAttributes, CustomAttributeTypes.INCREASED_DAMAGE_WHILE_POISONED))
        cmd.increasedElementalDamage.addAll(getDoubleValues(cmd.damagerAttributes, CustomAttributeTypes.INCREASED_ELEMENTAL_DAMAGE_WHILE_POISONED))
    }

    @CommandHandler
    fun on(cmd: CollectGenericIncreasedDamageCommand) {
        if (!cmd.entity.hasEffect(MobEffects.POISON)) return
        cmd.increased.addAll(getDoubleValues(cmd.attributes, CustomAttributeTypes.INCREASED_DAMAGE_WHILE_POISONED))
    }

    @CommandHandler
    fun on(cmd: CollectElementalDamageCommand) {
        if (!cmd.entity.hasEffect(MobEffects.POISON)) return
        cmd.increased.addAll(getDoubleValues(cmd.attributes, CustomAttributeTypes.INCREASED_ELEMENTAL_DAMAGE_WHILE_POISONED))
    }

    private fun getDoubleValues(
        attributes: Map<CustomAttributeType, List<CustomAttribute>>,
        attributeType: CustomAttributeType,
    ): List<Double> {
        val attr = attributes[attributeType] ?: return listOf()
        return attr.map { it.rolls[0].asDoubleRoll().getValue() }
    }

    @CommandHandler
    fun dodge(cmd: DodgeCalculationCommand) {
        if (!cmd.damaged.hasEffect(MobEffects.POISON)) return

        val attributes = cmd.damagedAttributes[CustomAttributeTypes.DODGE_WHILE_POISONED] ?: return
        cmd.dodgeChances.addAll(attributes.map { it.rolls[0].asDoubleRoll().getValue() })
    }
}