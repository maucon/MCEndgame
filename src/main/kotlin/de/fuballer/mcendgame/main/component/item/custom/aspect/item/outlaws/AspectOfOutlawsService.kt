package de.fuballer.mcendgame.main.component.item.custom.aspect.item.outlaws

import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItems
import de.fuballer.mcendgame.main.messaging.dungeon.DungeonGenerateBanditsCommand
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable

@Injectable
object AspectOfOutlawsService {
    @CommandHandler
    fun generateDungeonBandits(cmd: DungeonGenerateBanditsCommand) {
        val count = cmd.aspects[AspectItems.ASPECT_OF_OUTLAWS] ?: return
        cmd.addBandits(AspectOfOutlaws.ADDITIONAL_BANDITS * count)
    }
}