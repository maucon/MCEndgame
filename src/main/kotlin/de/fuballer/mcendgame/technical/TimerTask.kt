package de.fuballer.mcendgame.technical

import java.util.TimerTask

data class TimerTask(
    private val runnable: Runnable
) : TimerTask() {
    override fun run() {
        runnable.run()
    }
}
