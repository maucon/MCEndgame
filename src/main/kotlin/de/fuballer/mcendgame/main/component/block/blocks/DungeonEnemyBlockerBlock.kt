package de.fuballer.mcendgame.main.component.block.blocks

import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.getLastDamageTime
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.isDungeonBoss
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.isDungeonEnemy
import net.minecraft.block.BarrierBlock
import net.minecraft.block.BlockState
import net.minecraft.block.EntityShapeContext
import net.minecraft.block.ShapeContext
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.function.BooleanBiFunction
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView

class DungeonEnemyBlockerBlock(
    settings: Settings,
) : BarrierBlock(settings) {
    companion object {
        const val ID = "dungeon_enemy_blocker"

        private const val DAMAGE_TAKEN_TIME = 100
    }

    override fun getCollisionShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext,
    ): VoxelShape {
        val entityContext = context as? EntityShapeContext ?: return VoxelShapes.empty()
        val entity = entityContext.entity as? LivingEntity ?: return VoxelShapes.empty()

        if (!entity.isDungeonEnemy() || (entity.isDungeonBoss() && (entity as? MobEntity)?.isAiDisabled != true)) return VoxelShapes.empty()
        if (entity.entityWorld.time - entity.getLastDamageTime() <= DAMAGE_TAKEN_TIME) return VoxelShapes.empty()

        val blockShape = VoxelShapes.fullCube()
        val offsetShape = blockShape.offset(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())
        if (VoxelShapes.matchesAnywhere(offsetShape, VoxelShapes.cuboid(entity.boundingBox), BooleanBiFunction.AND)) return VoxelShapes.empty()

        return blockShape
    }

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape {
        val entityContext = context as? EntityShapeContext ?: return VoxelShapes.empty()
        val player = entityContext.entity as? PlayerEntity ?: return VoxelShapes.empty()
        if (!player.isCreative) return VoxelShapes.empty()
        return VoxelShapes.fullCube()
    }
}