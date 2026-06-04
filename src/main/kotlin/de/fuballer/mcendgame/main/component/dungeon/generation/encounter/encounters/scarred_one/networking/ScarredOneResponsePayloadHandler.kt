package de.fuballer.mcendgame.main.component.dungeon.generation.encounter.encounters.scarred_one.networking

import de.fuballer.mcendgame.main.component.dungeon.generation.encounter.encounters.scarred_one.ScarredOneEncounterService
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.initializer.Initializer
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking

@Injectable
class ScarredOneResponsePayloadHandler(
    val scarredOneEncounterService: ScarredOneEncounterService,
) {
    @Initializer
    fun register() {
        PayloadTypeRegistry.serverboundPlay().register(ScarredOneResponsePayload.ID, ScarredOneResponsePayload.CODEC)

        ServerPlayNetworking.registerGlobalReceiver(ScarredOneResponsePayload.ID) { payload, context ->
            scarredOneEncounterService.response(context.player(), payload.accept, payload.uuid)
        }
    }
}