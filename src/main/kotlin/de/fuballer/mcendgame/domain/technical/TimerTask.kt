package de.fuballer.mcendgame.domain.technical

import java.util.TimerTask

data class TimerTask(
    private val runnable: Runnable
) : TimerTask() {
    override fun run() {
        runnable.run()
    }
}
