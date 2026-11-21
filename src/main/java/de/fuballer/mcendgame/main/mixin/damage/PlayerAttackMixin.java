package de.fuballer.mcendgame.main.mixin.damage;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import de.fuballer.mcendgame.main.accessor.PlayerEntityMixinAccessor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Player.class)
public abstract class PlayerAttackMixin implements PlayerEntityMixinAccessor {
    @Unique
    private float lastAttackCharge;
    @Unique
    private boolean lastAttackWasCritical;
    @Unique
    private boolean lastAttackWasSweeping;

    @ModifyExpressionValue(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;isSweepAttack(ZZZ)Z"
            )
    )
    private boolean onAttack(
            boolean sweepAttack,
            @Local(name = "attackStrengthScale") float attackStrengthScale,
            @Local(name = "criticalAttack") boolean criticalAttack
    ) {
        var player = (Player) (Object) this;
        if (!(player instanceof ServerPlayer)) {
            return sweepAttack;
        }

        lastAttackCharge = attackStrengthScale;
        lastAttackWasCritical = criticalAttack;
        lastAttackWasSweeping = sweepAttack;

        return sweepAttack;
    }

    @Override
    public float mcendgame$getLastAttackCharge() {
        return lastAttackCharge;
    }

    @Override
    public boolean mcendgame$getLastAttackWasCritical() {
        return lastAttackWasCritical;
    }

    @Override
    public boolean mcendgame$getLastAttackWasSweeping() {
        return lastAttackWasSweeping;
    }
}
