package de.fuballer.mcendgame.component.dungeon.world

import de.fuballer.mcendgame.MCEndgame
import de.fuballer.mcendgame.component.dungeon.world.db.ManagedWorldEntity
import de.fuballer.mcendgame.component.dungeon.world.db.WorldManageRepository
import de.fuballer.mcendgame.event.DungeonWorldDeleteEvent
import de.fuballer.mcendgame.event.EventGateway
import de.fuballer.mcendgame.framework.stereotype.Service
import de.fuballer.mcendgame.helper.FileHelper
import de.fuballer.mcendgame.helper.TimerTask
import org.bukkit.*
import org.bukkit.plugin.Plugin
import java.io.File
import java.util.*

class WorldManageService(
    private val worldManageRepo: WorldManageRepository
) : Service {
    override fun initialize(plugin: Plugin) {
        startWorldCleaningTimer()
    }

    override fun terminate() {
        deleteAllWorldFiles()
    }

    private fun startWorldCleaningTimer() {
        Timer().schedule(
            TimerTask { checkWorldTimers() },
            WorldSettings.WORLD_EMPTY_TEST_PERIOD,
            WorldSettings.WORLD_EMPTY_TEST_PERIOD
        )
    }

    fun createWorld(mapTier: Int): World {
        val name = "${WorldSettings.WORLD_PREFIX}${UUID.randomUUID()}"
        val worldCreator = WorldCreator(name)
            .type(WorldType.FLAT)
            .generateStructures(false)

        val world = Bukkit.createWorld(worldCreator)!!.apply {
            setGameRule(GameRule.KEEP_INVENTORY, true)
            setGameRule(GameRule.MOB_GRIEFING, false)
            setGameRule(GameRule.DO_MOB_SPAWNING, false)
            setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false)
            setGameRule(GameRule.DO_WEATHER_CYCLE, false)
            setGameRule(GameRule.DO_FIRE_TICK, false)
            setGameRule(GameRule.RANDOM_TICK_SPEED, 0)
            difficulty = Difficulty.HARD
            time = 18000
        }

        val entity = ManagedWorldEntity(name, world, mapTier, 0)
        worldManageRepo.save(entity)

        return world
    }

    private fun deleteAllWorldFiles() {
        worldManageRepo.findAll().forEach {
            Bukkit.unloadWorld(it.world, false)

            val toDelete = File("${MCEndgame.WORLD_CONTAINER}/${it.world.name}")
            FileHelper.deleteFile(toDelete)
        }
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

        Bukkit.getScheduler().runTask(MCEndgame.PLUGIN, Runnable {
            val dungeonWorldDeleteEvent = DungeonWorldDeleteEvent(world)
            EventGateway.apply(dungeonWorldDeleteEvent)

            Bukkit.unloadWorld(name, false)
            worldManageRepo.delete(name)

            val toDelete = File(MCEndgame.WORLD_CONTAINER.toString() + "/" + name)
            FileHelper.deleteFile(toDelete)
        })
    }
}
