package de.fuballer.mcendgame.main.component.dungeon.misc

import de.fuballer.mcendgame.main.messaging.misc.ItemEntityDamageCommand
import de.fuballer.mcendgame.main.util.extension.WorldExtension.isDungeonWorld
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.world.damagesource.DamageTypes

@Injectable
class ImmuneItemEntityService {
    @CommandHandler
    fun on(cmd: ItemEntityDamageCommand) {
        if (!cmd.world.isDungeonWorld()) return
        if (cmd.source.typeHolder() == DamageTypes.FELL_OUT_OF_WORLD) return

        cmd.ignoresDamage = true
    }
}