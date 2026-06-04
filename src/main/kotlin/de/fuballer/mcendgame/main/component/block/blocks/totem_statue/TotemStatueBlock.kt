package de.fuballer.mcendgame.main.component.block.blocks.totem_statue

import com.mojang.serialization.MapCodec
import de.fuballer.mcendgame.main.component.block.CustomBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.RotationSegment
import net.minecraft.world.level.pathfinder.PathComputationType
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape

class TotemStatueBlock(
    settings: Properties
) : BaseEntityBlock(settings) {
    companion object {
        const val ID = "totem_statue"

        private val SHAPE = column(8.0, 0.0, 15.0)
    }

    init {
        registerDefaultState(stateDefinition.any().setValue(BlockStateProperties.ROTATION_16, 0))
    }

    override fun getStateForPlacement(ctx: BlockPlaceContext): BlockState =
        defaultBlockState().setValue(BlockStateProperties.ROTATION_16, RotationSegment.convertToSegment(ctx.rotation))

    override fun useWithoutItem(
        state: BlockState,
        world: Level,
        pos: BlockPos,
        player: Player,
        hit: BlockHitResult,
    ): InteractionResult {
        if (world.isClientSide) return InteractionResult.SUCCESS

        val blockEntity = world.getBlockEntity(pos) as? TotemStatueBlockEntity ?: return InteractionResult.SUCCESS
        blockEntity.tryActivate(player)

        return InteractionResult.SUCCESS
    }

    override fun isPathfindable(state: BlockState, type: PathComputationType) = false

    override fun getShape(
        state: BlockState,
        world: BlockGetter,
        pos: BlockPos,
        context: CollisionContext,
    ): VoxelShape = SHAPE

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(BlockStateProperties.ROTATION_16)
    }

    override fun codec(): MapCodec<out BaseEntityBlock> = simpleCodec(::TotemStatueBlock)

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = TotemStatueBlockEntity(pos, state)

    override fun getRenderShape(state: BlockState) = RenderShape.INVISIBLE

    override fun <T : BlockEntity> getTicker(world: Level, state: BlockState, type: BlockEntityType<T>): BlockEntityTicker<T>? {
        return createTickerHelper(type, CustomBlockEntityTypes.TOTEM_STATUE) { worldx, _, _, blockEntity ->
            TotemStatueBlockEntity.tick(worldx, blockEntity)
        }
    }
}