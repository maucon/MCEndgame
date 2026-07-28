package de.fuballer.mcendgame.client.component.entity.custom.entities.beastweaver.beastweaver_wolf

import com.mojang.blaze3d.vertex.PoseStack
import de.fuballer.mcendgame.client.accessor.SubmitNodeStorageAccessor
import de.fuballer.mcendgame.client.component.entity.custom.entities.beastweaver.BeastweaverGradientData
import de.fuballer.mcendgame.client.component.entity.custom.entities.beastweaver.BeastweaverRenderer
import de.fuballer.mcendgame.client.component.render.CustomRenderLayers
import de.fuballer.mcendgame.main.component.entity.custom.entities.beastweaver_wolf.BeastweaverWolfEntity
import de.fuballer.mcendgame.main.util.extension.FloatExtension.clampedLerp
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.client.model.geom.ModelLayers
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.client.renderer.rendertype.RenderType
import net.minecraft.client.renderer.state.level.CameraRenderState
import net.minecraft.resources.Identifier

class BeastweaverWolfRenderer(
    context: EntityRendererProvider.Context,
) : MobRenderer<BeastweaverWolfEntity, BeastweaverWolfRenderState, BeastweaverWolfModel>(
    context,
    BeastweaverWolfModel(context.bakeLayer(ModelLayers.WOLF)),
    0.0F,
) {
    companion object {
        val TEXTURE = IdentifierUtil.default("textures/entity/beastweaver/beastweaver_wolf/beastweaver_wolf.png")
    }

    override fun getRenderType(
        state: BeastweaverWolfRenderState,
        isBodyVisible: Boolean,
        forceTransparent: Boolean,
        appearGlowing: Boolean
    ): RenderType {
        val texture = getTextureLocation(state)
        return CustomRenderLayers.beastweaverAttack(texture)
    }

    override fun submit(
        state: BeastweaverWolfRenderState,
        poseStack: PoseStack,
        submitNodeCollector: SubmitNodeCollector,
        camera: CameraRenderState,
    ) {
        poseStack.pushPose()

        val scale = state.scale
        poseStack.scale(scale, scale, scale)

        setupRotations(state, poseStack, state.bodyRot, scale)
        poseStack.scale(-1.0f, -1.0f, 1.0f)
        scale(state, poseStack)
        poseStack.translate(0.0f, -1.501f, 0.0f)

        val isBodyVisible = isBodyVisible(state)
        val forceTransparent = !isBodyVisible && !state.isInvisibleToPlayer
        val renderType = getRenderType(state, isBodyVisible, forceTransparent, state.appearsGlowing())

        val overlayCoords = getOverlayCoords(state, getWhiteOverlayProgress(state))

        val accessor = submitNodeCollector as SubmitNodeStorageAccessor
        accessor.`mcendgame$submitBeastweaverGradientModel`(
            model,
            state,
            poseStack,
            renderType,
            state.lightCoords,
            overlayCoords,
            BeastweaverRenderer.ATTACK_COLOR,
            null,
            state.outlineColor,
            null,
            state.gradientData
        )

        poseStack.popPose()
    }

    override fun createRenderState(): BeastweaverWolfRenderState = BeastweaverWolfRenderState()

    override fun getTextureLocation(state: BeastweaverWolfRenderState): Identifier = TEXTURE

    override fun extractRenderState(entity: BeastweaverWolfEntity, state: BeastweaverWolfRenderState, partialTicks: Float) {
        super.extractRenderState(entity, state, partialTicks)
        state.isAngry = entity.isAngry()
        state.isSitting = entity.isInSittingPose
        state.tailAngle = entity.tailAngle
        state.headRollAngle = entity.getHeadRollAngle(partialTicks)
        state.shakeAnim = entity.getShakeAnim(partialTicks)
        state.texture = entity.getTexture()
        state.wetShade = entity.getWetShade(partialTicks)
        state.collarColor = null
        state.bodyArmorItem = entity.bodyArmorItem.copy()

        val gradientOrigin = BeastweaverRenderer.GET_CAMERA_RELATIVE_ENTITY_POS(entity, partialTicks).add(0F, entity.bbHeight / 2F, 0F)
        val tickCount = entity.tickCount
        val scale = entity.scale
        val gradientStart = ((tickCount - 20F) / 40F).coerceIn(0F, 40F).clampedLerp(0F, 1F) * scale
        val gradientEnd = (tickCount / 40F).coerceIn(0F, 1F).clampedLerp(0F, 1F) * scale
        state.gradientData = BeastweaverGradientData(gradientOrigin.x, gradientOrigin.y, gradientOrigin.z, gradientStart, gradientEnd)
    }
}