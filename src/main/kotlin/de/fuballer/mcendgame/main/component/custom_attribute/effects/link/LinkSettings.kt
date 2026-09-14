package de.fuballer.mcendgame.main.component.custom_attribute.effects.link

import java.awt.Color
import kotlin.math.roundToInt

object LinkSettings {
    const val LINK_UPDATE_INTERVAL = 1
    const val LINK_DAMAGE_INTERVAL = 20
    const val LINK_DISTANCE_BREAK_PADDING = 1F
    const val MAX_LINK_DISTANCE = 64

    fun getLinkConnectingTime(distance: Double) = distance.toInt() + 1

    const val LINK_CONNECTION_HEIGHT = 0.7

    const val LINK_RENDER_SEGMENT_LENGTH = 0.2
    const val LINK_RENDER_SEGMENT_WIDTH = 0.03

    const val LINK_RENDER_MAX_THICKNESS_FACTOR = 0.8

    const val LINK_RENDER_SINE_VERTICAL_STRENGTH = 0.25
    const val LINK_RENDER_SINE_HORIZONTAL_STRENGTH = 0.1
    const val LINK_RENDER_SINE_SPEED = 0.15

    val LINK_ORIGIN_COLOR = Color(40, 5, 60, 200)
    val LINK_TARGET_COLOR = Color(120, 50, 220, 40)
    fun getColor(percentage: Double): Int {
        val p = percentage.coerceIn(0.0, 1.0)
        val r = (LINK_ORIGIN_COLOR.red + (LINK_TARGET_COLOR.red - LINK_ORIGIN_COLOR.red) * p).roundToInt()
        val g = (LINK_ORIGIN_COLOR.green + (LINK_TARGET_COLOR.green - LINK_ORIGIN_COLOR.green) * p).roundToInt()
        val b = (LINK_ORIGIN_COLOR.blue + (LINK_TARGET_COLOR.blue - LINK_ORIGIN_COLOR.blue) * p).roundToInt()
        val a = (LINK_ORIGIN_COLOR.alpha + (LINK_TARGET_COLOR.alpha - LINK_ORIGIN_COLOR.alpha) * p).roundToInt()
        return Color(r, g, b, a).rgb
    }
}