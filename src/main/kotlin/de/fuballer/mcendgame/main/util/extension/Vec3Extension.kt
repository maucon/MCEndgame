package de.fuballer.mcendgame.main.util.extension

import net.minecraft.util.Mth
import net.minecraft.world.phys.Vec3
import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

object Vec3Extension {
    private fun Vec3.horizontalAngleDeg(to: Vec3): Double {
        val horizontal = horizontal()
        val toHorizontal = to.horizontal()

        val dot = horizontal.dot(toHorizontal)
        val lengthProduct = horizontal.length() * toHorizontal.length()
        if (lengthProduct == 0.0) return 0.0

        val cos = (dot / lengthProduct).coerceIn(-1.0, 1.0)
        val rad = acos(cos)
        return rad * 180 / PI
    }

    fun Vec3.getYaw(): Double {
        val angle = horizontalAngleDeg(Vec3(0.0, 0.0, 1.0))
        return if (x > 0) -angle else angle
    }

    fun Vec3.angleRad(other: Vec3): Double {
        val lengths = length() * other.length()
        if (lengths == 0.0) return 0.0

        val dotProduct = dot(other)
        val cosine = Mth.clamp(dotProduct / lengths, -1.0, 1.0)
        return acos(cosine)
    }

    fun Vec3.angleDeg(other: Vec3) = Math.toDegrees(angleRad(other))

    fun Vec3.rotateHorizontalVectorUpwards(degrees: Double): Vec3 {
        val radians = Math.toRadians(degrees)

        val horizontalScale = cos(radians)
        val y = sin(radians)

        return Vec3(
            x * horizontalScale,
            length() * y,
            z * horizontalScale,
        )
    }
}