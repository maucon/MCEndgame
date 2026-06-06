package de.fuballer.mcendgame.main.component.dungeon.generation.encounter.encounters.scarred_one.networking

import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.initializer.Initializer
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry

@Injectable
object ScarredOneEffectsPayloadRegisterer {
    @Initializer(priority = 0) // before ScarredOneEffectsPayloadHandler.register
    fun register() {
        PayloadTypeRegistry.clientboundPlay().register(ScarredOneEffectsPayload.ID, ScarredOneEffectsPayload.CODEC)
    }
}