package de.fuballer.mcendgame.util

import de.fuballer.mcendgame.configuration.PluginConfiguration
import org.bukkit.NamespacedKey
import org.bukkit.OfflinePlayer
import org.bukkit.World
import org.bukkit.WorldCreator
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.scheduler.BukkitRunnable

object PluginUtil {
    // region event
    fun registerEvents(listener: Listener) = PluginConfiguration.pluginManager().registerEvents(listener, PluginConfiguration.plugin())
    // endregion

    // region task
    fun scheduleTask(task: Runnable) = PluginConfiguration.scheduler().runTask(PluginConfiguration.plugin(), task)

    fun scheduleSyncDelayedTask(task: Runnable) = PluginConfiguration.scheduler().scheduleSyncDelayedTask(PluginConfiguration.plugin(), task)

    fun scheduleSyncRepeatingTask(delay: Long, period: Long, task: Runnable) = PluginConfiguration.scheduler().scheduleSyncRepeatingTask(PluginConfiguration.plugin(), task, delay, period)

    fun cancelTask(taskId: Int) = PluginConfiguration.scheduler().cancelTask(taskId)
    // endregion

    // region player
    fun getOnlinePlayers(): Collection<Player> = PluginConfiguration.server().onlinePlayers

    fun getOfflinePlayers(): Array<OfflinePlayer> = PluginConfiguration.server().offlinePlayers
    // endregion

    // region world
    fun createWorld(creator: WorldCreator) = PluginConfiguration.server().createWorld(creator)!!

    fun unloadWorld(world: World) = PluginConfiguration.server().unloadWorld(world, false)
    // endregion

    // region BukkitRunnable
    fun BukkitRunnable.runTaskLater(delay: Long) = runTaskLater(PluginConfiguration.plugin(), delay)

    fun BukkitRunnable.runTaskTimer(delay: Long, period: Long) = runTaskTimer(PluginConfiguration.plugin(), delay, period)
    // endregion

    // region NameSpacedKey
    fun createNamespacedKey(key: String) = NamespacedKey(PluginConfiguration.plugin(), key)

    fun createFixedMetadataValue(value: Any) = FixedMetadataValue(PluginConfiguration.plugin(), value)
    // endregion
}
