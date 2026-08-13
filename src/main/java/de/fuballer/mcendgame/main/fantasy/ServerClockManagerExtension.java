package de.fuballer.mcendgame.main.fantasy;

import net.minecraft.core.Holder;
import net.minecraft.world.clock.ServerClockManager;
import net.minecraft.world.clock.WorldClock;
import org.jetbrains.annotations.ApiStatus;

import java.util.Map;

@ApiStatus.Internal
public interface ServerClockManagerExtension {
    default Map<Holder<WorldClock>, ServerClockManager.ClockInstance> fantasy$getClocks() {
        throw new UnsupportedOperationException("Implemented via Mixin.");
    }
}
