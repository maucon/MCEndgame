package de.fuballer.mcendgame.main.accessor;

@SuppressWarnings("unused")
public interface PlayerEntityMixinAccessor {
    float mcendgame$getLastAttackCharge();

    boolean mcendgame$getLastAttackWasCritical();

    boolean mcendgame$getLastAttackWasSweeping();
}