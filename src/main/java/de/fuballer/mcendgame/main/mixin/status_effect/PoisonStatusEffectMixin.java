package de.fuballer.mcendgame.main.mixin.status_effect;

import de.fuballer.mcendgame.main.accessor.LivingEntityPoisonDamageImmunityAccessor;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net/minecraft/entity/effect/PoisonStatusEffect")
public class PoisonStatusEffectMixin {
    @Inject(method = "applyUpdateEffect", at = @At("HEAD"), cancellable = true)
    void applyUpdateEffect(
            LivingEntity entity,
            int amplifier,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (((LivingEntityPoisonDamageImmunityAccessor) entity).mcendgame$isImmuneToPoisonDamage()) cir.setReturnValue(true);
    }
}
