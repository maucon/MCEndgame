package de.fuballer.mcendgame.main.mixin.knockback;

import de.fuballer.mcendgame.main.component.custom_attribute.effects.knockback.AttackKnockbackUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Player.class)
public class PlayerKnockbackCommandMixin {
    @Redirect(
            method = "causeExtraKnockback",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;knockback(DDDLnet/minecraft/world/damagesource/DamageSource;FZ)V")
    )
    void redirectTakeKnockback(LivingEntity instance, double power, double xd, double zd, DamageSource source, float damage, boolean comesFromEffect) {
        var player = (Player) (Object) this;
        AttackKnockbackUtil.INSTANCE.takeKnockbackFrom(instance, player, power, xd, zd);
    }
}
