package de.fuballer.mcendgame.client.component.boss_event

import de.fuballer.mcendgame.main.component.boss_event.BossEventType
import java.util.*

object ClientBossEventTypes {
    private val TYPES: MutableMap<UUID, BossEventType> = mutableMapOf()

    fun get(id: UUID) = TYPES[id]

    fun set(id: UUID, type: BossEventType) {
        TYPES[id] = type
    }

    fun remove(id: UUID) {
        TYPES.remove(id)
    }
}