package de.fuballer.mcendgame.main.mixin.damage;

import com.llamalad7.mixinextras.sugar.Local;
import de.fuballer.mcendgame.main.accessor.PlayerEntityMixinAccessor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
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

    @Inject(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;isSweepAttack(ZZZ)Z"
            )
    )
    private void onCriticalAttackSet(
            Entity entity,
            CallbackInfo ci,
            @Local(name = "criticalAttack") boolean criticalAttack,
            @Local(name = "attackStrengthScale") float attackStrengthScale
    ) {
        var player = (Player) (Object) this;
        if (!(player instanceof ServerPlayer)) {
            return;
        }

        lastAttackCharge = attackStrengthScale;
        lastAttackWasCritical = criticalAttack;
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
