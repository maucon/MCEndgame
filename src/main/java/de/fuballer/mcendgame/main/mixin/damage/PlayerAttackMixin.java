package de.fuballer.mcendgame.main.mixin.damage;

import de.fuballer.mcendgame.main.accessor.PlayerEntityMixinAccessor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerAttackMixin implements PlayerEntityMixinAccessor {
    @Unique
    private float lastAttackCharge;
    @Unique
    private boolean lastAttackWasCritical;

    @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getAttackStrengthScale(F)F"))
    protected void attack(Entity target, CallbackInfo ci) {
        var player = (Player) (Object) this;
        if (!(player instanceof ServerPlayer)) {
            return;
        }

        // TODO take from minecraft vars
        lastAttackCharge = player.getAttackStrengthScale(0.5F);
        lastAttackWasCritical = lastAttackCharge > 0.9F
                && player.fallDistance > 0.0F
                && !player.onGround()
                && !player.onClimbable()
                && !player.isInWater()
                && !player.hasEffect(MobEffects.BLINDNESS)
                && !player.isPassenger()
                && target instanceof LivingEntity
                && !player.isSprinting();
    }

    @Override
    public float mcendgame$getLastAttackCharge() {
        return lastAttackCharge;
    }

    @Override
    public boolean mcendgame$getLastAttackWasCritical() {
        return lastAttackWasCritical;
    }
}
