package de.fuballer.mcendgame.main.component.custom_attribute.effects

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getAllCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.messaging.misc.LookAtEntityGoalCommand
import de.fuballer.mcendgame.main.messaging.misc.MobEntityTargetCommand
import de.fuballer.mcendgame.main.util.extension.EntityExtension.isBehind
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.world.entity.LivingEntity

@Injectable
class StealthService {
    @CommandHandler
    fun on(cmd: MobEntityTargetCommand) {
        val target = cmd.target ?: return
        if (!hasStealthAndIsBehind(target, cmd.entity)) return
        cmd.canTarget = false
    }

    @CommandHandler
    fun on(cmd: LookAtEntityGoalCommand) {
        if (!hasStealthAndIsBehind(cmd.target, cmd.mob)) return
        cmd.canLookAt = false
    }

    private fun hasStealthAndIsBehind(entity: LivingEntity, target: LivingEntity) =
        entity.getAllCustomAttributes().contains(CustomAttributeTypes.STEALTH) && entity.isBehind(target)
}