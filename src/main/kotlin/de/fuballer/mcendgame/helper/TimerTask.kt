package de.fuballer.mcendgame.helper

import java.util.TimerTask

class TimerTask(
    private val runnable: Runnable
) : TimerTask() {
    override fun run() {
        runnable.run()
    }
}
