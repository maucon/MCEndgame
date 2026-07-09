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
            method = "applyInstantaneousEffect",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;heal(F)V")
    )
    void modifyHeal( // FIXME
            LivingEntity instance,
            float heal,
            ServerLevel serverLevel,
            Entity source,
            Entity owner,
            LivingEntity mob,
            int amplification,
            double scale
    ) {
        if (!(owner instanceof LivingEntity livingAttacker)) {
            mob.heal(heal);
            return;
        }

        var healingFactor = CustomAttributesExtensions.INSTANCE.getHealingFactor(livingAttacker);
        mob.heal(heal * (float) healingFactor);
    }
}
