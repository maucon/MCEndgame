package de.fuballer.mcendgame.client.mixin.ghostly_appearance;

import com.llamalad7.mixinextras.sugar.Local;
import de.fuballer.mcendgame.client.accessor.LivingEntityRenderStateGhostlyAccessor;
import de.fuballer.mcendgame.client.component.render.CustomRenderLayers;
import de.fuballer.mcendgame.client.component.render.ghostly.GhostlySettings;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(RenderLayer.class)
public class RenderLayerGhostlyMixin {
    @ModifyArg(
            method = "renderColoredCutoutModel",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/OrderedSubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;IIILnet/minecraft/client/renderer/texture/TextureAtlasSprite;ILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V"),
            index = 3
    )
    private static <S extends LivingEntityRenderState> RenderType setGhostlyRenderLayer(
            RenderType original,
            @Local(argsOnly = true) Identifier texture,
            @Local(argsOnly = true) S state
    ) {
        if (!(state instanceof LivingEntityRenderStateGhostlyAccessor accessor)) return original;
        if (!accessor.mcendgame$isGhostly()) return original;

        return CustomRenderLayers.INSTANCE.ghostly(texture);
    }

    @ModifyArg(
            method = "renderColoredCutoutModel",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/OrderedSubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;IIILnet/minecraft/client/renderer/texture/TextureAtlasSprite;ILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V"),
            index = 6
    )
    private static <S extends LivingEntityRenderState> int setGhostlyColor(
            int original,
            @Local(argsOnly = true) S state
    ) {
        if (!(state instanceof LivingEntityRenderStateGhostlyAccessor accessor)) return original;
        if (!accessor.mcendgame$isGhostly()) return original;

        return GhostlySettings.INSTANCE.getCOLOR();
    }
}
