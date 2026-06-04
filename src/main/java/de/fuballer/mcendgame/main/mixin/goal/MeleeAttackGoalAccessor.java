package de.fuballer.mcendgame.main.mixin.goal;

import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MeleeAttackGoal.class)
public interface MeleeAttackGoalAccessor {
    @Accessor("ticksUntilNextPathRecalculation")
    void mcendgame$setTicksUntilNextPathRecalculation(int ticks);

    @Accessor("ticksUntilNextPathRecalculation")
    int mcendgame$getTicksUntilNextPathRecalculation();

    @Accessor("pathedTargetX")
    void mcendgame$setPathedTargetX(double ticks);

    @Accessor("pathedTargetX")
    double mcendgame$getPathedTargetX();

    @Accessor("pathedTargetY")
    void mcendgame$setPathedTargetY(double ticks);

    @Accessor("pathedTargetY")
    double mcendgame$getPathedTargetY();

    @Accessor("pathedTargetZ")
    void mcendgame$setPathedTargetZ(double ticks);

    @Accessor("pathedTargetZ")
    double mcendgame$getPathedTargetZ();

    @Accessor("ticksUntilNextAttack")
    void mcendgame$setTicksUntilNextAttack(int cooldown);
}
