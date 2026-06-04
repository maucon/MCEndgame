package de.fuballer.mcendgame.client.mixin.living_entity.isolated;

import de.fuballer.mcendgame.client.accessor.LivingEntityRenderStateIsolatedAccessor;
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributeUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererIsolatedMixin<T extends LivingEntity, S extends LivingEntityRenderState> {
    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("HEAD"))
    void updateRenderState(T livingEntity, S livingEntityRenderState, float f, CallbackInfo ci) {
        if (!(livingEntity instanceof Mob || livingEntity instanceof Avatar)) return;
        if (!(livingEntityRenderState instanceof LivingEntityRenderStateIsolatedAccessor renderStateAccessor)) return;

        var player = Minecraft.getInstance().player;
        var isolated = player != null && CustomAttributeUtil.INSTANCE.isIsolated(livingEntity, player);
        renderStateAccessor.mcendgame$setIsolated(isolated);
    }
}
