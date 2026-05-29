package de.fuballer.mcendgame.main.mixin.goal;

import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TrackTargetGoal.class)
public interface TrackTargetGoalMobAccessor {
    @Accessor("mob")
    MobEntity mcendgame$getMob();
}
