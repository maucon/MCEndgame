package de.fuballer.mcendgame.main.mixin.status_effect;

import de.fuballer.mcendgame.main.accessor.LivingEntityWitherDamageImmunityAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.WitherMobEffect;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WitherMobEffect.class)
public class WitherMobEffectMixin {
    @Inject(method = "applyEffectTick", at = @At("HEAD"), cancellable = true)
    void applyUpdateEffect(
            ServerLevel world,
            LivingEntity entity,
            int amplifier,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (((LivingEntityWitherDamageImmunityAccessor) entity).mcendgame$isImmuneToWitherDamage()) cir.setReturnValue(true);
    }
}
