package de.fuballer.mcendgame.main.component.item.custom.aspect.item.special.key

import de.fuballer.mcendgame.main.messaging.dungeon.DungeonGenerateEnemiesCommand
import de.fuballer.mcendgame.main.messaging.dungeon.DungeonPlayerDecreaseProgressCommand
import de.fuballer.mcendgame.main.messaging.dungeon.DungeonPlayerIncreaseProgressCommand
import de.fuballer.mcendgame.main.messaging.dungeon.SelectDungeonTypeCommand
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable

@Injectable
object KeyService {
    @CommandHandler
    fun onSelectDungeonType(cmd: SelectDungeonTypeCommand) {
        cmd.aspects.keys.forEach { aspect ->
            val key = aspect as? KeyItem ?: return@forEach
            cmd.dungeonType = key.dungeonType
            return
        }
    }

    @CommandHandler
    fun onIncreaseProgress(cmd: DungeonPlayerIncreaseProgressCommand) {
        cmd.aspects.keys.forEach { aspect ->
            val key = aspect as? KeyItem ?: return@forEach
            cmd.progressBlocked = true
            return
        }
    }

    @CommandHandler
    fun onDecreaseProgress(cmd: DungeonPlayerDecreaseProgressCommand) {
        cmd.aspects.keys.forEach { aspect ->
            val key = aspect as? KeyItem ?: return@forEach
            cmd.decreaseBlocked = true
            return
        }
    }

    @CommandHandler
    fun onGenerateDungeonEnemies(cmd: DungeonGenerateEnemiesCommand) {
        cmd.aspects.keys.forEach { aspect ->
            val key = aspect as? KeyItem ?: return@forEach
            cmd.specialEnemiesDisabled = true
            return
        }
    }
}