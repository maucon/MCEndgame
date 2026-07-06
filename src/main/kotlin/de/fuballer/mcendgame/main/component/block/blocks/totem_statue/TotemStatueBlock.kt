package de.fuballer.mcendgame.main.component.block.blocks.totem_statue

import com.mojang.serialization.MapCodec
import de.fuballer.mcendgame.main.component.block.CustomBlockEntityTypes
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.ai.pathing.NavigationType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RotationPropertyHelper
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World

class TotemStatueBlock(
    settings: Settings
) : BlockWithEntity(settings) {
    companion object {
        const val ID = "totem_statue"

        private val SHAPE = createCuboidShape(4.0, 0.0, 4.0, 12.0, 15.0, 12.0) // TODO 1.21.1
    }

    init {
        defaultState = stateManager.getDefaultState().with(Properties.ROTATION, 0)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState =
        defaultState.with(Properties.ROTATION, RotationPropertyHelper.fromYaw(ctx.playerYaw))

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult,
    ): ActionResult {
        if (world.isClient) return ActionResult.SUCCESS

        val blockEntity = world.getBlockEntity(pos) as? TotemStatueBlockEntity ?: return ActionResult.SUCCESS
        blockEntity.tryActivate(player)

        return ActionResult.SUCCESS
    }

    override fun canPathfindThrough(state: BlockState, type: NavigationType) = false

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext,
    ): VoxelShape = SHAPE

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(Properties.ROTATION)
    }

    override fun getCodec(): MapCodec<out BlockWithEntity> = createCodec(::TotemStatueBlock)

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = TotemStatueBlockEntity(pos, state)

    override fun getRenderType(state: BlockState) = BlockRenderType.INVISIBLE

    override fun <T : BlockEntity> getTicker(world: World, state: BlockState, type: BlockEntityType<T>): BlockEntityTicker<T>? {
        return validateTicker(type, CustomBlockEntityTypes.TOTEM_STATUE) { worldx, pos, state, blockEntity ->
            TotemStatueBlockEntity.tick(worldx, pos, state, blockEntity)
        }
    }
}