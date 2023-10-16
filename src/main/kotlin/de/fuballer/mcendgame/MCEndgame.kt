package de.fuballer.mcendgame

import de.fuballer.mcendgame.framework.DependencyInjector
import de.fuballer.mcendgame.framework.stereotype.*
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

class MCEndgame : JavaPlugin() {
    private lateinit var repositories: List<Repository<*, *>>
    private lateinit var services: List<Service>
    private lateinit var listener: List<EventListener>
    private lateinit var commandHandler: List<CommandHandler>
    private lateinit var tabCompleter: List<CommandTabCompleter>

    companion object {
        lateinit var INSTANCE: Plugin
    }

    override fun onEnable() {
        INSTANCE = this

        val injectedObjects = DependencyInjector.instantiateClasses(this::class.java)

        repositories = injectedObjects.filterIsInstance<Repository<*, *>>()
        services = injectedObjects.filterIsInstance<Service>()
        listener = injectedObjects.filterIsInstance<EventListener>()
        commandHandler = injectedObjects.filterIsInstance<CommandHandler>()
        tabCompleter = injectedObjects.filterIsInstance<CommandTabCompleter>()

        listener.forEach { PluginUtil.registerEvents(it) }

        repositories.forEach { it.load() }
        services.forEach { it.initialize(this) }

        commandHandler.forEach { getCommand(it.getCommand())!!.setExecutor(it) }
        tabCompleter.forEach { getCommand(it.getCommand())!!.tabCompleter = it }

        logger.info("Enabled MC-Endgame")
    }

    override fun onDisable() {
        services.forEach { it.terminate() }
        repositories.forEach { it.flush() }

        logger.info("Disabled MC-Endgame")
    }
}
