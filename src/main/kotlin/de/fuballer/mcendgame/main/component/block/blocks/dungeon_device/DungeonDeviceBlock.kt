package de.fuballer.mcendgame.main.component.block.blocks.dungeon_device

import com.mojang.serialization.MapCodec
import de.maucon.mauconframework.event.EventGateway
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.ItemScatterer
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class DungeonDeviceBlock(
    settings: Settings
) : BlockWithEntity(settings) {
    companion object {
        const val ID = "dungeon_device"
    }

    override fun getCodec(): MapCodec<out BlockWithEntity> = createCodec(::DungeonDeviceBlock)

    override fun onUse(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hit: BlockHitResult): ActionResult {
        if (!world.isClient) {
            val screenHandlerFactory = state.createScreenHandlerFactory(world, pos)
            player.openHandledScreen(screenHandlerFactory)
        }

        return ActionResult.SUCCESS
    }

    override fun onStateReplaced(
        state: BlockState,
        world: World,
        pos: BlockPos,
        newState: BlockState,
        moved: Boolean
    ) {
        if (!state.hasBlockEntity()) return

        val blockEntity = world.getBlockEntity(pos) ?: return
        val dungeonDeviceBlockEntity = blockEntity as? DungeonDeviceBlockEntity ?: return

        ItemScatterer.spawn(world, pos, dungeonDeviceBlockEntity)
        world.updateComparators(pos, this)
        super.onStateReplaced(state, world, pos, newState, moved)
    }

    override fun onBreak(world: World, pos: BlockPos, state: BlockState, player: PlayerEntity): BlockState {
        if (!world.isClient) {
            val blockEntity = world.getBlockEntity(pos)

            blockEntity?.also {
                val event = DungeonDeviceBrokenEvent(blockEntity)
                EventGateway.publish(event)
            }
        }
        return super.onBreak(world, pos, state, player)
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = DungeonDeviceBlockEntity(pos, state)
}