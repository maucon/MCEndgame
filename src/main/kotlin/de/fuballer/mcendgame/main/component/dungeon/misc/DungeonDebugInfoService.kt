package de.fuballer.mcendgame.main.component.dungeon.misc

import de.fuballer.mcendgame.main.messaging.misc.PlayerAfterDimensionChangeEvent
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.gamerules.GameRules

/**
 * Keeps the client's `reducedDebugInfo` flag in sync with the level the player is actually in.
 */
@Injectable
class DungeonDebugInfoService {
    @EventSubscriber(sync = true)
    fun on(event: PlayerAfterDimensionChangeEvent) {
        val player = event.player as? ServerPlayer ?: return

        val reduced = event.newWorld.gameRules.get(GameRules.REDUCED_DEBUG_INFO)
        player.connection.send(ClientboundEntityEventPacket(player, if (reduced) ENTITY_EVENT_ENABLE else ENTITY_EVENT_DISABLE))
    }

    private companion object {
        const val ENTITY_EVENT_ENABLE: Byte = 22
        const val ENTITY_EVENT_DISABLE: Byte = 23
    }
}