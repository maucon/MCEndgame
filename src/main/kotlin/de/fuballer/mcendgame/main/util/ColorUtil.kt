package de.fuballer.mcendgame.main.util

object ColorUtil {
    fun rgbaToInt(r: Int, g: Int, b: Int, a: Int) =
        (a and 0xFF shl 24) or
                (r and 0xFF shl 16) or
                (g and 0xFF shl 8) or
                (b and 0xFF)

    fun multiplyAlpha(color: Int, multiplier: Float): Int {
        val currentAlpha = (color shr 24) and 0xFF
        val newAlpha = (currentAlpha * multiplier).toInt().coerceIn(0, 255)

        return (color and 0x00FFFFFF) or (newAlpha shl 24)
    }
}