package de.fuballer.mcendgame.main.component.custom_attribute.effects.status_effects

import de.fuballer.mcendgame.main.component.status_effect.CustomStatusEffects
import de.fuballer.mcendgame.main.component.status_effect.HowlOfTheWolfEffect
import de.fuballer.mcendgame.main.component.status_effect.MoltenRoarEffect
import de.fuballer.mcendgame.main.component.status_effect.ResilienceEffect
import de.fuballer.mcendgame.main.component.status_effect.VerdantEchoEffect
import de.fuballer.mcendgame.main.component.status_effect.beastweaver_blessings.BlessingOfTheEagleEffect
import de.fuballer.mcendgame.main.component.status_effect.beastweaver_blessings.BlessingOfTheRhinoEffect
import de.fuballer.mcendgame.main.component.status_effect.beastweaver_blessings.BlessingOfTheSerpentEffect
import de.fuballer.mcendgame.main.component.status_effect.beastweaver_blessings.BlessingOfTheWolfEffect
import de.fuballer.mcendgame.main.messaging.misc.CollectCustomAttributesCommand
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable

private val EFFECTS = mapOf(
    CustomStatusEffects.MOLTEN_ROAR to MoltenRoarEffect::getCustomAttributes,
    CustomStatusEffects.RESILIENCE to ResilienceEffect::getCustomAttributes,
    CustomStatusEffects.VERDANT_ECHO to VerdantEchoEffect::getCustomAttributes,
    CustomStatusEffects.HOWL_OF_THE_WOLF to HowlOfTheWolfEffect::getCustomAttributes,
    CustomStatusEffects.BLESSING_OF_THE_EAGLE to BlessingOfTheEagleEffect::getCustomAttributes,
    CustomStatusEffects.BLESSING_OF_THE_RHINO to BlessingOfTheRhinoEffect::getCustomAttributes,
    CustomStatusEffects.BLESSING_OF_THE_SERPENT to BlessingOfTheSerpentEffect::getCustomAttributes,
    CustomStatusEffects.BLESSING_OF_THE_WOLF to BlessingOfTheWolfEffect::getCustomAttributes,
)

@Injectable
class CustomAttributeStatusEffectService {
    @CommandHandler
    fun on(cmd: CollectCustomAttributesCommand) {
        val entity = cmd.entity

        EFFECTS.forEach { (effect, getCustomAttributes) ->
            val amplifier = entity.getEffect(effect)?.amplifier ?: return@forEach
            cmd.customAttributes.addAll(getCustomAttributes(amplifier))
        }
    }
}