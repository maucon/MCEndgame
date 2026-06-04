package de.fuballer.mcendgame.main.component.block.blocks.dungeon_device.networking

import de.fuballer.mcendgame.main.component.block.blocks.dungeon_device.DungeonDeviceBlockEntity
import de.fuballer.mcendgame.main.configuration.RuntimeConfig
import de.fuballer.mcendgame.main.messaging.dungeon.OpenDungeonButtonPressedEvent
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventGateway
import de.maucon.mauconframework.initializer.Initializer
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking

@Injectable
class OpenDungeonPayloadRegisterer {
    @Initializer
    fun register() {
        PayloadTypeRegistry.playC2S().register(DungeonDevicePayload.ID, DungeonDevicePayload.CODEC)

        ServerPlayNetworking.registerGlobalReceiver(DungeonDevicePayload.ID) { openDungeonPayload, context ->
            val blockEntity = RuntimeConfig.SERVER.getLevel(openDungeonPayload.worldKey)?.getBlockEntity(openDungeonPayload.pos) ?: return@registerGlobalReceiver
            val playerEntity = context.player()

            val dungeonDeviceEntity = blockEntity as? DungeonDeviceBlockEntity ?: return@registerGlobalReceiver

            EventGateway.publish(OpenDungeonButtonPressedEvent(dungeonDeviceEntity, playerEntity))
        }
    }
}