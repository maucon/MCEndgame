package de.fuballer.mcendgame.framework.stereotype

import org.bukkit.plugin.Plugin

interface Service : Injectable {
    fun initialize(plugin: Plugin) {}
    fun terminate() {}
}
