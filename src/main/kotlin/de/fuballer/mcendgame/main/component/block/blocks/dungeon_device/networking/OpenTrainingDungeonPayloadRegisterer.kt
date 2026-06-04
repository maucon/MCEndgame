package de.fuballer.mcendgame.main.component.block.blocks.dungeon_device.networking

import de.fuballer.mcendgame.main.component.block.blocks.dungeon_device.DungeonDeviceBlockEntity
import de.fuballer.mcendgame.main.configuration.RuntimeConfig
import de.fuballer.mcendgame.main.messaging.dungeon.OpenTrainingDungeonButtonPressedEvent
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventGateway
import de.maucon.mauconframework.initializer.Initializer
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking

@Injectable
class OpenTrainingDungeonPayloadRegisterer {
    @Initializer
    fun register() {
        PayloadTypeRegistry.playC2S().register(DungeonDeviceTrainingPayload.ID, DungeonDeviceTrainingPayload.CODEC)

        ServerPlayNetworking.registerGlobalReceiver(DungeonDeviceTrainingPayload.ID) { payload, context ->
            val blockEntity = RuntimeConfig.SERVER.getLevel(payload.worldKey)?.getBlockEntity(payload.pos) ?: return@registerGlobalReceiver
            val playerEntity = context.player()

            val dungeonDeviceEntity = blockEntity as? DungeonDeviceBlockEntity ?: return@registerGlobalReceiver

            EventGateway.publish(OpenTrainingDungeonButtonPressedEvent(dungeonDeviceEntity, playerEntity))
        }
    }
}