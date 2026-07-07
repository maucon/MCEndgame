package de.fuballer.mcendgame.main.component.block.blocks.crystalforge

import net.minecraft.block.*
import net.minecraft.entity.ai.pathing.NavigationType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.screen.SimpleNamedScreenHandlerFactory
import net.minecraft.state.StateManager
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World

class CrystalForgeBlock(
    settings: Settings
) : Block(settings) {
    companion object {
        const val ID = "crystal_forge"

        val BASE_SHAPE: VoxelShape = createCuboidShape(2.0, 0.0, 2.0, 14.0, 4.0, 14.0)
        val X_STEP_SHAPE: VoxelShape = createCuboidShape(3.0, 4.0, 4.0, 13.0, 5.0, 12.0)
        val X_STEM_SHAPE: VoxelShape = createCuboidShape(4.0, 5.0, 6.0, 12.0, 10.0, 10.0)
        val X_FACE_SHAPE: VoxelShape = createCuboidShape(0.0, 10.0, 3.0, 16.0, 16.0, 13.0)
        val Z_STEP_SHAPE: VoxelShape = createCuboidShape(4.0, 4.0, 3.0, 12.0, 5.0, 13.0)
        val Z_STEM_SHAPE: VoxelShape = createCuboidShape(6.0, 5.0, 4.0, 10.0, 10.0, 12.0)
        val Z_FACE_SHAPE: VoxelShape = createCuboidShape(3.0, 10.0, 0.0, 13.0, 16.0, 16.0)
        val X_AXIS_SHAPE: VoxelShape = VoxelShapes.union(BASE_SHAPE, X_STEP_SHAPE, X_STEM_SHAPE, X_FACE_SHAPE)
        val Z_AXIS_SHAPE: VoxelShape = VoxelShapes.union(BASE_SHAPE, Z_STEP_SHAPE, Z_STEM_SHAPE, Z_FACE_SHAPE)
    }


    init {
        defaultState = stateManager.getDefaultState().with(HorizontalFacingBlock.FACING, Direction.NORTH)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? =
        defaultState.with(HorizontalFacingBlock.FACING, ctx.horizontalPlayerFacing.rotateYClockwise())

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult,
    ): ActionResult {
        if (world.isClient) return ActionResult.SUCCESS

        val screenHandlerFactory = SimpleNamedScreenHandlerFactory(
            { syncId, inventory, _ -> CrystalForgeScreenHandler(syncId, inventory) },
            Text.translatable("${CrystalForgeSettings.CONTAINER_BASE_KEY}title")
        )
        player.openHandledScreen(screenHandlerFactory)

        return ActionResult.SUCCESS
    }

    override fun getOutlineShape(state: BlockState, world: BlockView, pos: BlockPos, context: ShapeContext): VoxelShape {
        val direction = state.get(AnvilBlock.FACING) as Direction
        return if (direction.axis == Direction.Axis.X) X_AXIS_SHAPE else Z_AXIS_SHAPE
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(HorizontalFacingBlock.FACING)
    }

    override fun canPathfindThrough(state: BlockState, type: NavigationType) = false
}