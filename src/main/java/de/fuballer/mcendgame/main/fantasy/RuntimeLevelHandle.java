package de.fuballer.mcendgame.main.fantasy;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public final class RuntimeLevelHandle {
    private final ServerLevel level;

    RuntimeLevelHandle(ServerLevel level) {
        this.level = level;
    }

    public ServerLevel asLevel() {
        return this.level;
    }

    public ResourceKey<Level> getRegistryKey() {
        return this.level.dimension();
    }
}
