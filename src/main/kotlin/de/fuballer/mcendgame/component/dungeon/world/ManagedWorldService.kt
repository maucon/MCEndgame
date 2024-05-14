package de.fuballer.mcendgame.component.dungeon.world

import de.fuballer.mcendgame.component.dungeon.world.db.ManagedWorldEntity
import de.fuballer.mcendgame.component.dungeon.world.db.ManagedWorldRepository
import de.fuballer.mcendgame.event.DungeonWorldDeleteEvent
import de.fuballer.mcendgame.event.EventGateway
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.framework.annotation.Qualifier
import de.fuballer.mcendgame.framework.stereotype.LifeCycleListener
import de.fuballer.mcendgame.helper.FileHelper
import de.fuballer.mcendgame.util.PluginUtil
import de.fuballer.mcendgame.util.SchedulingUtil
import org.bukkit.World
import org.bukkit.WorldCreator
import org.bukkit.WorldType
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.*

@Component
class ManagedWorldService(
    private val worldManageRepo: ManagedWorldRepository,
    private val fileHelper: FileHelper,
    @Qualifier("worldContainer")
    private val worldContainer: File
) : LifeCycleListener {

    override fun initialize(plugin: JavaPlugin) {
        SchedulingUtil.runTaskTimer(WorldSettings.WORLD_EMPTY_TEST_PERIOD) {
            checkWorldTimers()
        }
    }

    override fun terminate() {
        worldManageRepo.findAll()
            .forEach { deleteWorld(it) }
    }

    fun createWorld(
        player: Player,
        seed: Long
    ): World {
        val name = "${WorldSettings.WORLD_PREFIX}${UUID.randomUUID()}"
        val worldCreator = WorldCreator(name)
            .seed(seed)
            .type(WorldType.FLAT)
            .generateStructures(false)
            .generatorSettings(WorldSettings.GENERATOR_SETTINGS)

        val world = PluginUtil.createWorld(worldCreator).apply {
            WorldSettings.updateGameRules(this)

            difficulty = WorldSettings.DIFFICULTY
            time = WorldSettings.WORLD_TIME
        }

        val entity = ManagedWorldEntity(name, player, world)
        worldManageRepo.save(entity)

        return world
    }

    private fun checkWorldTimers() {
        worldManageRepo.findAll()
            .map {
                updateDeleteTimer(it)
                worldManageRepo.save(it)
            }
            .filter { it.deleteTimer > WorldSettings.MAX_WORLD_EMPTY_TIME }
            .forEach { deleteWorld(it) }
    }

    private fun updateDeleteTimer(it: ManagedWorldEntity) {
        if (it.world.players.isEmpty()) {
            it.deleteTimer++
        } else {
            it.deleteTimer = 0
        }
    }

    private fun deleteWorld(entity: ManagedWorldEntity) {
        val world = entity.world

        SchedulingUtil.runTask {
            val dungeonWorldDeleteEvent = DungeonWorldDeleteEvent(world)
            EventGateway.apply(dungeonWorldDeleteEvent)

            PluginUtil.unloadWorld(world)
            worldManageRepo.deleteById(world.name)

            val toDelete = File("$worldContainer/${world.name}")
            fileHelper.deleteFile(toDelete)
        }

        worldManageRepo.delete(entity)
    }
}