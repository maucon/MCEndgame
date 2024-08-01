package de.fuballer.mcendgame.util.command

import de.fuballer.mcendgame.framework.stereotype.CommandHandler
import org.bukkit.plugin.java.JavaPlugin

abstract class CommandHandler(
    private val commandName: String
) : CommandHandler {
    override fun initialize(plugin: JavaPlugin) = plugin.getCommand(commandName)!!.setExecutor(this)
}