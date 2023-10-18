package de.fuballer.mcendgame

import de.fuballer.mcendgame.framework.DependencyInjector
import de.fuballer.mcendgame.framework.stereotype.EventListener
import de.fuballer.mcendgame.framework.stereotype.LifeCycleListener
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.plugin.java.JavaPlugin

class MCEndgame : JavaPlugin() {
    companion object {
        lateinit var INSTANCE: JavaPlugin
    }

    private var lifeCycleListener: List<LifeCycleListener>? = null
    private var listener: List<EventListener>? = null

    override fun onEnable() {
        INSTANCE = this

        val injectedObjects = DependencyInjector.instantiateClasses(this::class.java)

        listener = injectedObjects.filterIsInstance<EventListener>()
            .onEach { PluginUtil.registerEvents(it) }

        lifeCycleListener = injectedObjects.filterIsInstance<LifeCycleListener>()
            .onEach { it.initialize(this) }

        logger.info("Enabled MC-Endgame")
    }

    override fun onDisable() {
        lifeCycleListener?.forEach { it.terminate() }

        logger.info("Disabled MC-Endgame")
    }
}
