package de.fuballer.mcendgame.main.mixin.status_effect;

import de.fuballer.mcendgame.main.accessor.LivingEntityWitherDamageImmunityAccessor;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net/minecraft/entity/effect/WitherStatusEffect")
public class WitherStatusEffectMixin {
    @Inject(method = "applyUpdateEffect", at = @At("HEAD"), cancellable = true)
    void applyUpdateEffect(
            LivingEntity entity,
            int amplifier,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (((LivingEntityWitherDamageImmunityAccessor) entity).mcendgame$isImmuneToWitherDamage()) cir.setReturnValue(true);
    }
}
