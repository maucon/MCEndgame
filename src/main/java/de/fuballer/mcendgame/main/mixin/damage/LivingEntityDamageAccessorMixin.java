package de.fuballer.mcendgame.main.mixin.damage;

import de.fuballer.mcendgame.main.accessor.LivingEntityDamageAccessor;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntity.class)
public abstract class LivingEntityDamageAccessorMixin implements LivingEntityDamageAccessor {
    @Unique
    private boolean isInInvulnerabilityFrames = false;
    @Unique
    private boolean lastHitWasApplied = true;

    @Override
    public boolean mcendgame$isInInvulnerabilityFrames() {
        return this.isInInvulnerabilityFrames;
    }

    @Override
    public void mcendgame$setInInvulnerabilityFrames(boolean value) {
        this.isInInvulnerabilityFrames = value;
    }

    @Override
    public boolean mcendgame$lastHitWasApplied() {
        return this.lastHitWasApplied;
    }

    @Override
    public void mcendgame$setLastHitWasApplied(boolean value) {
        this.lastHitWasApplied = value;
    }
}
