package de.fuballer.mcendgame.main.component.block.blocks.demonic_crucible

import de.fuballer.mcendgame.main.component.block.CustomBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.util.Util
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.BooleanOp
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes

class DemonicCrucibleBlock(
    settings: Properties
) : BaseEntityBlock(settings) {
    companion object {
        const val ID = "demonic_crucible"

        private val SHAPE = Util.make {
            Shapes.or(
                box(0.0, 0.0, 0.0, 3.0, 16.0, 3.0),
                box(13.0, 0.0, 0.0, 16.0, 16.0, 3.0),
                box(0.0, 0.0, 13.0, 3.0, 16.0, 16.0),
                box(13.0, 0.0, 13.0, 16.0, 16.0, 16.0),
                Shapes.join(
                    box(1.0, 1.0, 1.0, 15.0, 15.0, 15.0),
                    Shapes.or(
                        box(3.0, 3.0, 1.0, 13.0, 13.0, 15.0),
                        box(1.0, 3.0, 3.0, 15.0, 13.0, 13.0),
                        box(3.0, 1.0, 3.0, 13.0, 15.0, 13.0),
                    ),
                    BooleanOp.ONLY_FIRST,
                ),
                box(3.0, 3.0, 3.0, 13.0, 13.0, 13.0),
            )
        }
    }

    override fun useWithoutItem(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hit: BlockHitResult,
    ): InteractionResult {
        val crucible = level.getBlockEntity(pos) as? DemonicCrucibleBlockEntity ?: return InteractionResult.TRY_WITH_EMPTY_HAND
        if (level !is ServerLevel) return InteractionResult.CONSUME

        crucible.setStack(ItemStack.EMPTY, player, level)
        return InteractionResult.SUCCESS_SERVER
    }

    override fun useItemOn(
        itemStack: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult,
    ): InteractionResult {
        val crucible = level.getBlockEntity(pos) as? DemonicCrucibleBlockEntity ?: return InteractionResult.TRY_WITH_EMPTY_HAND
        if (level !is ServerLevel) return InteractionResult.CONSUME

        crucible.setStack(itemStack, player, level)
        return InteractionResult.SUCCESS_SERVER
    }

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = DemonicCrucibleBlockEntity(pos, state)

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext) = SHAPE

    override fun <T : BlockEntity> getTicker(
        world: Level,
        state: BlockState,
        type: BlockEntityType<T>,
    ): BlockEntityTicker<T>? {
        return createTickerHelper(type, CustomBlockEntityTypes.DEMONIC_CRUCIBLE) { level, pos, _, crucible ->
            crucible.tick(level, pos)
        }
    }
}