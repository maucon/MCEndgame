package de.fuballer.mcendgame.client.component.entity.custom.entities.portal

import com.mojang.blaze3d.vertex.PoseStack
import de.fuballer.mcendgame.client.component.entity.custom.entities.portal.type.PortalRenderType
import de.fuballer.mcendgame.client.component.entity.custom.entities.portal.type.default_.DefaultPortalEntityModel
import de.fuballer.mcendgame.main.component.portal.PortalEntity
import net.minecraft.client.model.EntityModel
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.LivingEntityRenderer
import net.minecraft.client.renderer.rendertype.RenderType
import net.minecraft.client.renderer.state.CameraRenderState
import net.minecraft.resources.Identifier

class PortalRenderer(
    private val context: EntityRendererProvider.Context,
) : LivingEntityRenderer<PortalEntity, PortalRenderState, EntityModel<PortalRenderState>>(
    context,
    DefaultPortalEntityModel(context.bakeLayer(DefaultPortalEntityModel.PORTAL)),
    0.0F
) {
    override fun createRenderState(): PortalRenderState =
        PortalRenderState()

    // do not render the name label
    override fun submitNameTag(state: PortalRenderState, matrices: PoseStack, queue: SubmitNodeCollector, cameraRenderState: CameraRenderState) {}

    override fun getTextureLocation(state: PortalRenderState): Identifier = state.type.getTexture(state.ageInTicks)

    override fun getRenderType(state: PortalRenderState, showBody: Boolean, translucent: Boolean, showOutline: Boolean): RenderType? {
        return state.type.getRenderLayer(this, state, showBody, translucent, showOutline)
            ?: super.getRenderType(state, showBody, translucent, showOutline)
    }

    override fun getShadowRadius(state: PortalRenderState): Float {
        return state.type.getShadowRadius()
    }

    override fun extractRenderState(
        entity: PortalEntity,
        state: PortalRenderState,
        tickDelta: Float
    ) {
        super.extractRenderState(entity, state, tickDelta)
        val typeId = entity.entityData.get(PortalEntity.TYPE)
        state.type = PortalRenderType.getType(typeId)

        state.openAnimationState.copyFrom(entity.type.openAnimationState)
        state.idleAnimationState.copyFrom(entity.type.idleAnimationState)
        state.closeAnimationState.copyFrom(entity.type.closeAnimationState)
    }

    override fun submit(
        state: PortalRenderState, matrixStack: PoseStack, queue: SubmitNodeCollector, cameraRenderState: CameraRenderState
    ) {
        this.model = state.type.getModel(context)
        super.submit(state, matrixStack, queue, cameraRenderState)
    }
}