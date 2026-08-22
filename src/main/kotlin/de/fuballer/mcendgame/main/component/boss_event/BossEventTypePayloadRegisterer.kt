package de.fuballer.mcendgame.main.component.boss_event

import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.initializer.Initializer
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry

@Injectable
class BossEventTypePayloadRegisterer {
    @Initializer(priority = 0)
    fun register() {
        PayloadTypeRegistry.clientboundPlay().register(BossEventTypePayload.ID, BossEventTypePayload.CODEC)
    }
}