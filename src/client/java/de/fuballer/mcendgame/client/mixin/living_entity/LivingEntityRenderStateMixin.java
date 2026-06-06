package de.fuballer.mcendgame.client.mixin.living_entity;

import de.fuballer.mcendgame.client.accessor.LivingEntityRenderStateAccessor;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntityRenderState.class)
public class LivingEntityRenderStateMixin implements LivingEntityRenderStateAccessor {
    @Unique
    public float health;
    @Unique
    public float maxHealth;
    @Unique
    public int lowHealthTicks20;

    @Override
    public float mcendgame$getHealth() {
        return health;
    }

    @Override
    public void mcendgame$setHealth(float health) {
        this.health = health;
    }

    @Override
    public float mcendgame$getMaxHealth() {
        return maxHealth;
    }

    @Override
    public void mcendgame$setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
    }

    @Override
    public int mcendgame$getLowHealthTicks20() {
        return lowHealthTicks20;
    }

    @Override
    public void mcendgame$setLowHealthTicks20(int lowHealthTicks20) {
        this.lowHealthTicks20 = lowHealthTicks20;
    }
}
