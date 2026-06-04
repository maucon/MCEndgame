package de.fuballer.mcendgame.main.component.custom_attribute.effects

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asIntRoll
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getAllCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.messaging.misc.MagicFindCommand
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.world.effect.MobEffects

@Injectable
class MagicFindWhilePoisonedService {
    @CommandHandler
    fun on(cmd: MagicFindCommand) {
        val entity = cmd.entity
        if (!entity.hasEffect(MobEffects.POISON)) return

        val attributes = entity.getAllCustomAttributes()[CustomAttributeTypes.MAGIC_FIND_WHILE_POISONED] ?: return
        cmd.magicFind += attributes.sumOf { it.rolls[0].asIntRoll().getValue() }
    }
}