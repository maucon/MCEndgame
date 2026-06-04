package de.fuballer.mcendgame.client.mixin.living_entity;

import de.fuballer.mcendgame.client.accessor.LivingEntityRenderStateWebbedAccessor;
import de.fuballer.mcendgame.main.accessor.LivingEntityWebbedAccessor;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererWebbedMixin<T extends LivingEntity, S extends LivingEntityRenderState> {
    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("HEAD"))
    void updateRenderState(T livingEntity, S livingEntityRenderState, float f, CallbackInfo ci) {
        if (!(livingEntity instanceof LivingEntityWebbedAccessor entityAccessor)) return;
        if (!(livingEntityRenderState instanceof LivingEntityRenderStateWebbedAccessor renderStateAccessor)) return;

        renderStateAccessor.mcendgame$setWebbed(entityAccessor.mcendgame$isWebbed());
    }
}
