package de.fuballer.mcendgame.client.mixin.phasing;

import de.fuballer.mcendgame.main.util.extension.EntityExtension;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.FogRenderer;
import net.minecraft.world.entity.Entity;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FogRenderer.class)
public abstract class BackgroundRendererBlockPhasingMixin {
    @Unique
    private static final Vector4f PHASING_FOG_COLOR = new Vector4f(0.2F, 0.7F, 0.6F, 1F);

    @Inject(
            method = "computeFogColor",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getFogColor(
            Camera camera,
            float partialTicks,
            ClientLevel level,
            int renderDistance,
            float darkenWorldAmount,
            Vector4f dest,
            CallbackInfo ci
    ) {
        Entity entity = camera.entity();
        if (!EntityExtension.INSTANCE.isBlockPhasingAtEyes(entity)) return;
        dest.set(PHASING_FOG_COLOR);
        ci.cancel();
    }

    @Inject(
            method = "setupFog",
            at = @At(value = "RETURN"),
            cancellable = true
    )
    void applyBlockPhasingFog(
            Camera camera,
            int renderDistanceInChunks,
            DeltaTracker deltaTracker,
            float darkenWorldAmount,
            ClientLevel level,
            CallbackInfoReturnable<FogData> cir
    ) {
        Entity entity = camera.entity();
        if (entity == null || !EntityExtension.INSTANCE.isBlockPhasingAtEyes(entity)) {
            return;
        }

        var fogData = new FogData();
        fogData.color = PHASING_FOG_COLOR;
        fogData.environmentalStart = 0F;
        fogData.environmentalEnd = 4F;
        fogData.renderDistanceStart = 0F;
        fogData.renderDistanceEnd = 4F;
        fogData.skyEnd = 4F;
        fogData.cloudEnd = 4F;

        cir.setReturnValue(fogData);
    }
}
