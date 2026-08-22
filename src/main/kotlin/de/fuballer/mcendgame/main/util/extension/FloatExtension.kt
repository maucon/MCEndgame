package de.fuballer.mcendgame.main.util.extension

import net.minecraft.util.Mth

object FloatExtension {
    fun Float.clampedLerp(start: Float, end: Float): Float = Mth.lerp(coerceIn(0f, 1f), start, end)
}