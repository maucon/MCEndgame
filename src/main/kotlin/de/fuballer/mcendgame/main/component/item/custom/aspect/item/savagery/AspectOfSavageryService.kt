package de.fuballer.mcendgame.main.component.item.custom.aspect.item.savagery

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.addCustomAttributes
import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItems
import de.fuballer.mcendgame.main.messaging.dungeon.DungeonEnemiesGeneratedCommand
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable

@Injectable
object AspectOfSavageryService {
    @CommandHandler
    fun on(command: DungeonEnemiesGeneratedCommand) {
        val amount = command.aspects[AspectItems.ASPECT_OF_SAVAGERY] ?: return

        command.enemies.forEach { enemy ->
            repeat(amount) {
                enemy.addCustomAttributes(
                    AspectOfSavagery.MORE_DAMAGE_ATTRIBUTE.roll(),
                    AspectOfSavagery.INCREASED_LOOT_ATTRIBUTE.roll(),
                )
            }
        }
    }
}