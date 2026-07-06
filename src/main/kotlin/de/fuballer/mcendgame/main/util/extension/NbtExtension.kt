package de.fuballer.mcendgame.main.util.extension

import com.mojang.serialization.Codec
import net.minecraft.nbt.NbtCompound
import java.util.*

object NbtExtension {
    fun <T : Any> NbtCompound.getSafe(key: String, codec: Codec<T>): Optional<T> {
        return try {
            getSafe(key, codec)
        } catch (e: Exception) {
            Optional.empty()
        }
    }
}