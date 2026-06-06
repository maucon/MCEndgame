package de.fuballer.mcendgame.main.component.status_effect.verdant_echo

import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.fuballer.mcendgame.main.component.status_effect.CustomStatusEffects
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable

private const val BASE_MORE_DAMAGE_TAKEN = -0.1
private const val MORE_DAMAGE_TAKEN_PER_AMPLIFIER = -0.05

@Injectable
class VerdantEchoEffectService {
    @CommandHandler
    fun on(cmd: DamageCalculationCommand) {
        val amplifier = cmd.damaged.getEffect(CustomStatusEffects.VERDANT_ECHO)?.amplifier ?: return
        cmd.moreDamageTaken.add(BASE_MORE_DAMAGE_TAKEN + (amplifier * MORE_DAMAGE_TAKEN_PER_AMPLIFIER))
    }
}