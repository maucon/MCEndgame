package de.fuballer.mcendgame.main.mixin.status_effect;

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net.minecraft.world.effect.HealOrHarmMobEffect")
public class ApplyHealingFactorHealOrHarmMobEffectMixin {
    @Redirect(
            method = "applyInstantenousEffect",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;heal(F)V")
    )
    void modifyHeal(
            LivingEntity instance,
            float originalAmount,
            ServerLevel world,
            Entity effectEntity,
            Entity attacker,
            LivingEntity target,
            int amplifier,
            double proximity
    ) {
        if (!(attacker instanceof LivingEntity livingAttacker)) {
            target.heal(originalAmount);
            return;
        }

        var healingFactor = CustomAttributesExtensions.INSTANCE.getHealingFactor(livingAttacker);
        target.heal(originalAmount * (float) healingFactor);
    }
}
