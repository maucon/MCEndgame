package de.fuballer.mcendgame

import com.comphenix.protocol.ProtocolLibrary
import de.fuballer.mcendgame.configuration.PluginConfiguration
import de.fuballer.mcendgame.framework.DependencyInjector
import de.fuballer.mcendgame.framework.stereotype.LifeCycleListener
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class MCEndgame : JavaPlugin() {
    private var lifeCycleListener: List<LifeCycleListener>? = null

    override fun onEnable() {
        Locale.setDefault(Locale.ENGLISH)

        PluginConfiguration.INSTANCE = this
        PluginConfiguration.PROTOCOL_MANAGER = ProtocolLibrary.getProtocolManager()
        dataFolder.mkdir()

        val injectedObjects = DependencyInjector.instantiateClasses(this::class.java)

        injectedObjects.filterIsInstance<Listener>()
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