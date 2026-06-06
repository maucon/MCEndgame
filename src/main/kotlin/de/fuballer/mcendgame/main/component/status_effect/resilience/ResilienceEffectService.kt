package de.fuballer.mcendgame.main.component.status_effect.resilience

import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.fuballer.mcendgame.main.component.status_effect.CustomStatusEffects
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable

@Injectable
class ResilienceEffectService {
    @CommandHandler
    fun on(cmd: DamageCalculationCommand) {
        val amplifier = cmd.damaged.getEffect(CustomStatusEffects.RESILIENCE)?.amplifier ?: return

        cmd.moreDamageTaken.add((amplifier + 1) * -0.02)
    }
}