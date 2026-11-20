package de.fuballer.mcendgame.main.component.custom_attribute.effects

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asDoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.fuballer.mcendgame.main.component.damage.custom_type.CustomDamageTypes
import de.fuballer.mcendgame.main.util.extension.DamageTypeExtension.isOf
import de.fuballer.mcendgame.main.util.extension.mixin.PlayerEntityMixinExtension.wasLastAttackSweeping
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.world.entity.player.Player

@Injectable
class IncreasedSweepingAttackDamage {
    @CommandHandler
    fun on(cmd: DamageCalculationCommand) {
        val attributes = cmd.damagerAttributes[CustomAttributeTypes.INCREASED_SWEEPING_DAMAGE] ?: return
        val player = cmd.damager as? Player ?: return

        if (!player.wasLastAttackSweeping() && !cmd.type.isOf(CustomDamageTypes.SWEEPING)) return

        attributes.forEach { attribute ->
            val increased = attribute.rolls[0].asDoubleRoll().getValue()
            cmd.increasedAttackDamage.add(increased)
        }
    }
}