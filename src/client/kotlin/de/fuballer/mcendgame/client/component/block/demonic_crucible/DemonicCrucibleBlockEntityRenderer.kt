package de.fuballer.mcendgame.client.component.block.demonic_crucible

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import de.fuballer.mcendgame.main.component.block.blocks.demonic_crucible.DemonicCrucibleBlockEntity
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.feature.ModelFeatureRenderer
import net.minecraft.client.renderer.state.level.CameraRenderState
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.phys.Vec3
import kotlin.math.sin

class DemonicCrucibleBlockEntityRenderer(
    context: BlockEntityRendererProvider.Context
) : BlockEntityRenderer<DemonicCrucibleBlockEntity, DemonicCrucibleBlockEntityRenderState> {
    override fun createRenderState() = DemonicCrucibleBlockEntityRenderState()

    override fun extractRenderState(
        blockEntity: DemonicCrucibleBlockEntity,
        state: DemonicCrucibleBlockEntityRenderState,
        tickProgress: Float,
        cameraPos: Vec3,
        crumblingOverlay: ModelFeatureRenderer.CrumblingOverlay?
    ) {
        super.extractRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay)

        val resolver = Minecraft.getInstance().itemModelResolver
        resolver.updateForTopItem(state.item, blockEntity.stack, ItemDisplayContext.NONE, null, null, 0)

        state.animationProgressTicks = blockEntity.progressTicks
    }

    override fun submit(
        state: DemonicCrucibleBlockEntityRenderState,
        poseStack: PoseStack,
        submitNodeCollector: SubmitNodeCollector,
        camera: CameraRenderState
    ) {
        if (state.item.isEmpty) return

        poseStack.pushPose()
        poseStack.translate(0.5, 0.5, 0.5)

        val animationTime = state.animationProgressTicks + state.partialTick
        val rotations = animationTimeToRotation(animationTime)
        val bob = animationTimeToBob(animationTime)

        poseStack.translate(0.0, bob, 0.0)
        poseStack.rotateDegrees(Axis.YP, rotations * 360F)
        poseStack.scale(0.5F, 0.5F, 0.5F)
        state.item.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0)

        poseStack.popPose()
    }

    private fun animationTimeToRotation(time: Float) = time * time / 7200f + time / 180f

    private fun animationTimeToBob(time: Float) = sin(time * time / 1600 + time / 20) * 0.025 + 0.015 * (time / DemonicCrucibleBlockEntity.PROGRESS_DURATION)
}