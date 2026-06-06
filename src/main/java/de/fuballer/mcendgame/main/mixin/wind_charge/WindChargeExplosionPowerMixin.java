package de.fuballer.mcendgame.main.mixin.wind_charge;

import de.fuballer.mcendgame.main.accessor.WindChargeEntityExplosionPowerAccessor;
import net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.WindCharge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(WindCharge.class)
public class WindChargeExplosionPowerMixin implements WindChargeEntityExplosionPowerAccessor {
    @Unique
    private float explosionPower = Float.NaN;

    @ModifyArg(
            method = "explode",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;explode(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;Lnet/minecraft/world/level/ExplosionDamageCalculator;DDDFZLnet/minecraft/world/level/Level$ExplosionInteraction;Lnet/minecraft/core/particles/ParticleOptions;Lnet/minecraft/core/particles/ParticleOptions;Lnet/minecraft/util/random/WeightedList;Lnet/minecraft/core/Holder;)V"
            )
    )
    float getExplosionPower(float originalPower) {
        return Float.isNaN(explosionPower) ? originalPower : explosionPower;
    }

    @Override
    public void mcendgame$setExplosionPower(float power) {
        explosionPower = power;
    }
}
