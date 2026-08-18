package de.fuballer.mcendgame.main.accessor;

public interface LivingEntityDamageAccessor {
    boolean mcendgame$isInInvulnerabilityFrames();

    void mcendgame$setInInvulnerabilityFrames(boolean value);

    boolean mcendgame$lastHitWasApplied();

    void mcendgame$setLastHitWasApplied(boolean value);

    float mcendgame$getLastResisted();

    void mcendgame$setLastResisted(float value);

    float mcendgame$getLastHurt();

    void mcendgame$setLastHurt(float value);
}
