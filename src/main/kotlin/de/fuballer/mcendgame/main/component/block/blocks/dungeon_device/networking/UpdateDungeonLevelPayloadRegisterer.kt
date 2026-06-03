package de.fuballer.mcendgame.main.component.block.blocks.dungeon_device.networking

import de.fuballer.mcendgame.main.messaging.dungeon.ClientDungeonLevelUpdateEvent
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventGateway
import de.maucon.mauconframework.initializer.Initializer
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking

@Injectable
class UpdateDungeonLevelPayloadRegisterer {
    @Initializer
    fun register() {
        PayloadTypeRegistry.playC2S().register(UpdateDungeonLevelPayload.ID, UpdateDungeonLevelPayload.CODEC)

        ServerPlayNetworking.registerGlobalReceiver(UpdateDungeonLevelPayload.ID) { payload, context ->
            val playerEntity = context.player()
            val dungeonLevel = payload.dungeonLevel
            EventGateway.publish(ClientDungeonLevelUpdateEvent(playerEntity, dungeonLevel))
        }
    }
}