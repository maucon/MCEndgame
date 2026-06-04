package de.fuballer.mcendgame.main.mixin.damage;

import de.fuballer.mcendgame.main.accessor.LivingEntityDamageAccessor;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LivingEntity.class)
public abstract class LivingEntityDamageAccessorMixin implements LivingEntityDamageAccessor {
    @Shadow
    private DamageSource lastDamageSource;
    @Shadow
    private long lastDamageStamp;

    @Shadow
    protected abstract void playSecondaryHurtSound(DamageSource damageSource);

    @Shadow
    protected abstract boolean checkTotemDeathProtection(DamageSource source);

    @Override
    public void mcendgame$setLastDamageSource(DamageSource damageSource) {
        this.lastDamageSource = damageSource;
    }

    @Override
    public void mcendgame$setLastDamageTime(long time) {
        this.lastDamageStamp = time;
    }

    @Override
    public void mcendgame$playThornsSound(DamageSource damageSource) {
        this.playSecondaryHurtSound(damageSource);
    }

    @Override
    public boolean mcendgame$tryUseDeathProtector(DamageSource source) {
        return this.checkTotemDeathProtection(source);
    }
}
