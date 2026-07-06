package de.fuballer.mcendgame.main.component.block.blocks.crystalforge

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.HorizontalFacingBlock
import net.minecraft.block.ShapeContext
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
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World

class CrystalForgeBlock(
    settings: Settings
) : Block(settings) {
    companion object {
        const val ID = "crystal_forge"

        private val SHAPES_BY_AXIS = VoxelShapes.createHorizontalAxisShapeMap(
            VoxelShapes.union(
                createColumnShape(12.0, 0.0, 4.0),
                createColumnShape(8.0, 10.0, 4.0, 5.0),
                createColumnShape(4.0, 8.0, 5.0, 10.0),
                createColumnShape(10.0, 16.0, 10.0, 16.0)
            )
        )
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

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext,
    ): K = SHAPES_BY_AXIS[(state.get(HorizontalFacingBlock.FACING) as Direction).axis]!!

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(HorizontalFacingBlock.FACING)
    }

    override fun canPathfindThrough(state: BlockState, type: NavigationType) = false
}