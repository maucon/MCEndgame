package de.fuballer.mcendgame.main.functional.scheduler

import de.fuballer.mcendgame.main.configuration.RuntimeConfig
import de.fuballer.mcendgame.main.messaging.server.ServerEndTickEvent
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import java.util.*

@Injectable
class Scheduler(
    private val taskRepo: TaskRepository
) {
    @EventSubscriber(sync = true)
    fun on(event: ServerEndTickEvent) {
        taskRepo.findAll().forEach { task ->
            val tickDifference = event.server.tickCount - task.startTick
            if (tickDifference < 0) return@forEach

            if (task.period == NOT_REPEATING) {
                task.runnable(0)
                taskRepo.delete(task)

                return@forEach
            }

            if (task.repeatDuration != INFINITE_REPEATING && tickDifference > task.repeatDuration) {
                taskRepo.delete(task)
                return@forEach
            }

            if (tickDifference % task.period != 0) return@forEach
            task.runnable(tickDifference)
        }
    }

    fun repeating(period: Int, runnable: (Int) -> Unit): UUID {
        return repeating(0, period, runnable)
    }

    fun repeating(delay: Int, period: Int, runnable: (Int) -> Unit): UUID {
        require(period > 0)

        val startTick = RuntimeConfig.SERVER.tickCount + delay
        val task = Task(runnable, startTick, period)

        return taskRepo.save(task).id
    }

    fun repeatingForDuration(period: Int, duration: Int, runnable: (Int) -> Unit): UUID {
        return repeatingForDuration(0, period, duration, runnable)
    }

    fun repeatingForDuration(delay: Int, period: Int, duration: Int, runnable: (Int) -> Unit): UUID {
        require(period > 0)

        val startTick = RuntimeConfig.SERVER.tickCount + delay
        val task = Task(runnable, startTick, period, duration)

        return taskRepo.save(task).id
    }


    fun delayed(delay: Int, runnable: (Int) -> Unit): UUID {
        val startTick = RuntimeConfig.SERVER.tickCount + delay
        val task = Task(runnable, startTick)

        return taskRepo.save(task).id
    }
}