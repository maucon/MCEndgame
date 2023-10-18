package de.fuballer.mcendgame.domain.data_class

import java.util.TimerTask

data class TimerTask(
    private val runnable: Runnable
) : TimerTask() {
    override fun run() {
        runnable.run()
    }
}