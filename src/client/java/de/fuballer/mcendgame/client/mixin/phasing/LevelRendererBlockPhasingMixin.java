package de.fuballer.mcendgame.client.mixin.phasing;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import de.fuballer.mcendgame.main.util.extension.EntityExtension;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererBlockPhasingMixin {
    @Inject(
            method = "addSkyPass",
            at = @At("HEAD"),
            cancellable = true)
    void renderSky(
            FrameGraphBuilder frameGraphBuilder,
            Camera camera,
            GpuBufferSlice fogBuffer,
            CallbackInfo ci
    ) {
        var entity = camera.entity();
        if (!EntityExtension.INSTANCE.isBlockPhasingAtEyes(entity)) return;
        ci.cancel();
    }
}
