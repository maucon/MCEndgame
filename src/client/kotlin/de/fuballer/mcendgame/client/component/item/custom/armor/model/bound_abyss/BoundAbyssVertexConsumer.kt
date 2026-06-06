package de.fuballer.mcendgame.client.component.item.custom.armor.model.bound_abyss

import com.mojang.blaze3d.vertex.VertexConsumer
import de.fuballer.mcendgame.main.util.ColorUtil
import kotlin.math.max

class BoundAbyssVertexConsumer(
    private val delegate: VertexConsumer,
    private val effectStrength: Double,
) : VertexConsumer {
    override fun addVertex(x: Float, y: Float, z: Float): VertexConsumer {
        delegate.addVertex(x, y, z)
        return this
    }

    override fun setColor(red: Int, green: Int, blue: Int, alpha: Int): VertexConsumer {
        val decrease = (effectStrength * 100).toInt()
        delegate.setColor(red, max(green - decrease, 0), max(blue - decrease, 0), alpha)

        return this
    }

    override fun setColor(argb: Int): VertexConsumer {
        val a = (argb shr 24) and 0xFF
        val r = (argb shr 16) and 0xFF
        val g = (argb shr 8) and 0xFF
        val b = argb and 0xFF

        val decrease = (effectStrength * 100).toInt()

        val color = ColorUtil.rgbaToInt(r, max(g - decrease, 0), max(b - decrease, 0), a)
        delegate.setColor(color)

        return this
    }

    override fun setUv(u: Float, v: Float): VertexConsumer {
        delegate.setUv(u, v)
        return this
    }

    override fun setUv1(u: Int, v: Int): VertexConsumer {
        delegate.setUv1(u, v)
        return this
    }

    override fun setUv2(u: Int, v: Int): VertexConsumer {
        delegate.setUv2(u, v)
        return this
    }

    override fun setNormal(x: Float, y: Float, z: Float): VertexConsumer {
        delegate.setNormal(x, y, z)
        return this
    }

    override fun setLineWidth(width: Float): VertexConsumer {
        delegate.setLineWidth(width)
        return this
    }
}