package de.fuballer.mcendgame.main.runtime_worlds;

import net.minecraft.core.Holder;
import net.minecraft.world.clock.ServerClockManager;
import net.minecraft.world.clock.WorldClock;
import org.jetbrains.annotations.ApiStatus;

import java.util.Map;

public interface ServerClockManagerExtension {
    default Map<Holder<WorldClock>, ServerClockManager.ClockInstance> mcendgame$getClocks() {
        throw new UnsupportedOperationException("Implemented via Mixin.");
    }
}
