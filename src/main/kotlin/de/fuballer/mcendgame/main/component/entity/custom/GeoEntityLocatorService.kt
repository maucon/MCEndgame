package de.fuballer.mcendgame.main.component.entity.custom

import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.world.phys.Vec3
import java.util.*

@Injectable
object GeoEntityLocatorService {
    private val GEO_ENTITY_LOCATORS = mutableMapOf<UUID, EntityLocators>()

    fun keepOnlyExisting(existing: Set<UUID>) {
        if (existing.isEmpty()) {
            GEO_ENTITY_LOCATORS.clear()
            return
        }

        GEO_ENTITY_LOCATORS.keys.removeIf { !existing.contains(it) }
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