package de.fuballer.mcendgame.client.component.entity.custom.feature.webbed

import com.mojang.blaze3d.vertex.PoseStack
import de.fuballer.mcendgame.client.util.EntityRenderStateMixinExtension.isWebbed
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.client.model.EntityModel
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.RenderLayerParent
import net.minecraft.client.renderer.entity.layers.RenderLayer
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState
import net.minecraft.client.renderer.rendertype.RenderTypes
import net.minecraft.client.renderer.texture.OverlayTexture
import org.joml.Vector3f
import kotlin.math.max

class WebbedFeatureRenderer<T : LivingEntityRenderState, M : EntityModel<T>>(
    featureContext: RenderLayerParent<T, M>,
    ctx: EntityRendererProvider.Context,
) : RenderLayer<T, M>(featureContext) {
    val model = WebbedModel(ctx.bakeLayer(WebbedModel.WEBBED_LAYER))

    companion object {
        private val TEXTURE = IdentifierUtil.default("textures/entity/feature/webbed/webbed.png")
    }

    override fun submit(
        matrices: PoseStack,
        queue: SubmitNodeCollector,
        light: Int,
        state: T,
        limbAngle: Float,
        limbDistance: Float
    ) {
        if (!state.isWebbed()) return

        matrices.pushPose()

        // Players are rendered smaller due to an unknown scaling factor.
        // This might also apply to other mobs.
        val invScale = 1F / getMatrixStackHeightScale(matrices)

        // 1.5 reverts translate in LivingEntityRenderer
        val height = max(state.eyeHeight * 0.875F, state.boundingBoxHeight * 0.6F)
        val translateY = 1.5F - (height * invScale)
        matrices.translate(0F, translateY, 0F)

        val scaleX = state.boundingBoxWidth * 1.1F * invScale
        val scaleY = state.eyeHeight * 0.75F * invScale * (2F / 3F) // model is 1.5 blocks
        matrices.scale(scaleX, scaleY, scaleX)

        queue.submitModel(
            model,
            WebbedModel.WebbedData(),
            matrices,
            RenderTypes.entityCutout(TEXTURE),
            light,
            OverlayTexture.NO_OVERLAY,
            state.outlineColor,
            null,
        )

        matrices.popPose()
    }

    private fun getMatrixStackHeightScale(matrices: PoseStack): Float {
        val entry = matrices.last()
        val modelMatrix = entry.pose()

        //val xAxis = Vector3f(modelMatrix.m00(), modelMatrix.m01(), modelMatrix.m02())
        val yAxis = Vector3f(modelMatrix.m10(), modelMatrix.m11(), modelMatrix.m12())
        //val zAxis = Vector3f(modelMatrix.m20(), modelMatrix.m21(), modelMatrix.m22())

        return yAxis.length()
    }
}