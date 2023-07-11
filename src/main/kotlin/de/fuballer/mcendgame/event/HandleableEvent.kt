package de.fuballer.mcendgame.event

import org.bukkit.event.Event
import org.bukkit.event.HandlerList

@Suppress("unused") // Bukkit needs events to have these properties
open class HandleableEvent : Event() {
    override fun getHandlers() = handlerList

    companion object {
        private val handlerList = HandlerList()

        @JvmStatic
        fun getHandlerList() = handlerList
    }
}
