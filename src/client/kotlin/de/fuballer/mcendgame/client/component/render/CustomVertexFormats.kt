package de.fuballer.mcendgame.client.component.render

import com.mojang.blaze3d.vertex.VertexFormat
import com.mojang.blaze3d.vertex.VertexFormatElement

object CustomVertexFormats {
    val BEASTWEAVER_ATTACK: VertexFormat = VertexFormat.builder()
        .add("Position", VertexFormatElement.POSITION)
        .add("Color", VertexFormatElement.COLOR)
        .add("UV0", VertexFormatElement.UV0)
        .add("UV1", VertexFormatElement.UV1)
        .add("UV2", VertexFormatElement.UV2)
        .add("Normal", VertexFormatElement.NORMAL)
        .add("GradientOrigin", CustomVertexFormatElements.GRADIENT_ORIGIN)
        .add("GradientBounds", CustomVertexFormatElements.GRADIENT_BOUNDS)
        .padding(1)
        .build()
}