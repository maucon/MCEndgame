package de.fuballer.mcendgame.event

import org.bukkit.Bukkit
import org.bukkit.event.Event

object EventGateway {
    fun apply(event: Event) {
        Bukkit.getServer().pluginManager.callEvent(event)
    }
}
