package de.fuballer.mcendgame.main.component.custom_attribute.effects.dual_wield

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asDoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttributeType
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.fuballer.mcendgame.main.messaging.collect_attribute.CollectGenericMoreDamageCommand
import de.fuballer.mcendgame.main.util.extension.EntityExtension.isDualWielding
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.world.entity.LivingEntity

@Injectable
class DualWieldService {
    @CommandHandler
    fun on(cmd: DamageCalculationCommand) {
        val livingEntity = cmd.damager as? LivingEntity ?: return
        cmd.moreDamage.addAll(getMoreMultipliers(livingEntity, cmd.damagerAttributes))
    }

    @CommandHandler
    fun on(cmd: CollectGenericMoreDamageCommand) {
        cmd.more.addAll(getMoreMultipliers(cmd.entity, cmd.attributes))
    }

    private fun getMoreMultipliers(
        entity: LivingEntity,
        attributes: Map<CustomAttributeType, List<CustomAttribute>>,
    ): List<Double> {
        if (!entity.isDualWielding()) return emptyList()

        val attr = attributes[CustomAttributeTypes.MORE_DAMAGE_DUAL_WIELD] ?: return emptyList()
        return attr.map { it.rolls[0].asDoubleRoll().getValue() }
    }
}