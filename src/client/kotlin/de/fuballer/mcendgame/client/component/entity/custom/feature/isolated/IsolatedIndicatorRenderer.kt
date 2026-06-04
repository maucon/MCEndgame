package de.fuballer.mcendgame.client.component.entity.custom.feature.isolated

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import de.fuballer.mcendgame.client.util.EntityRenderStateMixinExtension.isIsolated
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributeUtil.canSeeIsolated
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState
import net.minecraft.client.renderer.rendertype.RenderTypes
import net.minecraft.client.renderer.texture.OverlayTexture

private val ICON_TEXTURE = IdentifierUtil.default("textures/indicator/isolated.png")

object IsolatedIndicatorRenderer {
    fun tryRender(
        state: LivingEntityRenderState,
        matrices: PoseStack,
        queue: SubmitNodeCollector,
        light: Int,
        isGeoEntity: Boolean = false,
    ) {
        if (state.deathTime > 0) return

        if (!state.isIsolated()) return
        val player = Minecraft.getInstance().player ?: return
        if (!player.canSeeIsolated()) return

        renderIcon(state, matrices, queue, light, isGeoEntity)
    }

    private fun renderIcon(
        state: LivingEntityRenderState,
        matrices: PoseStack,
        queue: SubmitNodeCollector,
        light: Int,
        isGeoEntity: Boolean,
    ) {
        matrices.pushPose()

        if (isGeoEntity) matrices.scale(-1f, -1f, 1f) // revert geckolibs axis flips

        val yOffset = (if (isGeoEntity) 0.0 else 1.5) - state.entityType.height - 0.5 // 1.5 moves origin to feet for vanilla entities
        matrices.translate(0.0, yOffset, 0.0)

        val camera = Minecraft.getInstance().gameRenderer.mainCamera
        matrices.mulPose(Axis.YN.rotationDegrees(state.bodyRot - camera.yRot()))
        matrices.mulPose(Axis.XP.rotationDegrees(camera.xRot()))

        queue.submitCustomGeometry(matrices, RenderTypes.entityCutoutNoCull(ICON_TEXTURE)) { entry, vertexConsumer ->
            val matrix = entry.pose()

            vertexConsumer.addVertex(matrix, -0.2f, -0.2f, 0f)
                .setColor(255, 255, 255, 255)
                .setUv(0f, 0f)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(0f, 0f, 1f)

            vertexConsumer.addVertex(matrix, 0.2f, -0.2f, 0f)
                .setColor(255, 255, 255, 255)
                .setUv(1f, 0f)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(0f, 0f, 1f)

            vertexConsumer.addVertex(matrix, 0.2f, 0.2f, 0f)
                .setColor(255, 255, 255, 255)
                .setUv(1f, 1f)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(0f, 0f, 1f)

            vertexConsumer.addVertex(matrix, -0.2f, 0.2f, 0f)
                .setColor(255, 255, 255, 255)
                .setUv(0f, 1f)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(0f, 0f, 1f)
        }

        matrices.popPose()
    }
}