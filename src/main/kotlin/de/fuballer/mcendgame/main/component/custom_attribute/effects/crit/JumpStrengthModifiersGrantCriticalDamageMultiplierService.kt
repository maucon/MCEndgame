package de.fuballer.mcendgame.main.component.custom_attribute.effects.crit

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributeUtil
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes

@Injectable
class JumpStrengthModifiersGrantCriticalDamageMultiplierService {
    @CommandHandler
    fun on(cmd: DamageCalculationCommand) {
        val damager = cmd.damager as? LivingEntity ?: return

        val values = CustomAttributeUtil.getVanillaAttributeAlsoAffectOtherValues(
            damager,
            cmd.damagerAttributes,
            CustomAttributeTypes.INCREASED_JUMP_STRENGTH_MODIFIERS_GRANT_CRITICAL_DAMAGE_MULTIPLIER,
            Attributes.JUMP_STRENGTH,
            AttributeModifier.Operation.ADD_MULTIPLIED_BASE,
        )
        cmd.criticalDamageMulti.addAll(values)
    }
}