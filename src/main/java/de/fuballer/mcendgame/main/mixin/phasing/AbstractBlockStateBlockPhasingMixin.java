package de.fuballer.mcendgame.main.mixin.phasing;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions;
import de.fuballer.mcendgame.main.component.tags.CustomTags;
import de.fuballer.mcendgame.main.util.extension.WorldExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockBehaviour.BlockStateBase.class)
public class AbstractBlockStateBlockPhasingMixin {
    @Unique
    private static final double MIN_PITCH_FOR_GROUND_PHASING = 85;

    @ModifyReturnValue(
            method = "getCollisionShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;",
            at = @At("RETURN")
    )
    VoxelShape getBlockPhasingCollisionShape(
            VoxelShape original,
            @Local(argsOnly = true) BlockGetter world,
            @Local(argsOnly = true) BlockPos pos,
            @Local(argsOnly = true) CollisionContext context
    ) {
        if (!(context instanceof EntityCollisionContext entityContext)) return original;
        if (!(entityContext.getEntity() instanceof LivingEntity entity)) return original;
        if (WorldExtension.INSTANCE.isDungeonWorld(entity.level())) return original;
        if (!CustomAttributesExtensions.INSTANCE.hasBlockPhasing(entity)) return original;

        var blockState = world.getBlockState(pos);
        if (blockState.is(CustomTags.INSTANCE.getPHASING_BLOCKING())) return original;

        var collisionShape = Shapes.empty();
        if (context.isDescending() && entity.getXRot() >= MIN_PITCH_FOR_GROUND_PHASING) return collisionShape;

        for (AABB box : original.toAabbs()) {
            var boxShape = Shapes.create(box);
            if (!context.isAbove(boxShape, pos, true)) continue;

            collisionShape = Shapes.or(collisionShape, boxShape);
        }
        return collisionShape;
    }
}
