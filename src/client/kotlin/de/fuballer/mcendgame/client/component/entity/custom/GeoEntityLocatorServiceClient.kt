package de.fuballer.mcendgame.client.component.entity.custom

import de.fuballer.mcendgame.client.messaging.events.ClientEndTickEvent
import de.fuballer.mcendgame.main.component.entity.custom.GeoEntityLocatorService
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber

@Injectable
object GeoEntityLocatorServiceClient {
    @EventSubscriber(sync = true)
    fun on(event: ClientEndTickEvent) {
        val level = event.minecraft.level
        if (level == null) {
            GeoEntityLocatorService.keepOnlyExisting(setOf())
            return
        }

        if (level.gameTime % 20 != 9L) return // % 20 = 9 so multiple things like this are spread across different ticks

        val existing = level.entitiesForRendering().mapTo(mutableSetOf()) { entity -> entity.uuid }
        GeoEntityLocatorService.keepOnlyExisting(existing)
    }
}