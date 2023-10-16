package de.fuballer.mcendgame.helper

import de.fuballer.mcendgame.framework.stereotype.Service
import java.io.File
import java.util.logging.Logger

class FileHelper(
    private val logger: Logger
) : Service {
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

        logger.warning("Couldn't delete file: " + file.path)
    }
}
