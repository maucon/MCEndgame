package de.fuballer.mcendgame.client.component.entity.custom.entities.beastweaver

import com.geckolib.cache.model.GeoBone
import com.geckolib.cache.model.GeoQuad
import com.geckolib.cache.model.cuboid.CuboidGeoBone
import com.geckolib.cache.model.cuboid.GeoCube
import com.geckolib.constant.dataticket.DataTicket
import com.geckolib.renderer.base.GeoRenderState
import com.geckolib.renderer.base.GeoRenderer
import com.geckolib.renderer.base.RenderPassInfo
import com.geckolib.util.RenderUtil
import com.google.common.reflect.TypeToken
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import de.fuballer.mcendgame.client.accessor.BufferBuilderSetVertexElementsAccessor
import de.fuballer.mcendgame.client.component.render.CustomRenderLayers
import de.fuballer.mcendgame.client.component.render.geo_layers.CustomBonesProgressingTextureGeoLayer
import de.fuballer.mcendgame.main.component.entity.custom.entities.beastweaver.BeastweaverEntity
import de.fuballer.mcendgame.main.util.ColorUtil
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.rendertype.RenderType
import net.minecraft.resources.Identifier
import org.joml.Matrix4f
import org.joml.Vector3f
import org.joml.Vector4f

class BeastweaverSpiritAttackGeoLayer<O : Any, R : GeoRenderState>(
    renderer: GeoRenderer<BeastweaverEntity, O, R>,
    bones: List<String>,
    progress: (R) -> Float,
    textures: Map<Float, Identifier>,
    val getGradientOrigin: (BeastweaverEntity, Float) -> Vector3f,
    val getGradientBounds: (Float, Float) -> Pair<Float, Float>,
    baseTexture: Identifier = textures[textures.keys.first()]!!,
    val active: (R) -> Boolean = { true },
) : CustomBonesProgressingTextureGeoLayer<BeastweaverEntity, O, R>(renderer, bones, progress, textures, baseTexture) {
    companion object {
        private val BEASTWEAVER_ATTACK_GRADIENT_ORIGIN = DataTicket.create("beastweaver_attack_gradient_origin", object : TypeToken<Vector3f>() {})
        private val BEASTWEAVER_ATTACK_GRADIENT_BOUNDS = DataTicket.create("beastweaver_attack_gradient_bounds", object : TypeToken<Pair<Float, Float>>() {})
    }

    override fun getRenderType(renderState: R, texture: Identifier): RenderType {
        return CustomRenderLayers.beastweaverAttack(texture)
    }

    override fun isActive(renderState: R) = active(renderState)

    override fun addRenderData(animatable: BeastweaverEntity, relatedObject: O?, renderState: R, partialTick: Float) {
        val gradientOrigin = getGradientOrigin(animatable, partialTick)
        renderState.addGeckolibData(BEASTWEAVER_ATTACK_GRADIENT_ORIGIN, gradientOrigin)

        val scale = animatable.scale
        val bounds = getGradientBounds(progress(renderState), scale)
        renderState.addGeckolibData(BEASTWEAVER_ATTACK_GRADIENT_BOUNDS, bounds)
    }

    override fun renderBone(renderPassInfo: RenderPassInfo<R>, bone: GeoBone, renderTasks: SubmitNodeCollector) {
        val renderState = renderPassInfo.renderState()
        val boneTexture = getTextureResource(renderState)
        val baseTexture = this.renderer.getTextureLocation(renderState)
        val boneTextureSize = RenderUtil.getTextureDimensions(boneTexture)
        val baseTextureSize = RenderUtil.getTextureDimensions(baseTexture)
        val widthRatio = baseTextureSize.firstInt() / boneTextureSize.firstInt().toFloat()
        val heightRatio = baseTextureSize.secondInt() / boneTextureSize.secondInt().toFloat()
        val packedLight = renderPassInfo.packedLight()
        val packedOverlay = renderPassInfo.packedOverlay()

        val renderColor = ColorUtil.rgbaToInt(255, 255, 255, 255)
        val gradientOrigin = renderState.getGeckolibData(BEASTWEAVER_ATTACK_GRADIENT_ORIGIN) ?: Vector3f(0f, 0f, 0f)
        val gradientBounds = renderState.getGeckolibData(BEASTWEAVER_ATTACK_GRADIENT_BOUNDS) ?: Pair(0f, 1f)

        val renderType: RenderType? = getRenderType(renderState, boneTexture)

        if (renderType != null) {
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
                        renderColor,
                        widthRatio,
                        heightRatio,
                        gradientOrigin,
                        gradientBounds,
                    )
                    poseStack.popPose()
                }

                poseStack.popPose()
            }
        }
    }

    fun renderCube(
        cube: GeoCube,
        poseStack: PoseStack,
        vertexConsumer: VertexConsumer,
        packedLight: Int,
        packedOverlay: Int,
        renderColor: Int,
        widthRatio: Float,
        heightRatio: Float,
        gradientOrigin: Vector3f,
        gradientBounds: Pair<Float, Float>,
    ) {
        cube.translateToPivotPoint(poseStack)
        cube.rotate(poseStack)
        cube.translateAwayFromPivotPoint(poseStack)

        val normalisedPoseState = poseStack.last().normal()
        val poseState = Matrix4f(poseStack.last().pose())

        for (quad in cube.quads()) {
            if (quad == null) continue

            val normal = normalisedPoseState.transform(quad.normalVec())

            RenderUtil.fixInvertedFlatCube(cube, normal)
            renderQuad(
                quad,
                poseState,
                normal,
                vertexConsumer,
                packedLight,
                packedOverlay,
                renderColor,
                widthRatio,
                heightRatio,
                gradientOrigin,
                gradientBounds,
            )
        }
    }

    fun renderQuad(
        quad: GeoQuad,
        pose: Matrix4f,
        normal: Vector3f,
        vertexConsumer: VertexConsumer,
        packedLight: Int,
        packedOverlay: Int,
        renderColor: Int,
        widthRatio: Float,
        heightRatio: Float,
        gradientOrigin: Vector3f,
        gradientBounds: Pair<Float, Float>,
    ) {
        val accessor = vertexConsumer as? BufferBuilderSetVertexElementsAccessor ?: return

        for (vertex in quad.vertices()) {
            val vector4f = pose.transform(Vector4f(vertex.posX(), vertex.posY(), vertex.posZ(), 1f))

            accessor.`mcendgame$addVertex`(
                vector4f.x(), vector4f.y(), vector4f.z(),
                renderColor,
                vertex.texU() * widthRatio,
                vertex.texV() * heightRatio,
                packedOverlay,
                packedLight,
                normal.x(), normal.y(), normal.z(),
                gradientOrigin.x, gradientOrigin.y, gradientOrigin.z,
                gradientBounds.first, gradientBounds.second,
            )
        }
    }
}