package de.fuballer.mcendgame.main.mixin.goal;

import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MeleeAttackGoal.class)
public interface MeleeAttackGoalAccessor {
    @Accessor("updateCountdownTicks")
    void mcendgame$setUpdateCountdownTicks(int ticks);

    @Accessor("updateCountdownTicks")
    int mcendgame$getUpdateCountdownTicks();

    @Accessor("targetX")
    void mcendgame$setTargetX(double ticks);

    @Accessor("targetX")
    double mcendgame$getTargetX();

    @Accessor("targetY")
    void mcendgame$setTargetY(double ticks);

    @Accessor("targetY")
    double mcendgame$getTargetY();

    @Accessor("targetZ")
    void mcendgame$setTargetZ(double ticks);

    @Accessor("targetZ")
    double mcendgame$getTargetZ();

    @Accessor("cooldown")
    void mcendgame$setCooldown(int cooldown);
}
