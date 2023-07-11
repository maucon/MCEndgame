package de.fuballer.mcendgame.db

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import de.fuballer.mcendgame.MCEndgame
import de.fuballer.mcendgame.framework.stereotype.Entity
import java.io.File
import java.io.FileReader
import java.lang.reflect.ParameterizedType
import java.nio.file.Files
import java.nio.file.StandardOpenOption

const val CONFIG_FILE_SUFFIX = ".db"
const val CONFIG_FILE_EXTENSION = "json"

open class PersistableMapRepository<ID, ENTITY : Entity<ID>> : AbstractMapRepository<ID, ENTITY>() {
    // please don't ask me about it ಠ╭╮ಠ
    private val entityClass = (this.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1]
    private val gson = GsonBuilder().setPrettyPrinting().create()

    private lateinit var dbFile: File
    private val fileName: String

    init {
        val typeName = entityClass.typeName
        val lastDotIndex = typeName.lastIndexOf(".") + 1
        val className = typeName.subSequence(lastDotIndex, typeName.length)

        fileName = "$className${CONFIG_FILE_SUFFIX}.${CONFIG_FILE_EXTENSION}"
    }

    override fun load() {
        dbFile = File(MCEndgame.DATA_FOLDER, fileName)
        if (!dbFile.exists()) return

        val reader = FileReader(dbFile)
        val entityListType = TypeToken.getParameterized(List::class.java, entityClass).type

        this.map = gson.fromJson<List<ENTITY>>(reader, entityListType)
            .associateBy { it.id }
            .toMutableMap()
    }

    override fun flush() {
        val jsonBytes = gson.toJson(map.values).toByteArray()

        dbFile.delete()
        Files.write(dbFile.toPath(), jsonBytes, StandardOpenOption.CREATE, StandardOpenOption.WRITE)
    }
}
