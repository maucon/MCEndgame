package de.fuballer.mcendgame.client.mixin.phasing;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import de.fuballer.mcendgame.main.util.extension.EntityExtension;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererBlockPhasingMixin {
//    @Inject( FIXME
//            method = "addSkyPass",
//            at = @At("HEAD"),
//            cancellable = true
//    )
//    void renderSky(
//            FrameGraphBuilder frame,
//            CameraRenderState cameraState,
//            GpuBufferSlice skyFog,
//            CallbackInfo ci
//    ) {
//        var entity = camera.entity();
//        if (!EntityExtension.INSTANCE.isBlockPhasingAtEyes(entity)) return;
//        ci.cancel();
//    }
}
