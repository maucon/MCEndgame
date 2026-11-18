package de.fuballer.mcendgame.main.component.custom_attribute.effects.projectile

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asDoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable

@Injectable
class FlatProjectileDamageService {
    @CommandHandler
    fun on(cmd: DamageCalculationCommand) {
        if (!cmd.isProjectile) return
        val attributes = cmd.damagerAttributes[CustomAttributeTypes.FLAT_PROJECTILE_DAMAGE] ?: return

        attributes.forEach { attribute ->
            val flatProjectileDamage = attribute.rolls[0].asDoubleRoll().getValue()
            cmd.flatProjectileDamage.add(flatProjectileDamage)
        }
    }
}