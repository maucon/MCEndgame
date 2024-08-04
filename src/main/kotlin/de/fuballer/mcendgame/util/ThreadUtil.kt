package de.fuballer.mcendgame.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bukkit.Bukkit

object ThreadUtil {
    fun <R> bukkitSync(method: () -> R): R {
        if (Bukkit.isPrimaryThread()) {
            return method.invoke()
        }

        return SchedulingUtil.callSyncMethod {
            return@callSyncMethod method.invoke()
        }.get()
    }

    fun async(method: () -> Unit) = CoroutineScope(Dispatchers.IO).launch {
        method.invoke()
    }
}