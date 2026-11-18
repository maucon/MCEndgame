package de.fuballer.mcendgame.main.component.custom_attribute.effects

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributeUtil
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.fuballer.mcendgame.main.messaging.collect_attribute.CollectGenericIncreasedAndMoreDamageCommand
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes

@Injectable
class MovementSpeedModifiersAffectDamageService {
    @CommandHandler
    fun on(cmd: DamageCalculationCommand) {
        val damager = cmd.damager as? LivingEntity ?: return

        val increaseFactors = CustomAttributeUtil.getVanillaAttributeAlsoAffectOtherValues(
            damager,
            cmd.damagerAttributes,
            CustomAttributeTypes.INCREASED_MOVEMENT_SPEED_MODIFIERS_AFFECT_DAMAGE,
            Attributes.MOVEMENT_SPEED,
            AttributeModifier.Operation.ADD_MULTIPLIED_BASE
        )
        cmd.increasedDamage.addAll(increaseFactors)

        val moreFactors = CustomAttributeUtil.getVanillaAttributeAlsoAffectOtherValues(
            damager,
            cmd.damagerAttributes,
            CustomAttributeTypes.MORE_MOVEMENT_SPEED_MODIFIERS_AFFECT_DAMAGE,
            Attributes.MOVEMENT_SPEED,
            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        )
        cmd.moreDamage.addAll(moreFactors)
    }

    @CommandHandler
    fun on(cmd: CollectGenericIncreasedAndMoreDamageCommand) {
        val increaseFactors = CustomAttributeUtil.getVanillaAttributeAlsoAffectOtherValues(
            cmd.entity,
            cmd.attributes,
            CustomAttributeTypes.INCREASED_MOVEMENT_SPEED_MODIFIERS_AFFECT_DAMAGE,
            Attributes.MOVEMENT_SPEED,
            AttributeModifier.Operation.ADD_MULTIPLIED_BASE
        )
        cmd.increased.addAll(increaseFactors)

        val moreFactors = CustomAttributeUtil.getVanillaAttributeAlsoAffectOtherValues(
            cmd.entity,
            cmd.attributes,
            CustomAttributeTypes.MORE_MOVEMENT_SPEED_MODIFIERS_AFFECT_DAMAGE,
            Attributes.MOVEMENT_SPEED,
            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        )
        cmd.more.addAll(moreFactors)
    }
}