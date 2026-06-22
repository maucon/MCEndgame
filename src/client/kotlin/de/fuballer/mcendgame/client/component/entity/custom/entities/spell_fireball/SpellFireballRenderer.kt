package de.fuballer.mcendgame.client.component.entity.custom.entities.spell_fireball

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import de.fuballer.mcendgame.main.component.entity.custom.entities.spell_fireball.SpellFireballEntity
import de.fuballer.mcendgame.main.util.ColorUtil
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.rendertype.RenderTypes
import net.minecraft.client.renderer.state.level.CameraRenderState
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.util.Mth

class SpellFireballRenderer(
    context: EntityRendererProvider.Context,
) : EntityRenderer<SpellFireballEntity, SpellFireballRenderState>(context) {
    val model = SpellFireballEntityModel(context.bakeLayer(SpellFireballEntityModel.SPELL_FIREBALL))
    val modelOuter = SpellFireballEntityModel(context.bakeLayer(SpellFireballEntityModel.SPELL_FIREBALL_OUTER))

    companion object {
        private val TEXTURE = IdentifierUtil.default("textures/entity/spell_fireball/spell_fireball.png")
        private val OUTER_TEXTURES = listOf(
            IdentifierUtil.default("textures/entity/spell_fireball/spell_fireball_outer_0.png"),
            IdentifierUtil.default("textures/entity/spell_fireball/spell_fireball_outer_1.png"),
            IdentifierUtil.default("textures/entity/spell_fireball/spell_fireball_outer_2.png"),
            IdentifierUtil.default("textures/entity/spell_fireball/spell_fireball_outer_3.png"),
            IdentifierUtil.default("textures/entity/spell_fireball/spell_fireball_outer_4.png"),
            IdentifierUtil.default("textures/entity/spell_fireball/spell_fireball_outer_5.png"),
            IdentifierUtil.default("textures/entity/spell_fireball/spell_fireball_outer_6.png"),
            IdentifierUtil.default("textures/entity/spell_fireball/spell_fireball_outer_7.png"),
            IdentifierUtil.default("textures/entity/spell_fireball/spell_fireball_outer_8.png"),
            IdentifierUtil.default("textures/entity/spell_fireball/spell_fireball_outer_9.png"),
        )

        private val COLOR = ColorUtil.rgbaToInt(255, 255, 255, 255)
    }

    override fun createRenderState() = SpellFireballRenderState()

    override fun submit(
        state: SpellFireballRenderState,
        poseStack: PoseStack,
        queue: SubmitNodeCollector,
        cameraState: CameraRenderState
    ) {
        poseStack.pushPose()
        poseStack.scale(-1.0F, -1.0F, 1.0F)
        poseStack.mulPose(Axis.YP.rotationDegrees(180 + state.yRot))
        poseStack.mulPose(Axis.XP.rotationDegrees(state.xRot))
        model.setupAnim(state)

        queue.submitModel(
            model,
            state,
            poseStack,
            RenderTypes.entityCutout(TEXTURE),
            state.lightCoords,
            OverlayTexture.NO_OVERLAY,
            state.outlineColor,
            null,
        )

        submitOuter(state, poseStack, queue, cameraState)

        poseStack.popPose()
    }

    private fun submitOuter(
        state: SpellFireballRenderState,
        poseStack: PoseStack,
        queue: SubmitNodeCollector,
        cameraState: CameraRenderState,
    ) {
        val texture = OUTER_TEXTURES[state.ageInTicks.toInt() / 2 % OUTER_TEXTURES.size]
        queue.order(1).submitModel(
            modelOuter,
            state,
            poseStack,
            RenderTypes.eyes(texture),
            -1,
            OverlayTexture.NO_OVERLAY,
            COLOR,
            null,
            state.outlineColor,
            null
        )
    }

    override fun extractRenderState(
        entity: SpellFireballEntity,
        state: SpellFireballRenderState,
        tickDelta: Float,
    ) {
        super.extractRenderState(entity, state, tickDelta)

        val velocity = entity.deltaMovement
        val horizontal = velocity.horizontalDistance()
        state.yRot = (Mth.atan2(velocity.z, velocity.x) * Mth.RAD_TO_DEG).toFloat() - 90f
        state.xRot = (-(Mth.atan2(velocity.y, horizontal) * Mth.RAD_TO_DEG)).toFloat()
    }
}