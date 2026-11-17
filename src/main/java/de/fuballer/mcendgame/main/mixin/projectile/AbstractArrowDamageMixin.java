package de.fuballer.mcendgame.main.mixin.projectile;

import de.fuballer.mcendgame.main.accessor.AbstractArrowDamageAccessor;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractArrow.class)
public class AbstractArrowDamageMixin implements AbstractArrowDamageAccessor {
    @Shadow
    private double baseDamage;

    @Unique
    private double damageFactor = 1.0;

    @Override
    public double mcendgame$getDamage() {
        return this.baseDamage;
    }

    @Override
    public double mcendgame$getDamageFactor() {
        return damageFactor;
    }

    @Override
    public void mcendgame$setDamageFactor(double factor) {
        damageFactor = factor;
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    void mcendgame$addAdditionalSaveData(ValueOutput output, CallbackInfo ci) {
        output.putDouble("damageFactor", damageFactor);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    void mcendgame$readAdditionalSaveData(ValueInput input, CallbackInfo ci) {
        damageFactor = input.getDoubleOr("damageFactor", 1.0);
    }
}
