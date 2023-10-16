package de.fuballer.mcendgame.configuration

import de.fuballer.mcendgame.MCEndgame
import de.fuballer.mcendgame.framework.annotation.Bean
import de.fuballer.mcendgame.framework.stereotype.Configuration
import org.bukkit.Server
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.PluginManager
import org.bukkit.scheduler.BukkitScheduler
import java.io.File
import java.util.logging.Logger

@Suppress("unused")
class PluginConfiguration : Configuration {
    @Bean
    fun plugin(): Plugin = PluginConfiguration.plugin()

    @Bean
    fun logger(): Logger = MCEndgame.INSTANCE.logger

    @Bean
    fun worldContainer(): File = MCEndgame.INSTANCE.server.worldContainer

    @Bean
    fun server(): Server = PluginConfiguration.server()

    companion object {
        fun plugin(): Plugin = MCEndgame.INSTANCE
        fun dataFolder(): File = MCEndgame.INSTANCE.dataFolder
        fun scheduler(): BukkitScheduler = MCEndgame.INSTANCE.server.scheduler
        fun pluginManager(): PluginManager = MCEndgame.INSTANCE.server.pluginManager
        fun server(): Server = MCEndgame.INSTANCE.server
    }
}
