package de.fuballer.mcendgame.main.mixin.knockback;

import de.fuballer.mcendgame.main.component.custom_attribute.effects.knockback.AttackKnockbackUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public class LivingEntityKnockbackCommandMixin {
    @Redirect(method = "hurtServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;knockback(DDD)V"))
    void redirectTakeKnockbackInDamage(LivingEntity instance, double strength, double x, double z, ServerLevel world, DamageSource source, float amount) {
        AttackKnockbackUtil.INSTANCE.takeKnockbackFrom(instance, source.getEntity(), strength, x, z);
    }

    @Redirect(method = "blockedByItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;knockback(DDD)V"))
    void redirectTakeKnockbackInKnockback(LivingEntity instance, double strength, double x, double z) {
        var self = (LivingEntity) (Object) this;
        AttackKnockbackUtil.INSTANCE.takeKnockbackFrom(instance, self, strength, x, z);
    }

    @Redirect(method = "causeExtraKnockback", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;knockback(DDD)V"))
    void redirectTakeKnockbackInKnockbackTarget(LivingEntity instance, double strength, double x, double z) {
        var self = (LivingEntity) (Object) this;
        AttackKnockbackUtil.INSTANCE.takeKnockbackFrom(instance, self, strength, x, z);
    }
}