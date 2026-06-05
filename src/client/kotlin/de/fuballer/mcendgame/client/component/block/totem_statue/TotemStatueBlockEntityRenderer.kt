package de.fuballer.mcendgame.client.component.block.totem_statue

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import de.fuballer.mcendgame.main.component.block.blocks.totem_statue.TotemStatueBlockEntity
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.feature.ModelFeatureRenderer
import net.minecraft.client.renderer.rendertype.RenderTypes
import net.minecraft.client.renderer.state.level.CameraRenderState
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.RotationSegment
import net.minecraft.world.phys.Vec3
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.sin

private val TEXTURE = IdentifierUtil.default("textures/block/totem_statue.png")
private val ACTIVE_TEXTURE = IdentifierUtil.default("textures/block/totem_statue_active.png")

private const val HOVER_START_TICKS = 50
private const val HOVER_HEIGHT = 0.3
private const val HOVER_CYCLE_TICKS = 50
private const val HOVER_HEIGHT_DIFFERENCE = 0.1

private const val SLOPE_AT_CYCLE_START = 1.0 / HOVER_CYCLE_TICKS

// used for start of hover animation to smoothly transition into sin wave [f(x)=px^3+qx^2]
private val X3_COEFFICIENT = (HOVER_START_TICKS * SLOPE_AT_CYCLE_START - 2 * HOVER_HEIGHT) / HOVER_START_TICKS.toDouble().pow(3) //p
private val X2_COEFFICIENT = (3 * HOVER_HEIGHT - HOVER_START_TICKS * SLOPE_AT_CYCLE_START) / HOVER_START_TICKS.toDouble().pow(2) //q

private const val DEG_ROTATION_PER_TICK = 5F

class TotemStatueBlockEntityRenderer(
    context: BlockEntityRendererProvider.Context,
) : BlockEntityRenderer<TotemStatueBlockEntity, TotemStatueBlockEntityRenderState> {
    private val model: TotemStatueBlockEntityModel

    init {
        val loadedModels = context.entityModelSet
        model = TotemStatueBlockEntityModel(loadedModels.bakeLayer(TotemStatueBlockEntityModel.MODEL_LAYER))
    }

    private fun getHoverOffset(ticks: Float): Double {
        if (ticks < 0) return 0.0

        if (ticks < HOVER_START_TICKS) return X3_COEFFICIENT * ticks.toDouble().pow(3) + X2_COEFFICIENT * ticks.toDouble().pow(2)

        val waveTicks = ticks - HOVER_START_TICKS
        val waveProgress = waveTicks.toDouble() / HOVER_CYCLE_TICKS
        val waveOffset = HOVER_HEIGHT_DIFFERENCE * sin(2 * PI * waveProgress)

        return HOVER_HEIGHT + waveOffset
    }

    private fun getHoverRotation(ticks: Float): Float {
        val deg = ticks * DEG_ROTATION_PER_TICK
        val slowStartFactor = 1 - exp(-ticks * 0.01F)
        return deg * slowStartFactor
    }

    override fun createRenderState(): TotemStatueBlockEntityRenderState = TotemStatueBlockEntityRenderState()

    override fun extractRenderState(
        blockEntity: TotemStatueBlockEntity,
        state: TotemStatueBlockEntityRenderState,
        tickProgress: Float,
        cameraPos: Vec3,
        crumblingOverlay: ModelFeatureRenderer.CrumblingOverlay?
    ) {
        super.extractRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay)

        state.rotation = blockEntity.blockState.getValue(BlockStateProperties.ROTATION_16)
        state.activeTicks = blockEntity.getActiveTicks()
    }

    override fun submit(
        state: TotemStatueBlockEntityRenderState,
        matrices: PoseStack,
        queue: SubmitNodeCollector,
        cameraState: CameraRenderState,
    ) {
        matrices.pushPose()
        matrices.translate(0.5F, 0.0F, 0.5F)
        matrices.scale(-1.0F, -1.0F, 1.0F)

        val rotation = state.rotation
        val rotationDeg = RotationSegment.convertToDegrees(rotation)
        matrices.mulPose(Axis.YP.rotationDegrees(rotationDeg))

        val activeTicks = state.activeTicks
        if (activeTicks > 0) {
            val preciseTick = activeTicks + Minecraft.getInstance().deltaTracker.getGameTimeDeltaPartialTick(false)

            val hoverOffset = getHoverOffset(preciseTick)
            matrices.translate(0.0, -hoverOffset, 0.0)

            val hoverRot = getHoverRotation(preciseTick)
            matrices.mulPose(Axis.YP.rotationDegrees(hoverRot))
        }

        val modelState = TotemStatueBlockEntityModel.TotemStatueModelState()
        val texture = if (activeTicks >= 0) ACTIVE_TEXTURE else TEXTURE
        val renderLayer = RenderTypes.entityCutout(texture)
        queue.submitModel(
            model,
            modelState,
            matrices,
            renderLayer,
            state.lightCoords,
            OverlayTexture.NO_OVERLAY,
            -1,
            null,
            0,
            state.breakProgress
        )

        matrices.popPose()
    }
}