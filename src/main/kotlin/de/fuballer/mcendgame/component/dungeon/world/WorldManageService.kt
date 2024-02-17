package de.fuballer.mcendgame.component.dungeon.world

import de.fuballer.mcendgame.component.dungeon.seed.DungeonSeedService
import de.fuballer.mcendgame.component.dungeon.world.db.ManagedWorldEntity
import de.fuballer.mcendgame.component.dungeon.world.db.WorldManageRepository
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
class WorldManageService(
    private val worldManageRepo: WorldManageRepository,
    private val dungeonSeedService: DungeonSeedService,
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
        worldManageRepo.findAll().forEach {
            PluginUtil.unloadWorld(it.world)

            val toDelete = File("$worldContainer/${it.world.name}")
            fileHelper.deleteFile(toDelete)
        }
    }

    fun createWorld(
        player: Player,
        mapTier: Int
    ): World {
        val seed = dungeonSeedService.getSeed(player)

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

        val entity = ManagedWorldEntity(name, player, world, mapTier, 0)
        worldManageRepo.save(entity)

        return world
    }

    private fun checkWorldTimers() {
        worldManageRepo.findAll()
            .map {
                if (it.world.players.isEmpty()) {
                    it.deleteTimer++
                } else {
                    it.deleteTimer = 0
                }

                worldManageRepo.save(it)
            }
            .filter { it.deleteTimer > WorldSettings.MAX_WORLD_EMPTY_TIME }
            .forEach { deleteWorld(it.world) }
    }

    private fun deleteWorld(world: World) {
        val name = world.name

        SchedulingUtil.runTask {
            val dungeonWorldDeleteEvent = DungeonWorldDeleteEvent(world)
            EventGateway.apply(dungeonWorldDeleteEvent)

            PluginUtil.unloadWorld(world)
            worldManageRepo.delete(name)

            val toDelete = File("$worldContainer/$name")
            fileHelper.deleteFile(toDelete)
        }
    }
}