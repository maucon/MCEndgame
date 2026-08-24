package de.fuballer.mcendgame.main.component.sound

import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.initializer.Initializer
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry

@Injectable
class RangeDefinedSoundPayloadRegisterer {
    @Initializer(priority = 0)
    fun register() {
        PayloadTypeRegistry.clientboundPlay().register(RangeDefinedSoundPayload.ID, RangeDefinedSoundPayload.CODEC)
    }
}