package de.fuballer.mcendgame.client.component.entity.custom.entities.beastweaver

import com.geckolib.cache.model.GeoBone
import com.geckolib.cache.model.cuboid.CuboidGeoBone
import com.geckolib.constant.dataticket.DataTicket
import com.geckolib.renderer.base.GeoRenderState
import com.geckolib.renderer.base.GeoRenderer
import com.geckolib.renderer.base.RenderPassInfo
import com.geckolib.util.RenderUtil
import com.google.common.reflect.TypeToken
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import de.fuballer.mcendgame.client.component.render.geo_layers.CustomBonesProgressingTextureGeoLayer
import de.fuballer.mcendgame.main.component.entity.custom.entities.beastweaver.BeastweaverEntity
import de.fuballer.mcendgame.main.util.ColorUtil
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.rendertype.RenderType
import net.minecraft.client.renderer.rendertype.RenderTypes
import net.minecraft.resources.Identifier

class BeastweaverEyesGlowGeoLayer<O : Any, R : GeoRenderState>(
    renderer: GeoRenderer<BeastweaverEntity, O, R>,
    bones: List<String>,
    private val alpha: (R) -> Float,
    textures: Map<Float, Identifier>,
    baseTexture: Identifier = textures[textures.keys.first()]!!,
    private val active: (R) -> Boolean = { true },
) : CustomBonesProgressingTextureGeoLayer<BeastweaverEntity, O, R>(renderer, bones, alpha, textures, baseTexture, 0f) {
    companion object {
        private val BEASTWEAVER_EYES_GLOW_ALPHA = DataTicket.create("beastweaver_eyes_glow_alpha", object : TypeToken<Float>() {})
    }

    override fun isActive(renderState: R) = active(renderState)

    override fun addRenderData(animatable: BeastweaverEntity, relatedObject: O?, renderState: R, partialTick: Float) {
        if (!isActive(renderState)) return
        renderState.addGeckolibData(BEASTWEAVER_EYES_GLOW_ALPHA, alpha(renderState))
    }

    override fun getRenderType(renderState: R, texture: Identifier) = RenderTypes.eyes(texture)

    override fun renderBone(renderPassInfo: RenderPassInfo<R>, bone: GeoBone, renderTasks: SubmitNodeCollector) {
        val renderState = renderPassInfo.renderState()
        val boneTexture = getTextureResource(renderState)
        val baseTexture = renderer.getTextureLocation(renderState)
        val boneTextureSize = RenderUtil.getTextureDimensions(boneTexture)
        val baseTextureSize = RenderUtil.getTextureDimensions(baseTexture)
        val widthRatio = baseTextureSize.firstInt() / boneTextureSize.firstInt().toFloat()
        val heightRatio = baseTextureSize.secondInt() / boneTextureSize.secondInt().toFloat()
        val packedLight = renderPassInfo.packedLight()
        val packedOverlay = renderPassInfo.packedOverlay()

        val alpha = renderState.getGeckolibData(BEASTWEAVER_EYES_GLOW_ALPHA) ?: 0F
        val color = ColorUtil.rgbaToInt(255, 255, 255, (alpha * 255).toInt())

        val renderType: RenderType = getRenderType(renderState, boneTexture) ?: return
        renderTasks.submitCustomGeometry(renderPassInfo.poseStack(), renderType) { pose: PoseStack.Pose?, buffer: VertexConsumer? ->
            val poseStack = renderPassInfo.poseStack()

            poseStack.pushPose()
            poseStack.last().set(pose!!)
            bone.translateAwayFromPivotPoint(poseStack)

            for (cube in (bone as CuboidGeoBone).cubes) {
                poseStack.pushPose()
                renderCube(
                    cube,
                    poseStack,
                    buffer!!,
                    packedLight,
                    packedOverlay,
                    color,
                    widthRatio,
                    heightRatio,
                )
                poseStack.popPose()
            }

            poseStack.popPose()
        }
    }
}