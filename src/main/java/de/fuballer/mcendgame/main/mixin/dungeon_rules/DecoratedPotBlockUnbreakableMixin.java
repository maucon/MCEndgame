package de.fuballer.mcendgame.main.mixin.dungeon_rules;

import de.fuballer.mcendgame.main.util.extension.WorldExtension;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DecoratedPotBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
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
            Level world,
            BlockState state,
            BlockHitResult hit,
            Projectile projectile,
            CallbackInfo ci
    ) {
        if (WorldExtension.INSTANCE.isDungeonWorld(world)) ci.cancel();
    }
}
