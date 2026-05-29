package de.fuballer.mcendgame.main.mixin.dungeon_rules;

import de.fuballer.mcendgame.main.util.extension.WorldExtension;
import net.minecraft.block.BlockState;
import net.minecraft.block.DecoratedPotBlock;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DecoratedPotBlock.class)
public class DecoratedPotBlockUnbreakableMixin {
    @Inject(
            method = "onProjectileHit",
            at = @At("HEAD"),
            cancellable = true
    )
    void preventBreaking(
            World world,
            BlockState state,
            BlockHitResult hit,
            ProjectileEntity projectile,
            CallbackInfo ci
    ) {
        if (WorldExtension.INSTANCE.isDungeonWorld(world)) ci.cancel();
    }
}
