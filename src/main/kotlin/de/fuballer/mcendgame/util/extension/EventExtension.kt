package de.fuballer.mcendgame.util.extension

import org.bukkit.event.Cancellable

object EventExtension {
    /**
     * Cancels the [Cancellable] by settings `isCancelled` to `true`.
     * Meaning that the event will be cancelled.
     */
    fun Cancellable.cancel() {
        isCancelled = true
    }
}