package de.fuballer.mcendgame.main.mixin.knockback;

import de.fuballer.mcendgame.main.component.custom_attribute.effects.knockback.AttackKnockbackUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.RamTarget;
import net.minecraft.world.entity.animal.goat.Goat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RamTarget.class)
public class RamTargetKnockbackCommandMixin {
    @Redirect(
            method = "tick(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/animal/goat/Goat;J)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;knockback(DDD)V")
    )
    void redirectTakeKnockback(LivingEntity instance, double strength, double x, double z, ServerLevel serverWorld, Goat goatEntity) {
        AttackKnockbackUtil.INSTANCE.takeKnockbackFrom(instance, goatEntity, strength, x, z);
    }
}
