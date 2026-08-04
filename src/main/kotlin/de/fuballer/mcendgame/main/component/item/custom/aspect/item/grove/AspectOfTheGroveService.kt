package de.fuballer.mcendgame.main.component.item.custom.aspect.item.grove

import de.fuballer.mcendgame.main.component.dungeon.type.DungeonType
import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItems
import de.fuballer.mcendgame.main.component.item.custom.crystal.CrystalItems
import de.fuballer.mcendgame.main.messaging.dungeon.DungeonFinalBossDeathEvent
import de.fuballer.mcendgame.main.messaging.dungeon.DungeonGenerateCommand
import de.fuballer.mcendgame.main.messaging.dungeon.SelectDungeonTypeCommand
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.getDungeonAspects
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.server.level.ServerLevel
import kotlin.math.max

@Injectable
object AspectOfTheGroveService {
    @CommandHandler
    fun onSelectDungeonType(cmd: SelectDungeonTypeCommand) {
        if (!cmd.aspects.contains(AspectItems.ASPECT_OF_THE_GROVE)) return
        cmd.dungeonType = DungeonType.BEASTWEAVER_GROVE
    }

    @CommandHandler
    fun onGenerateLayout(cmd: DungeonGenerateCommand) {
        if (!cmd.aspects.contains(AspectItems.ASPECT_OF_THE_GROVE)) return
        cmd.dungeonLevel = max(cmd.dungeonLevel, AspectOfTheGrove.MIN_DUNGEON_LEVEL)
    }

    @EventSubscriber(sync = true)
    fun onDungeonBossDeath(event: DungeonFinalBossDeathEvent) {
        val serverWorld = event.world as? ServerLevel ?: return
        if (!serverWorld.getDungeonAspects().contains(AspectItems.ASPECT_OF_THE_GROVE)) return

        val stack = CrystalItems.IMITATION_CRYSTAL.defaultInstance
        event.bossEntity.spawnAtLocation(serverWorld, stack)

        // TODO drop more for higher dungeon level
    }
}