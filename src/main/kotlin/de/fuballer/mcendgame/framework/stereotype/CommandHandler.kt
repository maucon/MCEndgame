package de.fuballer.mcendgame.framework.stereotype

import org.bukkit.command.CommandExecutor
import org.bukkit.plugin.java.JavaPlugin

abstract class CommandHandler(
    private val commandName: String
) : CommandExecutor, LifeCycleListener {
    override fun initialize(plugin: JavaPlugin) = plugin.getCommand(commandName)!!.setExecutor(this)
}
