package de.fuballer.mcendgame.event

import org.bukkit.event.Event
import org.bukkit.event.HandlerList

open class HandleableEvent : Event() {
    override fun getHandlers() = handlerList

    companion object {
        private val handlerList = HandlerList()

        @Suppress("unused") // Bukkit needs events to have this property
        @JvmStatic
        fun getHandlerList() = handlerList
    }
}
