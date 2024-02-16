package de.fuballer.mcendgame.component.portal.skins

import org.bukkit.Location

interface PortalSkin {
    fun prepare(location: Location)
    fun play()
    fun cancel()
}