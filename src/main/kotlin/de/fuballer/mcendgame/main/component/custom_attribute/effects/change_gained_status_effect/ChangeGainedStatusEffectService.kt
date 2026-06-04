package de.fuballer.mcendgame.main.component.custom_attribute.effects.change_gained_status_effect

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asStringRoll
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getAllCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.messaging.misc.GainStatusEffectCommand
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.world.effect.MobEffectInstance

@Injectable
class ChangeGainedStatusEffectService {
    @CommandHandler
    fun on(cmd: GainStatusEffectCommand) {
        val attributes = cmd.entity.getAllCustomAttributes()[CustomAttributeTypes.CHANGE_GAINED_STATUS_EFFECT] ?: return
        val effectConversions =
            attributes.associate { GainedStatusEffect.fromString(it.rolls[0].asStringRoll().getValue()) to GainedStatusEffect.fromString(it.rolls[1].asStringRoll().getValue()) }

        val originalEffect = cmd.effect
        val relevantConversions = effectConversions.filter { it.key?.effect == originalEffect.effect }
        if (relevantConversions.isEmpty()) return

        val chosenEffect = relevantConversions[relevantConversions.keys.random()]!!
        cmd.effect = MobEffectInstance(
            chosenEffect.effect,
            originalEffect.duration,
            originalEffect.amplifier,
            originalEffect.isAmbient,
            originalEffect.isVisible,
            originalEffect.showIcon()
        )
    }
}