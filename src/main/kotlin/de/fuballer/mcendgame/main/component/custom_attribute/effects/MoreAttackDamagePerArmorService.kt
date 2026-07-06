package de.fuballer.mcendgame.main.component.custom_attribute.effects

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asDoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttributes

@Injectable
class MoreAttackDamagePerArmorService {
    @CommandHandler
    fun on(cmd: DamageCalculationCommand) {
        val attributes = cmd.damagerAttributes[CustomAttributeTypes.MORE_ATTACK_DAMAGE_PER_ARMOR] ?: return
        val damager = cmd.damager as? LivingEntity ?: return
        val armor = damager.getAttributeValue(EntityAttributes.GENERIC_ARMOR)
        attributes.forEach {
            val damagePerArmor = it.rolls[0].asDoubleRoll().getValue()
            cmd.moreAttackDamage.add(armor * damagePerArmor)
        }
    }
}