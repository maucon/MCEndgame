package de.fuballer.mcendgame.main.fantasy;

import com.google.common.collect.ImmutableList;
import de.fuballer.mcendgame.main.mixin.fantasy.MinecraftServerAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProgressListener;
import net.minecraft.util.Util;
import net.minecraft.world.clock.ServerClockManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.DerivedLevelData;
import net.minecraft.world.level.storage.WorldData;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

public class RuntimeLevel extends ServerLevel {
    @Nullable
    private final GameRules rules;
    @Nullable
    private final ServerClockManager clockManager;

    protected RuntimeLevel(MinecraftServer server, ResourceKey<Level> dimension, RuntimeLevelConfig config) {
        LevelStem dimensionOptions = config.createDimensionOptions(server);
        WorldData worldData = server.getWorldData();
        worldData.setDifficulty(config.getDifficulty());

        GameRules gameRules = new GameRules(worldData.enabledFeatures());

        config.getGameRules().applyTo(gameRules, null);

        this.clockManager = config.getClockManager(server, gameRules);

        super(
                server, Util.backgroundExecutor(), ((MinecraftServerAccess) server).getStorageSource(),
                new DerivedLevelData(worldData, worldData.overworldData()),
                dimension,
                dimensionOptions,
                false,
                0,
                ImmutableList.of(),
                false
        );
        this.rules = gameRules;
    }

    @Override
    public @NonNull GameRules getGameRules() {
        if (this.rules != null) {
            return this.rules;
        }
        return super.getGameRules();
    }

    @Override
    protected void tickTime() {
        if (this.tickTime && this.clockManager instanceof RuntimeClockManager clockManager) {
            clockManager.tickFromLevel(this);
        }
    }

    @Override
    public void save(@Nullable ProgressListener progressListener, boolean flush, boolean enabled) {
    }

    @Override
    public @NonNull ServerClockManager clockManager() {
        return this.clockManager != null ? this.clockManager : super.clockManager();
    }
}
