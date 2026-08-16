package de.fuballer.mcendgame.client.component.boss_event

import de.fuballer.mcendgame.main.component.boss_event.BossEventTypePayload
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.initializer.Initializer
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking

@Injectable
object RangeDefinedSoundPayloadReceiverRegisterer {
    @Initializer
    fun register() {
        ClientPlayNetworking.registerGlobalReceiver(BossEventTypePayload.ID) { payload, _ ->
            ClientBossEventTypes.set(payload.id, payload.type)
        }
    }
}