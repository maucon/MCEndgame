package de.fuballer.mcendgame.client.component.entity.custom.entities.arachne

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import de.fuballer.mcendgame.client.component.entity.custom.data.EntityConnectionPointData
import de.fuballer.mcendgame.client.component.entity.custom.data.MultipleEntityConnectionData
import de.fuballer.mcendgame.main.component.entity.custom.entities.arachne.ArachneEntity
import de.fuballer.mcendgame.main.component.entity.custom.entities.mount.DirectionalMovementEntity
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.culling.Frustum
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.client.renderer.rendertype.RenderTypes
import net.minecraft.client.renderer.state.level.CameraRenderState
import net.minecraft.core.BlockPos
import net.minecraft.resources.Identifier
import net.minecraft.util.LightCoordsUtil
import net.minecraft.util.Mth
import net.minecraft.world.level.LightLayer
import net.minecraft.world.phys.Vec3
import org.joml.Matrix4f

class ArachneRenderer(
    context: EntityRendererProvider.Context,
) : MobRenderer<ArachneEntity, ArachneRenderState, ArachneEntityModel>(
    context,
    ArachneEntityModel(context.bakeLayer(ArachneEntityModel.ARACHNE)),
    0.8F //shadow
) {
    override fun createRenderState(): ArachneRenderState =
        ArachneRenderState()

    override fun getTextureLocation(state: ArachneRenderState): Identifier {
        if (state.isSaddled) return IdentifierUtil.default("textures/entity/arachne/arachne_saddled.png")
        return IdentifierUtil.default("textures/entity/arachne/arachne.png")
    }

    override fun extractRenderState(
        entity: ArachneEntity,
        renderState: ArachneRenderState,
        tickDelta: Float
    ) {
        super.extractRenderState(entity, renderState, tickDelta)

        renderState.idleAnimationState.copyFrom(entity.idleAnimationState)

        renderState.walkAnimationState.copyFrom(entity.walkAnimationState)
        renderState.walkBWAnimationState.copyFrom(entity.walkBWAnimationState)

        renderState.spitAnimationState.copyFrom(entity.spitAnimationState)
        renderState.meleeAttackAnimationState.copyFrom(entity.attackAnimationState)

        renderState.isSaddled = entity.isSaddled

        renderState.moveSpeed = entity.entityData.get(DirectionalMovementEntity.ANIMATION_MOVEMENT_SPEED)

        updateHookedRenderState(entity, renderState, tickDelta)
    }

    private fun updateHookedRenderState(
        entity: ArachneEntity,
        renderState: ArachneRenderState,
        tickDelta: Float
    ) {
        val webHookData = MultipleEntityConnectionData()
        renderState.webHookData = webHookData

        if (entity.hookedEntityIds.isEmpty()) return

        val yaw = entity.getPreciseBodyRotation(tickDelta) * (Math.PI / 180.0).toFloat()
        webHookData.offset = entity.getLeashOffset(tickDelta).yRot(-yaw)
        webHookData.originEntity.pos = entity.getRopeHoldPosition(tickDelta)

        val world = entity.level()

        val blockPos = BlockPos.containing(entity.getEyePosition(tickDelta))
        webHookData.originEntity.blockLight = getBlockLightLevel(entity, blockPos)
        webHookData.originEntity.skyLight = world.getBrightness(LightLayer.SKY, blockPos)

        val hookedEntityDataList = mutableListOf<EntityConnectionPointData>()
        for (hookedEntityId in entity.hookedEntityIds) {
            val hookedEntity = world.getEntity(hookedEntityId) ?: continue

            val hookedEntityData = EntityConnectionPointData()

            hookedEntityData.pos = hookedEntity.getRopeHoldPosition(tickDelta)

            val hookedBlockPos = BlockPos.containing(hookedEntity.getEyePosition(tickDelta))
            hookedEntityData.blockLight = world.getBrightness(LightLayer.BLOCK, hookedBlockPos)
            hookedEntityData.skyLight = world.getBrightness(LightLayer.SKY, hookedBlockPos)

            hookedEntityDataList.add(hookedEntityData)
        }
        webHookData.connectedEntities = hookedEntityDataList
    }

    override fun submit(
        state: ArachneRenderState,
        matrices: PoseStack,
        orderedRenderCommandQueue: SubmitNodeCollector,
        cameraRenderState: CameraRenderState,
    ) {
        super.submit(state, matrices, orderedRenderCommandQueue, cameraRenderState)

        val webHookData = state.webHookData ?: return
        renderWebHook(matrices, orderedRenderCommandQueue, webHookData)
    }

    private fun renderWebHook(
        matrices: PoseStack,
        queue: SubmitNodeCollector,
        webHookData: MultipleEntityConnectionData,
    ) {
        for (hookedData in webHookData.connectedEntities) {
            val hookedOffset = hookedData.pos.subtract(webHookData.originEntity.pos)

            val segmentSize = 0.05f
            val horizontalSizeFactor = 1F / hookedOffset.horizontalDistance() * segmentSize
            val segmentSizeX = (hookedOffset.x * horizontalSizeFactor).toFloat()
            val segmentSizeZ = (hookedOffset.z * horizontalSizeFactor).toFloat()

            matrices.pushPose()
            matrices.translate(webHookData.offset)

            queue.submitCustomGeometry(matrices, RenderTypes.leash()) { entry, vertexConsumer ->
                for (segment in 0..24) {
                    renderWebHookSegment(
                        vertexConsumer,
                        entry.pose(),
                        hookedOffset,
                        hookedData.blockLight,
                        webHookData.originEntity.blockLight,
                        hookedData.skyLight,
                        webHookData.originEntity.skyLight,
                        segmentSize,
                        segmentSizeZ,
                        segmentSizeX,
                        segment,
                        false,
                    )
                }

                for (segment in 24 downTo 0) {
                    renderWebHookSegment(
                        vertexConsumer,
                        entry.pose(),
                        hookedOffset,
                        hookedData.blockLight,
                        webHookData.originEntity.blockLight,
                        hookedData.skyLight,
                        webHookData.originEntity.skyLight,
                        segmentSize,
                        segmentSizeZ,
                        segmentSizeX,
                        segment,
                        true,
                    )
                }
            }

            matrices.popPose()
        }
    }

    private fun renderWebHookSegment(
        vertexConsumer: VertexConsumer,
        matrix: Matrix4f,
        hookedEntityOffset: Vec3, // relative pos
        leashedEntityBlockLight: Int,
        leashHolderBlockLight: Int,
        leashedEntitySkyLight: Int,
        leashHolderSkyLight: Int,
        segmentSizeY: Float,
        segmentSizeX: Float,
        segmentSizeZ: Float,
        segmentIndex: Int,
        rotated: Boolean,
    ) {
        val segmentPercent = segmentIndex.toFloat() / 24.0f
        val blockLight = Mth.lerpInt(segmentPercent, leashedEntityBlockLight, leashHolderBlockLight)
        val skyLight = Mth.lerpInt(segmentPercent, leashedEntitySkyLight, leashHolderSkyLight)
        val light = LightCoordsUtil.pack(blockLight, skyLight)

        val brightnessFactor = if (segmentIndex % 2 == (if (rotated) 1 else 0)) 0.85f else 0.98f
        val red = 0.99f * brightnessFactor
        val green = 0.95f * brightnessFactor
        val blue = 0.87f * brightnessFactor

        val segmentX = (hookedEntityOffset.x * segmentPercent).toFloat()
        val segmentY =
            (if (hookedEntityOffset.y > 0.0f) hookedEntityOffset.y * segmentPercent * segmentPercent else hookedEntityOffset.y - hookedEntityOffset.y * (1.0f - segmentPercent) * (1.0f - segmentPercent)).toFloat()
        val segmentZ = (hookedEntityOffset.z * segmentPercent).toFloat()

        vertexConsumer.addVertex(
            matrix,
            segmentX - segmentSizeX / 2,
            segmentY + if (rotated) segmentSizeY else 0F,
            segmentZ + segmentSizeZ / 2
        ).setColor(red, green, blue, 1.0f).setLight(light)
        vertexConsumer.addVertex(
            matrix,
            segmentX + segmentSizeX / 2,
            segmentY + if (rotated) 0F else segmentSizeY,
            segmentZ - segmentSizeZ / 2
        ).setColor(red, green, blue, 1.0f).setLight(light)
    }

    override fun shouldRender(
        entity: ArachneEntity,
        frustum: Frustum,
        x: Double,
        y: Double,
        z: Double
    ): Boolean {
        if (super.shouldRender(entity, frustum, x, y, z)) return true

        val world = entity.level()
        for (hookedId in entity.hookedEntityIds) {
            val hookedEntity = world.getEntity(hookedId) ?: continue
            if (frustum.isVisible(hookedEntity.boundingBox)) return true
        }

        return false
    }
}