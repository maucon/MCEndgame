package de.fuballer.mcendgame.client.component.screen.scarred_one

import de.fuballer.mcendgame.main.component.dungeon.generation.encounter.encounters.scarred_one.networking.ScarredOneEffectsPayload
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.initializer.Initializer
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking

@Injectable
class ScarredOneEffectsPayloadHandler {
    @Initializer
    fun register() {
        ClientPlayNetworking.registerGlobalReceiver(ScarredOneEffectsPayload.ID) { payload, context ->
            val client = context.client()

            client.execute {
                client.gui.setScreen(
                    ScarredOneScreen(
                        payload.positiveEffects,
                        payload.negativeEffects,
                        payload.uuid,
                    )
                )
            }
        }
    }
}