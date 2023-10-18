package de.fuballer.mcendgame.framework.stereotype

import org.bukkit.plugin.java.JavaPlugin

interface LifeCycleListener {
    fun initialize(plugin: JavaPlugin) {}
    fun terminate() {}
}
