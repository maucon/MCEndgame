package de.fuballer.mcendgame.main.mixin.damage;

import de.fuballer.mcendgame.main.component.damage.custom_type.CustomDamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Objects;

@Mixin(Player.class)
public abstract class PlayerSweepingAttackMixin {
    @ModifyArg(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;doSweepAttack(Lnet/minecraft/world/entity/Entity;FLnet/minecraft/world/damagesource/DamageSource;F)V"
            ),
            index = 2
    )
    private DamageSource modifyServerAttack(DamageSource original) {
        var world = Objects.requireNonNull(original.getEntity()).level();

        return CustomDamageTypes.INSTANCE.of(
                world,
                CustomDamageTypes.INSTANCE.getSWEEPING(),
                original.getEntity(),
                original.getDirectEntity()
        );
    }
}
