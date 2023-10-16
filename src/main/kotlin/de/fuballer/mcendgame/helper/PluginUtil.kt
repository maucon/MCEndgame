package de.fuballer.mcendgame.helper

import de.fuballer.mcendgame.MCEndgame
import org.bukkit.OfflinePlayer
import org.bukkit.World
import org.bukkit.WorldCreator
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryType

object PluginUtil {
    private fun getScheduler() = MCEndgame.INSTANCE.server.scheduler

    fun getLogger() = MCEndgame.INSTANCE.logger
    fun getWorldContainer() = MCEndgame.INSTANCE.server.worldContainer
    fun getDataFolder() = MCEndgame.INSTANCE.dataFolder
    fun getServer() = MCEndgame.INSTANCE.server
    fun getPluginManager() = MCEndgame.INSTANCE.server.pluginManager

    // region event
    fun registerEvents(listener: Listener) = getPluginManager().registerEvents(listener, MCEndgame.INSTANCE)
    // endregion

    // region task
    fun scheduleTask(task: Runnable) = getScheduler().runTask(MCEndgame.INSTANCE, task)

    fun scheduleSyncDelayedTask(task: Runnable) = getScheduler().scheduleSyncDelayedTask(MCEndgame.INSTANCE, task)

    fun scheduleSyncRepeatingTask(delay: Long, period: Long, task: Runnable) = getScheduler().scheduleSyncRepeatingTask(MCEndgame.INSTANCE, task, delay, period)

    fun cancelTask(taskId: Int) = getScheduler().cancelTask(taskId)
    // endregion

    // region player
    fun getOnlinePlayers(): Collection<Player> = getServer().onlinePlayers

    fun getOfflinePlayers(): Array<OfflinePlayer> = getServer().offlinePlayers
    // endregion

    // region inventory
    fun createInventory(type: InventoryType, title: String) = getServer().createInventory(null, type, title)

    fun createInventory(size: Int, title: String) = getServer().createInventory(null, size, title)
    // endregion

    // region world
    fun createWorld(creator: WorldCreator) = getServer().createWorld(creator)!!

    fun unloadWorld(world: World) = getServer().unloadWorld(world, false)
    // endregion
}
