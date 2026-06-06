package de.fuballer.mcendgame.client.component.item.custom.armor

import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.state.EntityRenderState

interface CustomVertexConsumer {
    fun getVertexConsumer(
        renderState: EntityRenderState,
        provider: MultiBufferSource,
        default: VertexConsumer,
    ): VertexConsumer
}