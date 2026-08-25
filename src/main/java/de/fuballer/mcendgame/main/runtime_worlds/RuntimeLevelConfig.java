package de.fuballer.mcendgame.main.runtime_worlds;

import com.google.common.base.Preconditions;
import de.fuballer.mcendgame.main.component.dimension.CustomDimensions;
import de.fuballer.mcendgame.main.runtime_worlds.util.GameRuleStore;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.clock.*;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRules;

import java.util.Map;

public final class RuntimeLevelConfig {
    private final ResourceKey<DimensionType> dimensionTypeKey = CustomDimensions.INSTANCE.getDUNGEON();
    private final GameRuleStore gameRules = new GameRuleStore();
    private final Difficulty difficulty = Difficulty.HARD;
    private ChunkGenerator generator = null;
    private int clockTime = 0;

    public RuntimeLevelConfig setClockTime(int clockTime) {
        this.clockTime = clockTime;
        return this;
    }

    public RuntimeLevelConfig setGenerator(ChunkGenerator generator) {
        this.generator = generator;
        return this;
    }

    public <T> RuntimeLevelConfig setGameRule(GameRule<T> key, T value) {
        this.gameRules.set(key, value);
        return this;
    }

    public LevelStem createDimensionOptions(MinecraftServer server) {
        var dimensionType = this.resolveDimensionType(server);
        return new LevelStem(dimensionType, this.generator);
    }

    private Holder<DimensionType> resolveDimensionType(MinecraftServer server) {
        var dimensionType = server.registryAccess().lookupOrThrow(Registries.DIMENSION_TYPE).get(this.dimensionTypeKey).orElse(null);
        Preconditions.checkNotNull(dimensionType, "invalid dimension type " + this.dimensionTypeKey);
        return dimensionType;
    }

    public GameRuleStore getGameRules() {
        return this.gameRules;
    }

    public ServerClockManager getClockManager(MinecraftServer server, GameRules gameRules) {
        Map<Holder<WorldClock>, ClockState> clockMap = Map.of(
                server.registryAccess().getOrThrow(WorldClocks.OVERWORLD),
                new ClockState(clockTime, 0, 1, true)
        );
        PackedClockStates states = new PackedClockStates(clockMap);

        var clockManager = new RuntimeClockManager(states, () -> gameRules.get(GameRules.ADVANCE_TIME));
        clockManager.init(server);

        return clockManager;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }
}
