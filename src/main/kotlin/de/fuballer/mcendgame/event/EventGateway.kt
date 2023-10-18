package de.fuballer.mcendgame.event

import de.fuballer.mcendgame.configuration.PluginConfiguration
import org.bukkit.event.Event

object EventGateway {
    fun apply(event: Event) = PluginConfiguration.pluginManager().callEvent(event)
}
