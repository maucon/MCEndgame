package de.fuballer.mcendgame.main.mixin.runtime_worlds.clock;

import net.minecraft.world.clock.ServerClockManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerClockManager.ClockInstance.class)
public interface ClockInstanceAccessor {
    @Accessor
    boolean isPaused();

    @Accessor
    long getTotalTicks();

    @Accessor
    float getPartialTick();

    @Accessor
    float getRate();
}
