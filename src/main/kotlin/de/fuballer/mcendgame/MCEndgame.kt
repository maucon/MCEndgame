package de.fuballer.mcendgame

import com.comphenix.protocol.ProtocolLibrary
import de.fuballer.mcendgame.component.dungeon.generation.layout_generator.linear.RoomTypes
import de.fuballer.mcendgame.configuration.PluginConfiguration
import de.fuballer.mcendgame.framework.DependencyInjector
import de.fuballer.mcendgame.framework.stereotype.LifeCycleListener
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import kotlin.time.measureTime

@Suppress("unused")
class MCEndgame : JavaPlugin() {
    private var lifeCycleListener: List<LifeCycleListener>? = null

    override fun onEnable() {
        val time = measureTime {
            Locale.setDefault(Locale.ENGLISH)

            PluginConfiguration.INSTANCE = this
            PluginConfiguration.PROTOCOL_MANAGER = ProtocolLibrary.getProtocolManager()
            dataFolder.mkdir()

            val injectedObjects = DependencyInjector.instantiateClasses(this::class.java)

            injectedObjects.filterIsInstance<Listener>()
                .onEach { PluginUtil.registerEvents(it) }

            lifeCycleListener = injectedObjects.filterIsInstance<LifeCycleListener>()
                .onEach { it.initialize(this) }

            RoomTypes.init() // load schematics of the dungeon maps
        }

        logger.info("Enabled $name in ${time.inWholeMilliseconds} ms")
    }

    override fun onDisable() {
        val time = measureTime {
            lifeCycleListener?.forEach { it.terminate() }
            ProtocolLibrary.getProtocolManager().removePacketListeners(this)
        }
        logger.info("Disabled $name in ${time.inWholeMilliseconds} ms")
    }
}