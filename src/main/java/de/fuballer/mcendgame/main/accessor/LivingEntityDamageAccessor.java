package de.fuballer.mcendgame.main.accessor;

import net.minecraft.world.damagesource.DamageSource;

public interface LivingEntityDamageAccessor {
    void mcendgame$setLastDamageSource(DamageSource damageSource);

    void mcendgame$setLastDamageTime(long time);

    void mcendgame$playThornsSound(DamageSource damageSource);

    boolean mcendgame$tryUseDeathProtector(DamageSource source);
}
