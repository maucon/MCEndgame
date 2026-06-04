package de.fuballer.mcendgame.client.component.entity.custom.entities.webshot

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import de.fuballer.mcendgame.main.component.entity.custom.entities.webshot.WebshotEntity
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.rendertype.RenderTypes
import net.minecraft.client.renderer.state.CameraRenderState
import net.minecraft.client.renderer.texture.OverlayTexture

class WebshotRenderer(
    context: EntityRendererProvider.Context,
) : EntityRenderer<WebshotEntity, WebshotRenderState>(context) {
    val model = WebshotEntityModel(context.bakeLayer(WebshotEntityModel.WEBSHOT))

    companion object {
        val TEXTURE = IdentifierUtil.default("textures/entity/webshot/webshot.png")
    }

    override fun submit(
        renderState: WebshotRenderState,
        matrices: PoseStack,
        queue: SubmitNodeCollector,
        cameraState: CameraRenderState
    ) {
        matrices.pushPose()
        matrices.translate(0.0f, 0.15f, 0.0f)
        matrices.mulPose(Axis.YP.rotationDegrees(renderState.yRot + 180))
        matrices.mulPose(Axis.XP.rotationDegrees(renderState.xRot))
        model.setupAnim(renderState)

        queue.submitModel(
            model,
            renderState,
            matrices,
            RenderTypes.entityCutout(TEXTURE),
            renderState.lightCoords,
            OverlayTexture.NO_OVERLAY,
            renderState.outlineColor,
            null,
        )
        matrices.popPose()

        super.submit(renderState, matrices, queue, cameraState)
    }

    override fun createRenderState(): WebshotRenderState = WebshotRenderState()

    override fun extractRenderState(
        webshotEntity: WebshotEntity,
        webshotRenderState: WebshotRenderState,
        tickDelta: Float
    ) {
        super.extractRenderState(webshotEntity, webshotRenderState, tickDelta)
        webshotRenderState.xRot = webshotEntity.getXRot(tickDelta)
        webshotRenderState.yRot = webshotEntity.getYRot(tickDelta)
    }
}