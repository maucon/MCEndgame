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
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.gamerules.GameRules;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

public class RuntimeLevel extends ServerLevel {
    final Style style;
    private final boolean flat;
    @Nullable
    private final GameRules rules;
    @Nullable
    private final ServerClockManager clockManager;

    protected RuntimeLevel(MinecraftServer server, ResourceKey<Level> dimension, RuntimeLevelConfig config, Style style) {
        LevelStem dimensionOptions = config.createDimensionOptions(server);
        GameRules gameRules = new GameRules(server.getWorldData().enabledFeatures());

        config.getGameRules().applyTo(gameRules, null);

        this.clockManager = config.getClockManager(server, gameRules);
        this.style = style;

        super(
                server, Util.backgroundExecutor(), ((MinecraftServerAccess) server).getStorageSource(),
                new RuntimeLevelData(server.getWorldData(), config),
                dimension,
                dimensionOptions,
                false,
                BiomeManager.obfuscateSeed(config.getSeed()),
                ImmutableList.of(),
                config.shouldTickTime()
        );
        this.flat = config.isFlat().orElse(super.isFlat());
        this.rules = gameRules;
    }

    @Override
    public GameRules getGameRules() {
        if (this.rules != null) {
            return this.rules;
        }
        return super.getGameRules();
    }

    @Override
    public long getSeed() {
        return this.getRuntimeLevelData().config.getSeed();
    }

    @Override
    protected void tickTime() {
        this.getRuntimeLevelData().setGameTime(this.getServer().overworld().getGameTime());

        if (this.tickTime && this.clockManager instanceof RuntimeClockManager clockManager) {
            clockManager.tickFromLevel(this);
        }
    }

    @Override
    public void save(@Nullable ProgressListener progressListener, boolean flush, boolean enabled) {
        if (this.style == Style.PERSISTENT || !flush) {
            super.save(progressListener, flush, enabled);
        }
    }

    @Override
    public boolean isFlat() {
        return this.flat;
    }

    private RuntimeLevelData getRuntimeLevelData() {
        return (RuntimeLevelData) this.levelData;
    }

    @Override
    public @NonNull ServerClockManager clockManager() {
        return this.clockManager != null ? this.clockManager : super.clockManager();
    }

    public enum Style {
        PERSISTENT,
        TEMPORARY
    }

    public interface Constructor {
        RuntimeLevel createLevel(MinecraftServer server, ResourceKey<Level> registryKey, RuntimeLevelConfig config, Style style);
    }
}
