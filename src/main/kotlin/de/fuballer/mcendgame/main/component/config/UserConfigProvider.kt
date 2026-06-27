package de.fuballer.mcendgame.main.component.config

import com.google.gson.GsonBuilder
import de.maucon.mauconframework.di.annotation.Configuration
import de.maucon.mauconframework.di.annotation.Injectable
import net.fabricmc.loader.api.FabricLoader
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.io.path.writeText

@Configuration
object ConfigProvider {
    @Injectable
    fun analyticsConfig(fabricLoader: FabricLoader): UserConfig {
        val path = fabricLoader.configDir.resolve(UserConfig.FILE)
        val gson = GsonBuilder().setPrettyPrinting().create()

        if (!path.exists()) {
            val default = UserConfig()
            path.writeText(gson.toJson(default))
            return default
        }

        return runCatching { gson.fromJson(path.readText(), UserConfig::class.java) }
            .getOrDefault(UserConfig())
    }
}