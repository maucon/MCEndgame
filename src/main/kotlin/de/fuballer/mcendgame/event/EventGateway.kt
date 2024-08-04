package de.fuballer.mcendgame.event

import de.fuballer.mcendgame.configuration.PluginConfiguration
import de.fuballer.mcendgame.util.ThreadUtil.bukkitSync
import org.bukkit.event.Event

object EventGateway {
    fun apply(event: Event) = bukkitSync {
        PluginConfiguration.pluginManager().callEvent(event)
    }
}
