package de.fuballer.mcendgame.client.component.entity.custom.entities.swamp_golem

import de.fuballer.mcendgame.main.component.entity.custom.entities.swamp_golem.SwampGolemEntity
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.MobRenderer

class SwampGolemRenderer(
    context: EntityRendererProvider.Context,
) : MobRenderer<SwampGolemEntity, SwampGolemRenderState, SwampGolemEntityModel>(
    context,
    SwampGolemEntityModel(context.bakeLayer(SwampGolemEntityModel.SWAMP_GOLEM)),
    0.65F //shadow
) {
    override fun createRenderState(): SwampGolemRenderState =
        SwampGolemRenderState()

    override fun getTextureLocation(state: SwampGolemRenderState) =
        IdentifierUtil.default("textures/entity/swamp_golem/swamp_golem.png")

    override fun extractRenderState(
        entity: SwampGolemEntity,
        renderState: SwampGolemRenderState,
        tickDelta: Float
    ) {
        super.extractRenderState(entity, renderState, tickDelta)
        renderState.slamAnimationState.copyFrom(entity.slamAnimationState)
        renderState.idleAnimationState.copyFrom(entity.idleAnimationState)
        renderState.walkAnimationState.copyFrom(entity.walkAnimationState)
    }
}