package de.fuballer.mcendgame.main.mixin.goal;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TargetGoal.class)
public interface TargetGoalMobAccessor {
    @Accessor("mob")
    Mob mcendgame$getMob();
}
