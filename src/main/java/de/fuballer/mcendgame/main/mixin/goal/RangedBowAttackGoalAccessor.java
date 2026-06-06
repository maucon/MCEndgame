package de.fuballer.mcendgame.main.mixin.goal;

import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RangedBowAttackGoal.class)
public interface RangedBowAttackGoalAccessor<T extends Monster & RangedAttackMob> {
    @Accessor("mob")
    T getMob();
}
