package de.fuballer.mcendgame.main.mixin.knockback;

import de.fuballer.mcendgame.main.component.custom_attribute.effects.knockback.AttackKnockbackUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public class LivingEntityKnockbackCommandMixin {
    @Redirect(method = "dealDefaultKnockback", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;knockback(DDDLnet/minecraft/world/damagesource/DamageSource;F)V"))
    void redirectTakeKnockbackInDamage(LivingEntity instance, double power, double xd, double zd, DamageSource source, float damage) {
        AttackKnockbackUtil.INSTANCE.takeKnockbackFrom(instance, source.getEntity(), power, xd, zd);
    }

    @Redirect(method = "blockedByItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;knockback(DDDLnet/minecraft/world/damagesource/DamageSource;F)V"))
    void redirectTakeKnockbackInKnockback(LivingEntity instance, double power, double xd, double zd, DamageSource source, float damage) {
        var self = (LivingEntity) (Object) this;
        AttackKnockbackUtil.INSTANCE.takeKnockbackFrom(instance, self, power, xd, zd);
    }

    @Redirect(method = "causeExtraKnockback", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;knockback(DDDLnet/minecraft/world/damagesource/DamageSource;FZ)V"))
    void redirectTakeKnockbackInKnockbackTarget(LivingEntity instance, double power, double xd, double zd, DamageSource source, float damage, boolean comesFromEffect) {
        var self = (LivingEntity) (Object) this;
        AttackKnockbackUtil.INSTANCE.takeKnockbackFrom(instance, self, power, xd, zd);
    }
}