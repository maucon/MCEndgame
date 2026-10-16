package de.fuballer.mcendgame.main.component.entity.custom

import de.fuballer.mcendgame.main.messaging.server.ServerEndTickEvent
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.world.phys.Vec3
import java.util.*

@Injectable
object GeoEntityLocatorService {
    private val GEO_ENTITY_LOCATORS = mutableMapOf<UUID, EntityLocators>()

    @EventSubscriber
    fun on(event: ServerEndTickEvent) {
        val server = event.server
        if (server.tickCount % 20 != 9) return // % 20 = 9 so multiple things like this are spread across different ticks

        val existingEntityUuids = server.allLevels
            .flatMap { world -> world.allEntities }
            .mapTo(mutableSetOf()) { entity -> entity.uuid }

        GEO_ENTITY_LOCATORS.keys.removeIf { it !in existingEntityUuids }
    }

    fun addLocatorPos(uuid: UUID, name: String, pos: Vec3?) {
        if (pos == null) return
        val entityLocators = GEO_ENTITY_LOCATORS[uuid] ?: EntityLocators()
        entityLocators.set(name, pos)
        GEO_ENTITY_LOCATORS[uuid] = entityLocators
    }

    fun getLocatorPos(uuid: UUID, name: String?) = name?.let { GEO_ENTITY_LOCATORS[uuid]?.get(name) }

    private data class EntityLocators(
        val locators: MutableMap<String, Vec3> = mutableMapOf<String, Vec3>()
    ) {
        fun set(name: String, pos: Vec3) {
            locators[name] = pos
        }

        fun get(name: String) = locators[name]
    }
}