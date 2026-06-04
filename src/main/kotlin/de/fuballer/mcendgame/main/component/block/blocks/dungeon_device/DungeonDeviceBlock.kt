package de.fuballer.mcendgame.main.component.block.blocks.dungeon_device

import com.mojang.serialization.MapCodec
import de.maucon.mauconframework.event.EventGateway
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.Containers
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult

class DungeonDeviceBlock(
    settings: Properties
) : BaseEntityBlock(settings) {
    companion object {
        const val ID = "dungeon_device"
    }

    override fun codec(): MapCodec<out BaseEntityBlock> = simpleCodec(::DungeonDeviceBlock)

    override fun useWithoutItem(state: BlockState, world: Level, pos: BlockPos, player: Player, hit: BlockHitResult): InteractionResult {
        if (!world.isClientSide) {
            val screenHandlerFactory = state.getMenuProvider(world, pos)
            player.openMenu(screenHandlerFactory)
        }

        return InteractionResult.SUCCESS
    }

    override fun affectNeighborsAfterRemoval(
        state: BlockState,
        world: ServerLevel,
        pos: BlockPos,
        moved: Boolean
    ) {
        if (!state.hasBlockEntity()) return

        val blockEntity = world.getBlockEntity(pos) ?: return
        val dungeonDeviceBlockEntity = blockEntity as? DungeonDeviceBlockEntity ?: return

        Containers.dropContents(world, pos, dungeonDeviceBlockEntity)
        world.updateNeighbourForOutputSignal(pos, this)
        super.affectNeighborsAfterRemoval(state, world, pos, moved)
    }

    override fun playerWillDestroy(world: Level, pos: BlockPos, state: BlockState, player: Player): BlockState {
        if (!world.isClientSide) {
            val blockEntity = world.getBlockEntity(pos)

            blockEntity?.also {
                val event = DungeonDeviceBrokenEvent(blockEntity)
                EventGateway.publish(event)
            }
        }
        return super.playerWillDestroy(world, pos, state, player)
    }

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = DungeonDeviceBlockEntity(pos, state)
}