package de.fuballer.mcendgame.main.component.dungeon.completion

import de.fuballer.mcendgame.main.messaging.dungeon.DungeonBossDeathEvent
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.isDungeonCompleted
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.setDungeonCompleted
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventGateway
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.server.level.ServerLevel

@Injectable
class DungeonCompletionService {
    @EventSubscriber(sync = true)
    fun on(event: DungeonBossDeathEvent) {
        val dungeonWorld = event.world
        if (dungeonWorld !is ServerLevel) return

        val players = dungeonWorld.players().toList()

        if (dungeonWorld.isDungeonCompleted()) return
        dungeonWorld.setDungeonCompleted()

        val dungeonCompletedEvent = DungeonCompletedEvent(event.isClient, dungeonWorld, players)
        EventGateway.publish(dungeonCompletedEvent)
    }
}