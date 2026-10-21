package de.fuballer.mcendgame.main.mixin.move_control;

import net.minecraft.world.entity.ai.control.MoveControl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MoveControl.class)
public interface MoveControlAccessor {
    @Accessor("strafeRight")
    float mcendgame$getStrafeRight();

    @Accessor("strafeForwards")
    float mcendgame$getStrafeForwards();
}