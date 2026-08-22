package de.fuballer.mcendgame.client.component.render

import com.mojang.blaze3d.GpuFormat
import com.mojang.blaze3d.vertex.VertexFormat

object CustomVertexFormats {
    private val POSITION_FORMAT: GpuFormat = GpuFormat.RGB32_FLOAT
    private val COLOR_FORMAT: GpuFormat = GpuFormat.RGBA8_UNORM
    private val UV0_FORMAT: GpuFormat = GpuFormat.RG32_FLOAT
    private val UV1_FORMAT: GpuFormat = GpuFormat.RG16_SINT
    private val UV2_FORMAT: GpuFormat = GpuFormat.RG16_SINT
    private val NORMAL_FORMAT: GpuFormat = GpuFormat.RGBA8_SNORM

    private val GRADIENT_ORIGIN_FORMAT: GpuFormat = GpuFormat.RGB32_FLOAT
    private val GRADIENT_BOUNDS_FORMAT: GpuFormat = GpuFormat.RG32_FLOAT

    val BEASTWEAVER_ATTACK: VertexFormat = VertexFormat.builder(0)
        .addAttribute("Position", POSITION_FORMAT)
        .addAttribute("Color", COLOR_FORMAT)
        .addAttribute("UV0", UV0_FORMAT)
        .addAttribute("UV1", UV1_FORMAT)
        .addAttribute("UV2", UV2_FORMAT)
        .addAttribute("Normal", NORMAL_FORMAT)
        .addAttribute("GradientOrigin", GRADIENT_ORIGIN_FORMAT)
        .addAttribute("GradientBounds", GRADIENT_BOUNDS_FORMAT)
        .build()
}