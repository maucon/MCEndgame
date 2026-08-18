package de.fuballer.mcendgame.main.mixin.damage;

import de.fuballer.mcendgame.main.accessor.LivingEntityDamageAccessor;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntity.class)
public abstract class LivingEntityDamageAccessorMixin implements LivingEntityDamageAccessor {
    @Shadow
    protected float lastHurt;

    @Unique
    private boolean isInInvulnerabilityFrames = false;
    @Unique
    private boolean lastHitWasApplied = true;
    @Unique
    private float lastResisted = 0f;

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

    @Override
    public float mcendgame$getLastResisted() {
        return this.lastResisted;
    }

    @Override
    public void mcendgame$setLastResisted(float value) {
        this.lastResisted = value;
    }

    @Override
    public float mcendgame$getLastHurt() {
        return this.lastHurt;
    }

    @Override
    public void mcendgame$setLastHurt(float value) {
        this.lastHurt = value;
    }
}
