package de.fuballer.mcendgame.client.mixin.living_entity;

import de.fuballer.mcendgame.client.accessor.LivingEntityLowHealthTicksAccessor;
import de.fuballer.mcendgame.client.accessor.LivingEntityRenderStateAccessor;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {
    @Inject(at = @At("TAIL"), method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V")
    public void updateRenderState(
            LivingEntity entity,
            LivingEntityRenderState state,
            float partialTicks,
            CallbackInfo ci
    ) {
        if (!(state instanceof LivingEntityRenderStateAccessor livingEntityRenderStateAccessor)) return;

        livingEntityRenderStateAccessor.mcendgame$setHealth(entity.getHealth());
        livingEntityRenderStateAccessor.mcendgame$setMaxHealth(entity.getMaxHealth());

        if (entity instanceof LivingEntityLowHealthTicksAccessor livingEntityLowHealthTicksAccessor) {
            livingEntityRenderStateAccessor.mcendgame$setLowHealthTicks20(livingEntityLowHealthTicksAccessor.mcendgame$getLowHealthTicks20());
        }
    }
}
