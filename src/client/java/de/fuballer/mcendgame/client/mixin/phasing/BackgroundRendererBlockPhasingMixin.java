package de.fuballer.mcendgame.client.mixin.phasing;

import com.llamalad7.mixinextras.sugar.Local;
import de.fuballer.mcendgame.main.util.extension.EntityExtension;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogRenderer;
import net.minecraft.world.entity.Entity;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.nio.ByteBuffer;

@Mixin(FogRenderer.class)
public abstract class BackgroundRendererBlockPhasingMixin {
    @Shadow
    protected abstract void updateBuffer(ByteBuffer buffer, int bufPos, Vector4f fogColor, float environmentalStart, float environmentalEnd, float renderDistanceStart, float renderDistanceEnd, float skyEnd, float cloudEnd);

    @Unique
    private static final Vector4f PHASING_FOG_COLOR = new Vector4f(0.2F, 0.7F, 0.6F, 1F);

    @Inject(
            method = "computeFogColor",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getFogColor(
            Camera camera,
            float tickProgress,
            ClientLevel world,
            int clampedViewDistance,
            float skyDarkness,
            CallbackInfoReturnable<Vector4f> cir
    ) {
        Entity entity = camera.entity();
        if (!EntityExtension.INSTANCE.isBlockPhasingAtEyes(entity)) return;
        cir.setReturnValue(PHASING_FOG_COLOR);
    }

    @Redirect(
            method = "setupFog(Lnet/minecraft/client/Camera;ILnet/minecraft/client/DeltaTracker;FLnet/minecraft/client/multiplayer/ClientLevel;)Lorg/joml/Vector4f;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/fog/FogRenderer;updateBuffer(Ljava/nio/ByteBuffer;ILorg/joml/Vector4f;FFFFFF)V")
    )
    void applyBlockPhasingFog(
            FogRenderer instance,
            ByteBuffer buffer,
            int bufPos,
            Vector4f fogColor,
            float environmentalStart,
            float environmentalEnd,
            float renderDistanceStart,
            float renderDistanceEnd,
            float skyEnd,
            float cloudEnd,
            @Local Entity entity
    ) {
        if (entity == null || !EntityExtension.INSTANCE.isBlockPhasingAtEyes(entity)) {
            updateBuffer(buffer, bufPos, fogColor, environmentalStart, environmentalEnd, renderDistanceStart, renderDistanceEnd, skyEnd, cloudEnd);
        } else {
            updateBuffer(buffer, bufPos, fogColor, 0F, 4F, 0F, 4F, 4F, 4F);
        }
    }
}
