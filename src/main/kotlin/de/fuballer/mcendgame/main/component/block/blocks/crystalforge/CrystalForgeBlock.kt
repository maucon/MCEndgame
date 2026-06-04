package de.fuballer.mcendgame.main.component.block.blocks.crystalforge

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionResult
import net.minecraft.world.SimpleMenuProvider
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.pathfinder.PathComputationType
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes

class CrystalForgeBlock(
    settings: Properties
) : Block(settings) {
    companion object {
        const val ID = "crystal_forge"

        private val SHAPES_BY_AXIS = Shapes.rotateHorizontalAxis(
            Shapes.or(
                column(12.0, 0.0, 4.0),
                column(8.0, 10.0, 4.0, 5.0),
                column(4.0, 8.0, 5.0, 10.0),
                column(10.0, 16.0, 10.0, 16.0)
            )
        )
    }

    init {
        registerDefaultState(stateDefinition.any().setValue(HorizontalDirectionalBlock.FACING, Direction.NORTH))
    }

    override fun getStateForPlacement(ctx: BlockPlaceContext): BlockState =
        defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, ctx.horizontalDirection.clockWise)

    override fun useWithoutItem(
        state: BlockState,
        world: Level,
        pos: BlockPos,
        player: Player,
        hit: BlockHitResult,
    ): InteractionResult {
        if (world.isClientSide) return InteractionResult.SUCCESS

        val screenHandlerFactory = SimpleMenuProvider(
            { syncId, inventory, _ -> CrystalForgeScreenHandler(syncId, inventory) },
            Component.translatable("${CrystalForgeSettings.CONTAINER_BASE_KEY}title")
        )
        player.openMenu(screenHandlerFactory)

        return InteractionResult.SUCCESS
    }

    override fun getShape(
        state: BlockState,
        world: BlockGetter,
        pos: BlockPos,
        context: CollisionContext,
    ) = SHAPES_BY_AXIS[state.getValue(HorizontalDirectionalBlock.FACING).axis]!!

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(HorizontalDirectionalBlock.FACING)
    }

    override fun isPathfindable(state: BlockState, type: PathComputationType) = false
}