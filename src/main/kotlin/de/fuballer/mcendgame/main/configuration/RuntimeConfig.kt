package de.fuballer.mcendgame.main.configuration

import com.google.gson.JsonElement
import com.mojang.serialization.JsonOps
import de.fuballer.mcendgame.main.runtime_worlds.RuntimeWorlds
import de.fuballer.mcendgame.main.messaging.server.ServerStartingEvent
import de.maucon.mauconframework.di.annotation.Configuration
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.resources.RegistryOps
import net.minecraft.server.MinecraftServer
import net.minecraft.world.level.storage.LevelResource
import java.nio.file.Path

@Configuration
object RuntimeConfig {
    lateinit var RUNTIME_WORLDS: RuntimeWorlds
    lateinit var SERVER: MinecraftServer
    lateinit var WORLD_SAVE_PATH: Path
    lateinit var REGISTRY_OPS: RegistryOps<JsonElement>

    @EventSubscriber(sync = true)
    fun on(event: ServerStartingEvent) {
        val server = event.server

        this.SERVER = server
        this.RUNTIME_WORLDS = RuntimeWorlds.create(server)
        this.WORLD_SAVE_PATH = server.getWorldPath(LevelResource.ROOT)
        this.REGISTRY_OPS = RegistryOps.create(JsonOps.INSTANCE, server.registryAccess())
    }
}