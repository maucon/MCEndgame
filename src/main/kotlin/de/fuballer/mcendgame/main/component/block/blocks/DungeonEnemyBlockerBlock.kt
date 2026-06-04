package de.fuballer.mcendgame.main.component.block.blocks

import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.getLastDamageTime
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.isDungeonBoss
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.isDungeonEnemy
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.BarrierBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.*

class DungeonEnemyBlockerBlock(
    settings: Properties,
) : BarrierBlock(settings) {
    companion object {
        const val ID = "dungeon_enemy_blocker"

        private const val DAMAGE_TAKEN_TIME = 100
    }

    override fun getCollisionShape(
        state: BlockState,
        world: BlockGetter,
        pos: BlockPos,
        context: CollisionContext,
    ): VoxelShape {
        val entityContext = context as? EntityCollisionContext ?: return Shapes.empty()
        val entity = entityContext.entity as? LivingEntity ?: return Shapes.empty()

        if (!entity.isDungeonEnemy() || (entity.isDungeonBoss() && (entity as? Mob)?.isNoAi != true)) return Shapes.empty()
        if (entity.level().gameTime - entity.getLastDamageTime() <= DAMAGE_TAKEN_TIME) return Shapes.empty()

        val blockShape = Shapes.block()
        val offsetShape = blockShape.move(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())
        if (Shapes.joinIsNotEmpty(offsetShape, Shapes.create(entity.boundingBox), BooleanOp.AND)) return Shapes.empty()

        return blockShape
    }

    override fun getShape(
        state: BlockState,
        world: BlockGetter,
        pos: BlockPos,
        context: CollisionContext
    ): VoxelShape {
        val entityContext = context as? EntityCollisionContext ?: return Shapes.empty()
        val player = entityContext.entity as? Player ?: return Shapes.empty()
        if (!player.isCreative) return Shapes.empty()
        return Shapes.block()
    }
}