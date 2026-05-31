package de.fuballer.mcendgame.main.component.custom_attribute.effects.companion

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asDoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getAllCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttributeType
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.fuballer.mcendgame.main.messaging.collect_attribute.CollectGenericIncreasedDamageCommand
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.isCompanion
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.passive.TameableEntity

@Injectable
class CompanionDamageService {
    @CommandHandler
    fun on(cmd: DamageCalculationCommand) {
        val attributes = getOwnerAttributes(cmd.damager)

        attributes[CustomAttributeTypes.COMPANION_ATTACK_DAMAGE]?.let { attr ->
            val attackDamage = attr.sumOf { it.rolls[0].asDoubleRoll().getValue() }
            cmd.attackDamage.add(attackDamage)
        }

        attributes[CustomAttributeTypes.INCREASED_COMPANION_DAMAGE]?.let { attr ->
            val increase = attr.sumOf { it.rolls[0].asDoubleRoll().getValue() }
            cmd.increasedDamage.add(increase)
        }
    }

    @CommandHandler
    fun on(cmd: CollectGenericIncreasedDamageCommand) {
        val attributes = getOwnerAttributes(cmd.entity)[CustomAttributeTypes.INCREASED_COMPANION_DAMAGE] ?: return
        val increase = attributes.sumOf { it.rolls[0].asDoubleRoll().getValue() }
        cmd.increased.add(increase)
    }

    private fun getOwnerAttributes(companion: Entity?): Map<CustomAttributeType, List<CustomAttribute>> {
        if (companion !is LivingEntity
            || !companion.isCompanion()
            || companion !is TameableEntity
        ) return mapOf()

        val owner = companion.owner ?: return mapOf()
        return owner.getAllCustomAttributes()
    }
}