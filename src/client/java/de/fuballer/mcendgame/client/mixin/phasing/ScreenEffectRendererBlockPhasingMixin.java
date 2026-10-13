package de.fuballer.mcendgame.client.mixin.phasing;

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.extract.LevelExtractor;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LevelExtractor.class)
public class ScreenEffectRendererBlockPhasingMixin {
    @Shadow
    private static @Nullable BlockState getViewBlockingState(LocalPlayer player, Frustum frustum) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Redirect(
            method = "extractPlayerState",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/extract/LevelExtractor;getViewBlockingState(Lnet/minecraft/client/player/LocalPlayer;Lnet/minecraft/client/renderer/culling/Frustum;)Lnet/minecraft/world/level/block/state/BlockState;")
    )
    private static BlockState doNotRenderInWallOverlay(LocalPlayer player, Frustum frustum) {
        if (CustomAttributesExtensions.INSTANCE.hasBlockPhasing(player)) return null;
        return getViewBlockingState(player, frustum);
    }
}
