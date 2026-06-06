package de.fuballer.mcendgame.client.mixin.ghostly_appearance;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import de.fuballer.mcendgame.client.accessor.LivingEntityRenderStateGhostlyAccessor;
import de.fuballer.mcendgame.client.component.render.CustomRenderLayers;
import de.fuballer.mcendgame.client.component.render.ghostly.GhostlySettings;
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererGhostlyMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> {
    @Shadow
    public abstract Identifier getTextureLocation(S state);

    @Inject(
            method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V",
            at = @At("TAIL")
    )
    public void updateRenderState(
            LivingEntity entity,
            LivingEntityRenderState state,
            float partialTicks,
            CallbackInfo ci
    ) {
        if (!(state instanceof LivingEntityRenderStateGhostlyAccessor accessor)) return;

        var ghostly = CustomAttributesExtensions.INSTANCE.isGhostly(entity);
        accessor.mcendgame$setGhostly(ghostly);
    }

    @ModifyReturnValue(
            method = "getRenderType",
            at = @At("RETURN")
    )
    private RenderType modifyRenderLayer(@Nullable RenderType original, S state) {
        if (!(state instanceof LivingEntityRenderStateGhostlyAccessor accessor)) return original;
        if (!accessor.mcendgame$isGhostly()) return original;

        return CustomRenderLayers.INSTANCE.ghostly(getTextureLocation(state));
    }

    @ModifyVariable(
            method = "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
            at = @At(value = "STORE"),
            name = "tintedColor"
    )
    private int modifyColor(int tintedColor, S state) {
        if (!(state instanceof LivingEntityRenderStateGhostlyAccessor accessor)) return tintedColor;
        if (!accessor.mcendgame$isGhostly()) return tintedColor;

        return GhostlySettings.INSTANCE.getCOLOR();
    }
}
