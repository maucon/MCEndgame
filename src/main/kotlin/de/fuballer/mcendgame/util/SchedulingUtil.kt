package de.fuballer.mcendgame.util

import de.fuballer.mcendgame.configuration.PluginConfiguration

object SchedulingUtil {
    /**
     * Returns a task that will run on the next server tick.
     *
     * @param task the task to be run
     * @return a BukkitTask that contains the id number
     * @throws IllegalArgumentException if task is null
     */
    fun runTask(task: Runnable) = PluginConfiguration.scheduler().runTask(PluginConfiguration.plugin(), task)

    /**
     * Returns a task that will repeatedly run until cancelled, starting after
     * the specified number of server ticks.
     *
     * @param task the task to be run
     * @param period the ticks to wait before running the task and the ticks to wait between runs
     * @return a BukkitTask that contains the id number
     * @throws IllegalArgumentException if task is null
     */
    fun runTaskTimer(period: Long, task: Runnable) = PluginConfiguration.scheduler().runTaskTimer(PluginConfiguration.plugin(), task, period, period)

    /**
     * Returns a task that will run after the specified number of server
     * ticks.
     *
     * @param plugin the reference to the plugin scheduling task
     * @param task the task to be run
     * @param delay the ticks to wait before running the task
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalArgumentException if task is null
     */
    fun runTaskLater(delay: Long, task: Runnable) = PluginConfiguration.scheduler().runTaskLater(PluginConfiguration.plugin(), task, delay)

    /**
     * Schedules a once off task to occur as soon as possible.
     * <p>
     * This task will be executed by the main server thread.
     *
     * @param task Task to be executed
     * @return Task id number (-1 if scheduling failed)
     */
    fun scheduleSyncDelayedTask(task: Runnable) = PluginConfiguration.scheduler().scheduleSyncDelayedTask(PluginConfiguration.plugin(), task)

    /**
     * Schedules a repeating task.
     * <p>
     * This task will be executed by the main server thread.
     *
     * @param task Task to be executed
     * @param delay Delay in server ticks before executing first repeat
     * @param period Period in server ticks of the task
     * @return Task id number (-1 if scheduling failed)
     */
    fun scheduleSyncRepeatingTask(delay: Long, period: Long, task: Runnable) = PluginConfiguration.scheduler().scheduleSyncRepeatingTask(PluginConfiguration.plugin(), task, delay, period)

    /**
     * Removes task from scheduler.
     *
     * @param taskId ID number of task to be removed
     */
    fun cancelTask(taskId: Int) = PluginConfiguration.scheduler().cancelTask(taskId)
}