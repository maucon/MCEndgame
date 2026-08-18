package de.fuballer.mcendgame.main.accessor;

public interface LivingEntityDamageAccessor {
    boolean mcendgame$isInInvulnerabilityFrames();

    void mcendgame$setInInvulnerabilityFrames(boolean value);

    boolean mcendgame$lastHitWasApplied();

    void mcendgame$setLastHitWasApplied(boolean value);
}
