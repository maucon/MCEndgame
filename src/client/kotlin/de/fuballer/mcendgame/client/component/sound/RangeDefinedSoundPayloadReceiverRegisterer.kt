package de.fuballer.mcendgame.client.component.sound

import de.fuballer.mcendgame.main.component.sound.RangeDefinedSoundPayload
import de.fuballer.mcendgame.main.component.sound.Sounds
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.initializer.Initializer
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking

@Injectable
class RangeDefinedSoundPayloadReceiverRegisterer {
    @Initializer
    fun register() {
        ClientPlayNetworking.registerGlobalReceiver(RangeDefinedSoundPayload.ID) { payload, _ ->
            Sounds.playRangeDefinedSound(
                payload.sound.value(),
                payload.category,
                payload.volume,
                payload.pitch,
                payload.pos,
                payload.range,
            )
        }
    }
}