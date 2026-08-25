package de.fuballer.mcendgame.main.functional.persistent_repository

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import com.mojang.serialization.Codec
import de.fuballer.mcendgame.main.MCEndgame
import de.fuballer.mcendgame.main.configuration.RuntimeConfig
import de.fuballer.mcendgame.main.messaging.misc.PlayerDisconnectEvent
import de.fuballer.mcendgame.main.messaging.server.ServerStartedEvent
import de.fuballer.mcendgame.main.messaging.server.ServerStoppingEvent
import de.maucon.mauconframework.event.EventSubscriber
import de.maucon.mauconframework.stereotype.Entity
import de.maucon.mauconframework.stereotype.extension.InMemoryMapRepository
import org.slf4j.Logger
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import kotlin.jvm.optionals.getOrNull

open class PersistentMapRepository<ID, ENTITY : Entity<ID>>(
    private val log: Logger,
    private val name: String,
    private val codec: Codec<ENTITY>
) : InMemoryMapRepository<ID, ENTITY>() {
    private val gson = GsonBuilder().setPrettyPrinting().create()

    @EventSubscriber(sync = true)
    fun on(event: ServerStartedEvent) {
        loadFromFile()
        Files.createDirectories(getFolderPath())
    }

    @EventSubscriber(sync = true)
    fun on(event: ServerStoppingEvent) {
        writeToFile()
    }

    @EventSubscriber(sync = true)
    fun on(event: PlayerDisconnectEvent) {
        writeToFile()
    }

    private fun loadFromFile() {
        try {
            if (!Files.exists(getFilePath())) {
                log.warn("No file for persistent repository '$name' found")
                findAll().forEach { delete(it) }
                return
            }

            val content = Files.readString(getFilePath(), StandardCharsets.UTF_8)
            val jsonArray = JsonParser.parseString(content).asJsonArray

            val entities = jsonArray
                .map { codec.parse(RuntimeConfig.REGISTRY_OPS, it) }
                .map { it.result() }
                .mapNotNull { it.getOrNull() }

            saveAll(entities)
            log.info("Loaded ${entities.size} entities for persistent repository '$name'")

        } catch (e: Exception) {
            log.error("Failed to read file for persistent repository '$name': ${e.message}", e)
        }
    }

    fun writeToFile() {
        try {
            val jsonArray = JsonArray()

            findAll()
                .map { codec.encodeStart(RuntimeConfig.REGISTRY_OPS, it) }
                .map { it.result() }
                .mapNotNull { it.getOrNull() }
                .forEach { jsonArray.add(it) }

            val prettyJson = gson.toJson(jsonArray)

            Files.writeString(getFilePath(), prettyJson, StandardCharsets.UTF_8)
            log.info("Saved ${jsonArray.size()} entities for persistent repository '$name'")

        } catch (e: Exception) {
            log.error("Failed to write file for persistent repository '$name': ${e.message}", e)
        }
    }

    private fun getFolderPath() = RuntimeConfig.WORLD_SAVE_PATH
        .resolve(MCEndgame.MOD_ID)
        .resolve("repository")

    private fun getFilePath() = getFolderPath().resolve("$name.json")
}