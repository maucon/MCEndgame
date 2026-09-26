package de.fuballer.mcendgame.main.component.item.custom.aspect.item.kin

import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItems
import de.fuballer.mcendgame.main.messaging.dungeon.DungeonGenerateBanditsCommand
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable

@Injectable
object AspectOfKinService {
    @CommandHandler
    fun generateDungeonBandits(cmd: DungeonGenerateBanditsCommand) {
        if (!cmd.aspects.containsKey(AspectItems.ASPECT_OF_KIN)) return
        cmd.pairs = true
    }
}