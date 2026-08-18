package de.fuballer.mcendgame.client.component.render

import com.mojang.blaze3d.vertex.VertexFormatElement

object CustomVertexFormatElements {
    val GRADIENT_ORIGIN: VertexFormatElement = VertexFormatElement.register(7, 0, VertexFormatElement.Type.FLOAT, false, 3)
    val GRADIENT_BOUNDS: VertexFormatElement = VertexFormatElement.register(8, 0, VertexFormatElement.Type.FLOAT, false, 2)
}