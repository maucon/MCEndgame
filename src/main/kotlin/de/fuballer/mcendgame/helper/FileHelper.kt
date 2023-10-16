package de.fuballer.mcendgame.helper

import java.io.File

object FileHelper {
    fun deleteFile(file: File) {
        if (file.isDirectory) {
            file.list()!!
                .map { File(file, it) }
                .forEach { deleteFile(it) }
        }

        try {
            if (file.delete()) return
        } catch (_: Exception) {
        }

        PluginUtil.getLogger().warning("Couldn't delete file: " + file.path)
    }
}
