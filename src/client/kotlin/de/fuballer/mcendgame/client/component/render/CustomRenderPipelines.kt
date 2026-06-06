package de.fuballer.mcendgame.client.component.render

import com.mojang.blaze3d.pipeline.BlendFunction
import com.mojang.blaze3d.pipeline.ColorTargetState
import com.mojang.blaze3d.pipeline.DepthStencilState
import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.platform.CompareOp
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.client.renderer.RenderPipelines

object CustomRenderPipelines {
    val LINK_PIPELINE: RenderPipeline = RenderPipelines.register(
        RenderPipeline.builder(RenderPipelines.MATRICES_FOG_SNIPPET)
            .withLocation("pipeline/link")
            .withVertexShader("core/position_color")
            .withFragmentShader("core/position_color")
            .withColorTargetState(ColorTargetState(BlendFunction.TRANSLUCENT))
            .withCull(false)
			.withDepthStencilState(DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, true))
            .withVertexFormat(DefaultVertexFormat.POSITION_COLOR_LIGHTMAP, VertexFormat.Mode.TRIANGLE_STRIP)
            .build()
    )

    val GHOSTLY_PIPELINE: RenderPipeline = RenderPipelines.register(
        RenderPipeline.builder(RenderPipelines.ENTITY_EMISSIVE_SNIPPET)
            .withLocation("pipeline/entity_translucent_emissive")
            .withShaderDefine("ALPHA_CUTOUT", 0.1f)
            .withShaderDefine("PER_FACE_LIGHTING")
            .withSampler("Sampler1")
            .withColorTargetState(ColorTargetState(BlendFunction.TRANSLUCENT))
            .withCull(false)
            .withDepthStencilState(DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, true))
            .build()
    )
}