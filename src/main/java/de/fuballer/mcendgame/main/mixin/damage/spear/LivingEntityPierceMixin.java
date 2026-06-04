package de.fuballer.mcendgame.main.mixin.damage.spear;

import de.fuballer.mcendgame.main.component.damage.custom_type.CustomDamageTypes;
import de.fuballer.mcendgame.main.context.PierceContext;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Objects;

@Mixin(LivingEntity.class)
public class LivingEntityPierceMixin {
    @ModifyArg(
            method = "stabAttack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z"
            ),
            index = 1
    )
    private DamageSource modifyDamageSource(DamageSource original) {
        var world = Objects.requireNonNull(original.getEntity()).level();
        var pierceType = Objects.requireNonNull(PierceContext.CURRENT.get());
        PierceContext.CURRENT.remove();

        var damageType = switch (pierceType) {
            case PIERCE -> CustomDamageTypes.INSTANCE.getPIERCE_ATTACK();
            case KINETIC -> CustomDamageTypes.INSTANCE.getKINETIC_ATTACK();
        };

        return CustomDamageTypes.INSTANCE.of(world, damageType, original.getEntity(), original.getDirectEntity());
    }
}
