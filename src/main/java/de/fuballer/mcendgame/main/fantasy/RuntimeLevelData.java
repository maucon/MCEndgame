package de.fuballer.mcendgame.main.fantasy;

import net.minecraft.world.Difficulty;
import net.minecraft.world.level.storage.DerivedLevelData;
import net.minecraft.world.level.storage.WorldData;
import org.jspecify.annotations.NonNull;

public final class RuntimeLevelData extends DerivedLevelData {
    final RuntimeLevelConfig config;

    public RuntimeLevelData(WorldData worldData, RuntimeLevelConfig config) {
        super(worldData, worldData.overworldData());
        this.config = config;
    }

    @Override
    public long getGameTime() {
        return this.config.getGameTime();
    }

    @Override
    public void setGameTime(long time) {
        this.config.setGameTime(time);
    }

    @Override
    public @NonNull Difficulty getDifficulty() {
        return this.config.getDifficulty();
    }
}
