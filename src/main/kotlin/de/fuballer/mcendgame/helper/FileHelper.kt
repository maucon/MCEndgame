package de.fuballer.mcendgame.helper

import org.bukkit.Bukkit
import java.io.File

object FileHelper {
    fun deleteFile(file: File) {
        if (file.isDirectory) {
            file.list()?.also { subFiles ->
                for (subFile in subFiles) {
                    deleteFile(File(file, subFile))
                }
            }
        }

        try {
            if (!file.delete()) {
                Bukkit.getLogger().warning("Couldn't delete file: " + file.path)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
