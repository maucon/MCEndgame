package de.fuballer.mcendgame.main.fantasy;

import com.google.common.base.Preconditions;
import de.fuballer.mcendgame.main.fantasy.util.GameRuleStore;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.clock.ClockState;
import net.minecraft.world.clock.PackedClockStates;
import net.minecraft.world.clock.ServerClockManager;
import net.minecraft.world.clock.WorldClock;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRules;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BooleanSupplier;

public final class RuntimeLevelConfig {
    private long seed = 0;
    private ResourceKey<DimensionType> dimensionTypeKey = null;
    private Holder<DimensionType> dimensionType;
    private ChunkGenerator generator = null;
    private Difficulty difficulty = Difficulty.NORMAL;
    private final RuntimeLevel.Constructor levelConstructor = RuntimeLevel::new;
    private final GameRuleStore gameRules = new GameRuleStore();

    private long gameTime = 0;
    private TriState flat = TriState.DEFAULT;
     private final BiFunction<PackedClockStates, BooleanSupplier, RuntimeClockManager> clockManagerConstructor = RuntimeClockManager::new;
    private final Map<ResourceKey<WorldClock>, ClockState> clockState = new HashMap<>();

    /**
     * Sets the level seed
     *
     * @param seed The level seed to use
     * @return The same instance of {@link RuntimeLevelConfig}
     */
    public RuntimeLevelConfig setSeed(long seed) {
        this.seed = seed;
        return this;
    }

    /**
     * Sets the level dimension type
     *
     * @param dimensionType The dimension type to use
     * @return The same instance of {@link RuntimeLevelConfig}
     */
    public RuntimeLevelConfig setDimensionType(ResourceKey<DimensionType> dimensionType) {
        this.dimensionTypeKey = dimensionType;
        this.dimensionType = null;
        return this;
    }

    /**
     * Sets the level chunk generator
     *
     * @param generator The chunk generator to use
     * @return The same instance of {@link RuntimeLevelConfig}
     */
    public RuntimeLevelConfig setGenerator(ChunkGenerator generator) {
        this.generator = generator;
        return this;
    }

    /**
     * Sets the level's game time
     *
     * @param gameTime The new time of the game
     * @return The same instance of {@link RuntimeLevelConfig}
     */
    public RuntimeLevelConfig setGameTime(long gameTime) {
        this.gameTime = gameTime;
        return this;
    }

    /**
     * Sets the level difficulty
     *
     * @param difficulty The difficulty to use
     * @return The same instance of {@link RuntimeLevelConfig}
     */
    public RuntimeLevelConfig setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
        return this;
    }

    public RuntimeLevelConfig setClockTime(ResourceKey<WorldClock> clock, int time) {
        return this.setClockTime(clock, new ClockState(time, 0, 1, false));
    }

    public RuntimeLevelConfig setClockTime(ResourceKey<WorldClock> clock, int time, float rate) {
        return this.setClockTime(clock, new ClockState(time, 0, rate, false));
    }

    public RuntimeLevelConfig setClockTime(ResourceKey<WorldClock> clock, int time, boolean paused) {
        return this.setClockTime(clock, new ClockState(time, 0, 1, paused));
    }

    public RuntimeLevelConfig setClockTime(ResourceKey<WorldClock> clock, ClockState state) {
        this.clockState.put(clock, state);
        return this;
    }

    /**
     * Modifies a gamerule
     *
     * @param key   The gamerule to modify
     * @param value The value of the gamerule
     * @return The same instance of {@link RuntimeLevelConfig}
     */
    public <T> RuntimeLevelConfig setGameRule(GameRule<T> key, T value) {
        this.gameRules.set(key, value);
        return this;
    }

    /**
     * Defines if the level is a flat level or not
     *
     * @param state If the level should be flat, not flat or use the default value
     * @return The same instance of {@link RuntimeLevelConfig}
     */
    public RuntimeLevelConfig setFlat(TriState state) {
        this.flat = state;
        return this;
    }

    /**
     * Defines if the level is a flat level or not
     *
     * @param state If the level should be flat or not
     * @return The same instance of {@link RuntimeLevelConfig}
     */
    public RuntimeLevelConfig setFlat(boolean state) {
        return this.setFlat(TriState.of(state));
    }

    public long getSeed() {
        return this.seed;
    }

    /**
     * Creates new dimension options from the server
     *
     * @return The new dimension options
     */
    public LevelStem createDimensionOptions(MinecraftServer server) {
        var dimensionType = this.resolveDimensionType(server);
        return new LevelStem(dimensionType, this.generator);
    }

    /**
     * Resolves the dimension type from the server
     *
     * @return The dimension type
     */
    private Holder<DimensionType> resolveDimensionType(MinecraftServer server) {
        var dimensionType = this.dimensionType;
        if (dimensionType == null) {
            dimensionType = server.registryAccess().lookupOrThrow(Registries.DIMENSION_TYPE).get(this.dimensionTypeKey).orElse(null);
            Preconditions.checkNotNull(dimensionType, "invalid dimension type " + this.dimensionTypeKey);
        }
        return dimensionType;
    }

    public RuntimeLevel.Constructor getLevelConstructor() {
        return this.levelConstructor;
    }

    public boolean shouldTickTime() {
        return false;
    }

    public Difficulty getDifficulty() {
        return this.difficulty;
    }

    public GameRuleStore getGameRules() {
        return this.gameRules;
    }

    public long getGameTime() {
        return this.gameTime;
    }

    public TriState isFlat() {
        return this.flat;
    }

    public ServerClockManager getClockManager(MinecraftServer server, GameRules gameRules) {
        PackedClockStates states = PackedClockStates.EMPTY;
        if (!this.clockState.isEmpty()) {
            var map = new HashMap<Holder<WorldClock>, ClockState>();
            for (var entry : this.clockState.entrySet()) {
                map.put(server.registryAccess().getOrThrow(entry.getKey()), entry.getValue());
            }
            states = new PackedClockStates(map);
        }

        var t = this.clockManagerConstructor.apply(states, () -> gameRules.get(GameRules.ADVANCE_TIME));
        t.init(server);

        return t;
    }
}
