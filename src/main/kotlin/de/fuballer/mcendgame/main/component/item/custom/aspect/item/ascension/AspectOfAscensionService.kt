package de.fuballer.mcendgame.main.component.item.custom.aspect.item.ascension

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.addCustomAttributes
import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItems
import de.fuballer.mcendgame.main.messaging.dungeon.DungeonEnemiesGeneratedCommand
import de.fuballer.mcendgame.main.messaging.dungeon.EliteAspectDropCommand
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.isElite
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable

@Injectable
object AspectOfAscensionService {
    @CommandHandler
    fun on(cmd: EliteAspectDropCommand) {
        if (!cmd.aspects.containsKey(AspectItems.ASPECT_OF_ASCENSION)) return
        cmd.count++
    }

    @CommandHandler
    fun on(cmd: DungeonEnemiesGeneratedCommand) {
        if (!cmd.aspects.containsKey(AspectItems.ASPECT_OF_ASCENSION)) return

        cmd.enemies.filter { it.isElite() }
            .forEach { elite ->
                elite.addCustomAttributes(
                    AspectOfAscension.MORE_DAMAGE_TAKEN_ATTRIBUTE.roll(),
                    AspectOfAscension.MORE_DAMAGE_DEALT_ATTRIBUTE.roll(),
                )
            }
    }
}