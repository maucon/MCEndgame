package de.fuballer.mcendgame.event

import de.fuballer.mcendgame.helper.PluginUtil
import org.bukkit.event.Event

object EventGateway {
    fun apply(event: Event) = PluginUtil.getPluginManager().callEvent(event)
}
