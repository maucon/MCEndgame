package de.fuballer.mcendgame.main.mixin.damage.spear;

import de.fuballer.mcendgame.main.component.damage.custom_type.CustomDamageTypes;
import de.fuballer.mcendgame.main.context.PierceContext;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Objects;

@Mixin(Player.class)
public class PlayerPierceMixin {
    @ModifyArg(
            method = "stabAttack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;hurtOrSimulate(Lnet/minecraft/world/damagesource/DamageSource;F)Z"
            ),
            index = 0
    )
    private DamageSource modifyDamageSource(DamageSource original) {
        var world = Objects.requireNonNull(original.getEntity()).level();
        var pierceType = PierceContext.CURRENT.get();
        if (pierceType == null) {
            return original;
        }
        PierceContext.CURRENT.remove();

        var damageType = switch (pierceType) {
            case PIERCE -> CustomDamageTypes.INSTANCE.getPIERCE_ATTACK();
            case KINETIC -> CustomDamageTypes.INSTANCE.getKINETIC_ATTACK();
        };

        return CustomDamageTypes.INSTANCE.of(world, damageType, original.getEntity(), original.getDirectEntity());
    }
}
