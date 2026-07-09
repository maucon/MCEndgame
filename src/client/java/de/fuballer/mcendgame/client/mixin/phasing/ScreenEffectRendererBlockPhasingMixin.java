package de.fuballer.mcendgame.client.mixin.phasing;

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ScreenEffectRenderer.class)
public class ScreenEffectRendererBlockPhasingMixin {
    @Shadow
    private static @Nullable BlockState getViewBlockingState(Player player) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Redirect(
            method = "submit",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ScreenEffectRenderer;getViewBlockingState(Lnet/minecraft/world/entity/player/Player;)Lnet/minecraft/world/level/block/state/BlockState;")
    )
    private static BlockState doNotRenderInWallOverlay(Player player) {
        if (CustomAttributesExtensions.INSTANCE.hasBlockPhasing(player)) return null;
        return getViewBlockingState(player);
    }
}
