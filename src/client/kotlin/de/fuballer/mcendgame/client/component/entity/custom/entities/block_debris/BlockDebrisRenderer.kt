package de.fuballer.mcendgame.client.component.entity.custom.entities.block_debris

import com.mojang.blaze3d.vertex.PoseStack
import de.fuballer.mcendgame.main.component.entity.custom.entities.block_debris.BlockDebrisEntity
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.culling.Frustum
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.state.level.CameraRenderState
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.RenderShape

class BlockDebrisRenderer(
    context: EntityRendererProvider.Context
) : EntityRenderer<BlockDebrisEntity, BlockDebrisRenderState>(context) {
    init {
        shadowRadius = 0.5f
    }

    override fun shouldRender(
        entity: BlockDebrisEntity,
        culler: Frustum,
        camX: Double,
        camY: Double,
        camZ: Double,
    ): Boolean {
        if (!super.shouldRender(entity, culler, camX, camY, camZ)) return false
        return entity.getBlockState() != entity.level().getBlockState(entity.blockPosition())
    }

    override fun submit(
        state: BlockDebrisRenderState,
        poseStack: PoseStack,
        submitNodeCollector: SubmitNodeCollector,
        camera: CameraRenderState,
    ) {
        val blockState = state.movingBlockRenderState.blockState
        if (blockState.renderShape == RenderShape.MODEL) {
            poseStack.pushPose()
            poseStack.translate(-0.5, 0.0, -0.5)
            submitNodeCollector.submitMovingBlock(poseStack, state.movingBlockRenderState)
            poseStack.popPose()
            super.submit(state, poseStack, submitNodeCollector, camera)
        }
    }

    override fun createRenderState(): BlockDebrisRenderState {
        return BlockDebrisRenderState()
    }

    override fun extractRenderState(entity: BlockDebrisEntity, state: BlockDebrisRenderState, partialTicks: Float) {
        super.extractRenderState(entity, state, partialTicks)

        val pos = BlockPos.containing(entity.x, entity.boundingBox.maxY, entity.z)

        state.movingBlockRenderState.randomSeedPos = entity.getStartPos()
        state.movingBlockRenderState.blockPos = pos
        state.movingBlockRenderState.blockState = entity.getBlockState()

        val clientLevel = entity.level() as? ClientLevel ?: return
        state.movingBlockRenderState.biome = clientLevel.getBiome(pos)
        state.movingBlockRenderState.cardinalLighting = clientLevel.cardinalLighting()
        state.movingBlockRenderState.lightEngine = clientLevel.lightEngine
    }
}
