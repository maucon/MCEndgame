package de.fuballer.mcendgame.configuration

import de.fuballer.mcendgame.MCEndgame
import de.fuballer.mcendgame.framework.annotation.Bean
import de.fuballer.mcendgame.framework.annotation.Configuration
import org.bukkit.Server
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitScheduler
import java.io.File
import java.util.logging.Logger

@Configuration
class PluginConfiguration {
    @Bean("javaPlugin")
    fun plugin(): JavaPlugin = PluginConfiguration.plugin()

    @Bean
    fun logger(): Logger = MCEndgame.INSTANCE.logger

    @Bean
    fun worldContainer(): File = MCEndgame.INSTANCE.server.worldContainer

    @Bean
    fun server(): Server = PluginConfiguration.server()

    companion object {
        fun plugin(): JavaPlugin = MCEndgame.INSTANCE
        fun scheduler(): BukkitScheduler = MCEndgame.INSTANCE.server.scheduler
        fun pluginManager(): PluginManager = MCEndgame.INSTANCE.server.pluginManager
        fun server(): Server = MCEndgame.INSTANCE.server
    }
}
