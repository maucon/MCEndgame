package de.fuballer.mcendgame.main.component.entity.custom.networking

import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.initializer.Initializer
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry

@Injectable
class EntityHookEntityPayloadRegisterer {
    @Initializer(priority = 0)
    fun register() {
        PayloadTypeRegistry.clientboundPlay().register(EntityHookEntityPayload.ID, EntityHookEntityPayload.CODEC)
    }
}