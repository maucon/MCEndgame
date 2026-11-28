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
import java.nio.file.StandardCopyOption

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

            val entities = jsonArray.mapIndexedNotNull { index, json ->
                codec.parse(RuntimeConfig.REGISTRY_OPS, json)
                    .resultOrPartial { error -> log.error("Failed to read entry #$index of persistent repository '$name': $error") }
                    .orElse(null)
            }

            if (entities.size != jsonArray.size()) backupUnreadableFile()

            saveAll(entities)
            log.info("Loaded ${entities.size} entities for persistent repository '$name'")

        } catch (e: Exception) {
            log.error("Failed to read file for persistent repository '$name': ${e.message}", e)
        }
    }

    private fun backupUnreadableFile() {
        val backupPath = getFolderPath().resolve("$name.json.bak")
        Files.copy(getFilePath(), backupPath, StandardCopyOption.REPLACE_EXISTING)
        log.warn("Some entries of persistent repository '$name' could not be read, backed the file up to ${backupPath.fileName}")
    }

    fun writeToFile() {
        try {
            val jsonArray = JsonArray()

            findAll()
                .map { entity ->
                    codec.encodeStart(RuntimeConfig.REGISTRY_OPS, entity)
                        .resultOrPartial { error -> log.error("Failed to write entity ${entity.id} of persistent repository '$name': $error") }
                }
                .mapNotNull { it.orElse(null) }
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