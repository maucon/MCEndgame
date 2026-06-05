package de.fuballer.mcendgame.client.mixin.ghostly_appearance;

import com.geckolib.animatable.GeoAnimatable;
import com.geckolib.renderer.GeoEntityRenderer;
import com.geckolib.renderer.base.GeoRenderState;
import de.fuballer.mcendgame.client.accessor.LivingEntityRenderStateGhostlyAccessor;
import de.fuballer.mcendgame.client.component.render.CustomRenderLayers;
import de.fuballer.mcendgame.client.component.render.ghostly.GhostlySettings;
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GeoEntityRenderer.class)
public class GeoEntityRendererGhostlyMixin<T extends Entity & GeoAnimatable, R extends EntityRenderState & GeoRenderState> {
    @Inject(method = "extractLivingEntityRenderState", at = @At("HEAD"))
    void extractLivingEntityRenderState(
            LivingEntity entity,
            LivingEntityRenderState renderState,
            float partialTick,
            ItemModelResolver itemModelResolver,
            CallbackInfo ci
    ) {
        if (!(renderState instanceof LivingEntityRenderStateGhostlyAccessor accessor)) return;

        var ghostly = CustomAttributesExtensions.INSTANCE.isGhostly(entity);
        accessor.mcendgame$setGhostly(ghostly);
    }

    @Inject(
            method = "getRenderType(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;Lnet/minecraft/resources/Identifier;)Lnet/minecraft/client/renderer/rendertype/RenderType;",
            at = @At("HEAD"),
            cancellable = true
    )
    void modifyRenderLayer(R renderState, Identifier texture, CallbackInfoReturnable<RenderType> cir) {
        if (!(renderState instanceof LivingEntityRenderStateGhostlyAccessor accessor)) return;
        if (!accessor.mcendgame$isGhostly()) return;

        cir.setReturnValue(CustomRenderLayers.INSTANCE.ghostly(texture));
        cir.cancel();
    }

    @ModifyVariable(
            method = "getRenderColor(Lnet/minecraft/world/entity/Entity;Ljava/lang/Void;F)I",
            at = @At("STORE"),
            name = "color"
    )
    int modifyRenderColor(int color, T animatable) {
        if (!(animatable instanceof LivingEntity livingEntity)) return color;
        if (!CustomAttributesExtensions.INSTANCE.isGhostly(livingEntity)) return color;
        return GhostlySettings.INSTANCE.getCOLOR();
    }
}
