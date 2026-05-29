package de.fuballer.mcendgame.main.component.dungeon.misc

import de.fuballer.mcendgame.main.messaging.misc.PlayerAfterRespawnEvent
import de.fuballer.mcendgame.main.mixin.server_player_entity.ServerPlayerEntityAccessor
import de.fuballer.mcendgame.main.util.extension.WorldExtension.isDungeonWorld
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber

@Injectable
class DungeonKeepInventoryService {
    @EventSubscriber(sync = true)
    fun on(event: PlayerAfterRespawnEvent) {
        val oldPlayer = event.oldPlayer
        if (!oldPlayer.entityWorld.isDungeonWorld()) return

        val accessor = event.newPlayer as ServerPlayerEntityAccessor
        accessor.`mcendgame$invokeCopyInventoryAndExperienceFrom`(oldPlayer)
    }
}