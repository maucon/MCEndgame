package de.fuballer.mcendgame.configuration

import com.comphenix.protocol.ProtocolManager
import de.fuballer.mcendgame.framework.annotation.Bean
import de.fuballer.mcendgame.framework.annotation.Configuration
import org.bukkit.Server
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitScheduler
import org.bukkit.scoreboard.ScoreboardManager
import java.io.File
import java.util.logging.Logger

@Configuration
class PluginConfiguration {
    @Bean("javaPlugin")
    fun plugin(): JavaPlugin = PluginConfiguration.plugin()

    @Bean
    fun logger(): Logger = plugin().logger

    @Bean
    fun worldContainer(): File = plugin().server.worldContainer

    @Bean
    fun server(): Server = PluginConfiguration.server()

    @Bean
    fun protocolManager(): ProtocolManager = PluginConfiguration.protocolManager()

    companion object {
        lateinit var INSTANCE: JavaPlugin
        lateinit var PROTOCOL_MANAGER: ProtocolManager

        fun plugin(): JavaPlugin = INSTANCE
        fun scheduler(): BukkitScheduler = INSTANCE.server.scheduler
        fun pluginManager(): PluginManager = INSTANCE.server.pluginManager
        fun server(): Server = INSTANCE.server
        fun protocolManager(): ProtocolManager = PROTOCOL_MANAGER
        fun scoreboardManager(): ScoreboardManager = plugin().server.scoreboardManager!!
    }
}
