package de.fuballer.mcendgame.configuration

import de.fuballer.mcendgame.MCEndgame
import de.fuballer.mcendgame.framework.stereotype.Bean
import de.fuballer.mcendgame.framework.stereotype.Configuration
import org.bukkit.plugin.Plugin
import java.util.logging.Logger

@Suppress("unused")
class PluginConfiguration : Configuration {
    @Bean
    fun plugin(): Plugin = MCEndgame.INSTANCE

    @Bean
    fun logger(): Logger = MCEndgame.INSTANCE.logger
}
