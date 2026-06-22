package de.fuballer.mcendgame.client.component.entity.custom.entities.skeleton_mage

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.model.EntityModel
import net.minecraft.client.model.geom.EntityModelSet
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.entity.RenderLayerParent
import net.minecraft.client.renderer.entity.layers.RenderLayer
import net.minecraft.client.renderer.rendertype.RenderTypes
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.resources.Identifier

class SkeletonMageInnerLayer<M : EntityModel<SkeletonMageRenderState>>(
    renderer: RenderLayerParent<SkeletonMageRenderState, M>,
    models: EntityModelSet,
    layerLocation: ModelLayerLocation,
    private val textures: List<Identifier>,
    private val textureTicks: Int = 2,
) : RenderLayer<SkeletonMageRenderState, M>(renderer) {
    private val layerModel = SkeletonMageModel(models.bakeLayer(layerLocation))

    override fun submit(
        poseStack: PoseStack,
        submitNodeCollector: SubmitNodeCollector,
        lightCoords: Int,
        state: SkeletonMageRenderState,
        yRot: Float,
        xRot: Float
    ) {
        val tickAge = state.ageInTicks
        val texture = textures[state.ageInTicks.toInt() / textureTicks % textures.size]
        submitNodeCollector.order(1).submitModel(
            layerModel,
            state,
            poseStack,
            RenderTypes.energySwirl(texture, 0f, tickAge * 0.01F % 1.0F),
            -1,
            OverlayTexture.NO_OVERLAY,
            -1,
            null,
            state.outlineColor,
            null
        )
    }
}